package es.goeventsnow.backend.unit;

import java.security.Principal;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import es.goeventsnow.backend.dto.user.UserDTO;
import es.goeventsnow.backend.dto.user.UserMapper;
import es.goeventsnow.backend.model.Participant;
import es.goeventsnow.backend.dto.participant.ParticipantDTO;
import es.goeventsnow.backend.dto.participant.ParticipantMapper;
import es.goeventsnow.backend.repository.ParticipantRepository;
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

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private ParticipantMapper participantMapper;

    @InjectMocks
    private UserService userService;

    private User firstMockUser;
    private User secondMockUser;
    private UserDTO firstMockUserDTO;
    private UserDTO secondMockUserDTO;
    private List<User> userList;
    private Participant mockParticipant;
    private ParticipantDTO mockParticipantDTO;

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
                "mockUser1@example.com", "firstPassword", 0, "None", false, null, List.of("USER"),  new ArrayList<>());
        secondMockUserDTO = new UserDTO(2L, "Mock User 2", "secondMockUser", 987654321,
                "mockUser2@example.com", "secondPassword", 0, "None", false, null, List.of("USER"), new ArrayList<>());

        userList = List.of(firstMockUser, secondMockUser);

        mockParticipant = new Participant("Mock Participant", "Music", "Bio");
        mockParticipant.setId(10L);
        mockParticipant.setNumFollowers(0);

        mockParticipantDTO = new ParticipantDTO(10L, "Mock Participant", "Music", "Bio", false, 0);
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
                "encodedPassword", 0, "None", false, null, List.of("USER"), null);
        User newUser = new User("newUser", "New User", 111222333, "new@example.com", "encodedPassword", "USER");
        UserDTO savedUserDTO = new UserDTO(3L, "New User", "newUser", 111222333, "new@example.com",
                "encodedPassword", 0, "None", false, null, List.of("USER"), null);

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
                "updated@example.com", "ignoredPassword", 0, "None", false, null, List.of("USER") , null);
        UserDTO updatedUserDTO = new UserDTO(1L, "Updated Name", "firstMockUser", 111222333,
                "updated@example.com", "firstPassword", 0, "None", false, null, List.of("USER"),null);

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

    @Test
    public void followParticipantTest() throws SQLException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(firstMockUser));
        when(participantRepository.findById(10L)).thenReturn(Optional.of(mockParticipant));

        when(userRepository.save(any(User.class))).thenReturn(firstMockUser);
        when(participantRepository.save(any(Participant.class))).thenReturn(mockParticipant);

        UserDTO followedUserDTO = new UserDTO(1L, "Mock User 1", "firstMockUser", 123456789,
            "mockUser1@example.com", "firstPassword", 0, "None", false, null, List.of("USER"), List.of(mockParticipantDTO));

        when(userMapper.toDTO(firstMockUser)).thenReturn(followedUserDTO);

        UserDTO userFollowed = userService.followParticipant(1L, 10L);

        assertTrue(userFollowed.followedParticipants().stream().anyMatch(p -> p.id().equals(10L)));
        verify(userRepository, times(1)).findById(1L);
        verify(participantRepository, times(1)).findById(10L);
        verify(userRepository, times(1)).save(any(User.class));
        verify(participantRepository, times(1)).save(any(Participant.class));
    }

    @Test
    public void unfollowParticipantTest() throws SQLException {
  
        firstMockUser.setFollowedParticipants(new ArrayList<>(List.of(mockParticipant)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(firstMockUser));
        when(participantRepository.findById(10L)).thenReturn(Optional.of(mockParticipant));

        UserDTO unfollowedUserDTO = new UserDTO(1L, "Mock User 1", "firstMockUser", 123456789,
            "mockUser1@example.com", "firstPassword", 0, "None", false, null, List.of("USER"), new ArrayList<>());

        when(userRepository.save(any(User.class))).thenReturn(firstMockUser);
        when(participantRepository.save(any(Participant.class))).thenReturn(mockParticipant);
        when(userMapper.toDTO(firstMockUser)).thenReturn(unfollowedUserDTO);

        UserDTO userUnfollowed = userService.unfollowParticipant(1L, 10L);

        assertFalse(userUnfollowed.followedParticipants().stream().anyMatch(p -> p.id().equals(10L)));
        verify(userRepository, times(1)).findById(1L);
        verify(participantRepository, times(1)).findById(10L);
        verify(userRepository, times(1)).save(any(User.class));
        verify(participantRepository, times(1)).save(any(Participant.class));
    }

    @Test
    public void getFollowedParticipantsTest() throws SQLException {
  
        firstMockUser.setFollowedParticipants(new ArrayList<>(List.of(mockParticipant)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(firstMockUser));

        Page<Participant> participantsFollowed = new PageImpl<>(List.of(mockParticipant));
        when(participantRepository.findByIdIn(List.of(10L), Pageable.unpaged())).thenReturn(participantsFollowed);

        when(participantMapper.toDTO(mockParticipant)).thenReturn(mockParticipantDTO);

        Page<ParticipantDTO> result = userService.getFollowedParticipants(1L, Pageable.unpaged());

        assertTrue(result.getContent().stream().anyMatch(p -> p.id().equals(10L)));
        verify(userRepository, times(1)).findById(1L);
        verify(participantRepository, times(1)).findByIdIn(List.of(10L), Pageable.unpaged());
        verify(participantMapper, times(1)).toDTO(mockParticipant);
    }

    @Test
    public void followParticipantAlreadyFollowedThrowsBadRequestTest() throws SQLException {
        firstMockUser.setFollowedParticipants(new ArrayList<>(List.of(mockParticipant)));

        when(userRepository.findById(1L)).thenReturn(Optional.of(firstMockUser));
        when(participantRepository.findById(10L)).thenReturn(Optional.of(mockParticipant));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.followParticipant(1L, 10L));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(userRepository, times(1)).findById(1L);
        verify(participantRepository, times(1)).findById(10L);
        verify(userRepository, never()).save(any(User.class));
        verify(participantRepository, never()).save(any(Participant.class));
    }

}
