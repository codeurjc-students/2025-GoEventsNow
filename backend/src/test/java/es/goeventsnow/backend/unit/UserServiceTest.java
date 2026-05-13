package es.goeventsnow.backend.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import es.goeventsnow.backend.dto.user.UserDTO;
import es.goeventsnow.backend.dto.user.UserMapper;
import es.goeventsnow.backend.model.User;
import es.goeventsnow.backend.repository.UserRepository;
import es.goeventsnow.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Principal principal;

    @InjectMocks
    private UserService userService;

    private User firstMockUser;
    private User secondMockUser;
    private UserDTO firstMockUserDTO;
    private UserDTO secondMockUserDTO;
    private List<User> userList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        firstMockUser = new User("firstMockUser", "Mock User 1", 123456789, "mockUser1@example.com",
                "firstPassword", "USER");
        firstMockUser.setId(1L);

        secondMockUser = new User("secondMockUser", "Mock User 2", 987654321, "mockUser2@example.com",
                "secondPassword", "USER");
        secondMockUser.setId(2L);

        firstMockUserDTO = new UserDTO(1L, "Mock User 1", "firstMockUser", 123456789,
                "mockUser1@example.com", "firstPassword", 0, "None", false, null, List.of("USER"));
        secondMockUserDTO = new UserDTO(2L, "Mock User 2", "secondMockUser", 987654321,
                "mockUser2@example.com", "secondPassword", 0, "None", false, null, List.of("USER"));

        userList = List.of(firstMockUser, secondMockUser);
    }

    @Test
    public void getAllUsersTest() {
        Collection<UserDTO> userDTOs = List.of(firstMockUserDTO, secondMockUserDTO);

        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.toDTOs(userList)).thenReturn(userDTOs);

        Collection<UserDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertSame(userDTOs, result);
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDTOs(userList);
    }

    @Test
    public void findByIdTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(firstMockUser));
        when(userMapper.toDTO(firstMockUser)).thenReturn(firstMockUserDTO);

        UserDTO result = userService.findById(1L);

        assertNotNull(result);
        assertEquals("firstMockUser", result.username());
        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, times(1)).toDTO(firstMockUser);
    }

    @Test
    public void findByIdNotFoundTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.findById(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(userRepository, times(1)).findById(1L);
        verify(userMapper, never()).toDTO(any(User.class));
    }

    @Test
    public void findByUsernameTest() {
        when(userRepository.findByUsername("firstMockUser")).thenReturn(Optional.of(firstMockUser));
        when(userMapper.toDTO(firstMockUser)).thenReturn(firstMockUserDTO);

        UserDTO result = userService.findByUsername("firstMockUser");

        assertNotNull(result);
        assertEquals("firstMockUser", result.username());
        verify(userRepository, times(1)).findByUsername("firstMockUser");
        verify(userMapper, times(1)).toDTO(firstMockUser);
    }

    @Test
    public void findByUsernameNotFoundTest() {
        when(userRepository.findByUsername("missingUser")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.findByUsername("missingUser"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(userRepository, times(1)).findByUsername("missingUser");
        verify(userMapper, never()).toDTO(any(User.class));
    }

    @Test
    public void userExistsTest() {
        when(userRepository.findByUsername("firstMockUser")).thenReturn(Optional.of(firstMockUser));

        boolean result = userService.userExists("firstMockUser");

        assertTrue(result);
        verify(userRepository, times(1)).findByUsername("firstMockUser");
    }

    @Test
    public void userDoesNotExistTest() {
        when(userRepository.findByUsername("missingUser")).thenReturn(Optional.empty());

        boolean result = userService.userExists("missingUser");

        assertFalse(result);
        verify(userRepository, times(1)).findByUsername("missingUser");
    }

    @Test
    public void createUserTest() throws SQLException {
        UserDTO newUserDTO = new UserDTO(null, "New User", "newUser", 111222333, "new@example.com",
                "encodedPassword", 0, "None", false, null, List.of("USER"));
        User newUser = new User("newUser", "New User", 111222333, "new@example.com", "encodedPassword", "USER");
        UserDTO savedUserDTO = new UserDTO(3L, "New User", "newUser", 111222333, "new@example.com",
                "encodedPassword", 0, "None", false, null, List.of("USER"));

        when(userMapper.toDomain(newUserDTO)).thenReturn(newUser);
        when(userMapper.toDTO(newUser)).thenReturn(savedUserDTO);

        UserDTO result = userService.createUser(newUserDTO);

        assertEquals(3L, result.id());
        assertEquals("newUser", result.username());
        verify(userMapper, times(1)).toDomain(newUserDTO);
        verify(userRepository, times(1)).save(newUser);
        verify(userMapper, times(1)).toDTO(newUser);
    }

    @Test
    public void createUserWithIdThrowsBadRequestTest() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.createUser(firstMockUserDTO));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void replaceUserTest() throws SQLException {
        UserDTO updateUserDTO = new UserDTO(1L, "Updated Name", "ignoredUsername", 111222333,
                "updated@example.com", "ignoredPassword", 0, "None", false, null, List.of("USER"));
        UserDTO updatedUserDTO = new UserDTO(1L, "Updated Name", "firstMockUser", 111222333,
                "updated@example.com", "firstPassword", 0, "None", false, null, List.of("USER"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(firstMockUser));
        when(userMapper.toDTO(firstMockUser)).thenReturn(updatedUserDTO);

        UserDTO result = userService.replaceUser(1L, updateUserDTO);

        assertEquals("Updated Name", result.fullname());
        assertEquals(111222333, result.phone());
        assertEquals("updated@example.com", result.email());
        assertEquals("firstMockUser", firstMockUser.getUsername());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(firstMockUser);
        verify(userMapper, times(1)).toDTO(firstMockUser);
    }

    @Test
    public void replaceUserNotFoundTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.replaceUser(1L, firstMockUserDTO));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(userRepository, never()).save(any(User.class));
        verify(userMapper, never()).toDTO(any(User.class));
    }

    @Test
    public void getAuthenticatedUserTest() {
        when(request.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("firstMockUser");
        when(userRepository.findByUsername("firstMockUser")).thenReturn(Optional.of(firstMockUser));
        when(userMapper.toDTO(firstMockUser)).thenReturn(firstMockUserDTO);

        UserDTO result = userService.getAuthenticatedUser(request);

        assertEquals("firstMockUser", result.username());
        verify(request, times(1)).getUserPrincipal();
        verify(userRepository, times(1)).findByUsername("firstMockUser");
    }

    @Test
    public void getAuthenticatedUserWithoutPrincipalThrowsUnauthorizedTest() {
        when(request.getUserPrincipal()).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.getAuthenticatedUser(request));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        verify(userRepository, never()).findByUsername(any(String.class));
    }
}
