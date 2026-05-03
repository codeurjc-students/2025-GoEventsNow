package es.goeventsnow.backend.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import es.goeventsnow.backend.dto.user.UserDTO;
import es.goeventsnow.backend.model.User;
import es.goeventsnow.backend.service.UserService;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class UserRepositoryTest extends IntegrationTestBase {

    @Autowired
    private UserService userService;

    @Test
    public void shouldReturnAllSavedUsersThroughService() {
        User firstUser = createAndSaveUser("user1", "User One", 123456789, "password1", "user1@example.com");
        User secondUser = createAndSaveUser("user2", "User Two", 987654321, "password2", "user2@example.com");

        Collection<UserDTO> allUsers = userService.getAllUsers();

        assertTrue(allUsers.stream().anyMatch(userDTO -> userDTO.username().equals(firstUser.getUsername())));
        assertTrue(allUsers.stream().anyMatch(userDTO -> userDTO.username().equals(secondUser.getUsername())));
    }

    @Test
    public void shouldFindUserByUsernameThroughService() {
        User savedUser = createAndSaveUser("username", "Find By Username", 123456789, "password", "find@example.com");

        UserDTO retrievedUser = userService.findByUsername(savedUser.getUsername());

        assertEquals(savedUser.getId(), retrievedUser.id());
        assertEquals(savedUser.getUsername(), retrievedUser.username());
        assertEquals(savedUser.getEmail(), retrievedUser.email());
        assertFalse(retrievedUser.username().isEmpty());
    }

    @Test
    public void shouldFindUserByIdThroughService() {
        User savedUser = createAndSaveUser("username_id", "Find By Id", 987654321, "password", "find-id@example.com");

        UserDTO retrievedUser = userService.findById(savedUser.getId());

        assertEquals(savedUser.getId(), retrievedUser.id());
        assertEquals(savedUser.getUsername(), retrievedUser.username());
        assertEquals(savedUser.getEmail(), retrievedUser.email());
        assertFalse(retrievedUser.username().isEmpty());
    }

    @Test
    public void shouldCreateUserThroughService() throws SQLException {

        UserDTO newUser = createUserDTO(null, "New User", "username", 123456789, "newuser@example.com", "password");
        UserDTO createdUser = userService.createUser(newUser);
        User userInRepository = userRepository.findById(createdUser.id()).orElseThrow();

        assertEquals(newUser.username(), userInRepository.getUsername());
        assertEquals(newUser.fullname(), userInRepository.getFullname());
        assertEquals(newUser.email(), userInRepository.getEmail());
        assertEquals(newUser.phone(), userInRepository.getPhone());
        assertEquals(newUser.password(), userInRepository.getEncodedPassword());
        assertEquals(0, userInRepository.getNumTicketsBought());
        assertEquals("None", userInRepository.getFavoriteGenre());
        assertFalse(userInRepository.getProfileImage());
        assertTrue(userInRepository.getRoles().contains("USER"));
    }

    @Test
    public void shouldReplaceUserThroughService() throws SQLException {

        User savedUser = createAndSaveUser("replace_user", "Original User", 987654321, "password", "original@example.com");
        UserDTO updateUser = createUserDTO(savedUser.getId(), "Updated User", "ignoredUsername", 123456789, "updated@example.com", "ignoredPassword");

        userService.replaceUser(savedUser.getId(), updateUser);
        User userInRepository = userRepository.findById(savedUser.getId()).orElseThrow();

        assertEquals("Updated User", userInRepository.getFullname());
        assertEquals("updated@example.com", userInRepository.getEmail());
        assertEquals(123456789, userInRepository.getPhone());
        assertEquals(savedUser.getUsername(), userInRepository.getUsername());
        assertEquals(savedUser.getEncodedPassword(), userInRepository.getEncodedPassword());
        assertNotEquals("ignoredUsername", userInRepository.getUsername());
    }

    @Test
    public void shouldReportUserExistsThroughService() {
        User savedUser = createAndSaveUser("existing_user", "Existing User", 123456789, "password", "exists@example.com");

        assertTrue(userService.userExists(savedUser.getUsername()));
        assertFalse(userService.userExists("missing_user"));
    }

    @Test
    public void shouldThrowNotFoundWhenUserDoesNotExistThroughService() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.findById(999L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

}
