package es.goeventsnow.backend.integration;

import java.sql.SQLException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import es.goeventsnow.backend.dto.event.EventDTO;
import es.goeventsnow.backend.dto.participant.ParticipantDTO;
import es.goeventsnow.backend.dto.user.UserDTO;
import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.model.Participant;
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

    @Test
    public void testFollowAndUnfollowParticipant() throws SQLException {
        User user = createAndSaveUser("follower", "Follower User", 123456789, "password", "follower@example.com");
        Participant savedParticipant = createAndSaveParticipant("Original Participant", "Music", "Original biography");

        UserDTO followedUser = userService.followParticipant(user.getId(), savedParticipant.getId());
        assertTrue(followedUser.followedParticipants().stream().anyMatch(participantDTO -> participantDTO.id().equals(savedParticipant.getId())));

        UserDTO unfollowedUser = userService.unfollowParticipant(user.getId(), savedParticipant.getId());
        assertFalse(unfollowedUser.followedParticipants().stream().anyMatch(participantDTO -> participantDTO.id().equals(savedParticipant.getId())));
    }

    @Test
    public void getFollowedParticipants() throws SQLException {
        User user = createAndSaveUser("follower", "Follower User", 123456789, "password", "follower@example.com");
        Participant savedParticipant = createAndSaveParticipant("Original Participant", "Music", "Original biography");

        UserDTO followedUser = userService.followParticipant(user.getId(), savedParticipant.getId());
        assertTrue(followedUser.followedParticipants().stream().anyMatch(participantDTO -> participantDTO.id().equals(savedParticipant.getId())));

        Page<ParticipantDTO> followedParticipants = userService.getFollowedParticipants(user.getId(), Pageable.unpaged());
        assertTrue(followedParticipants.getContent().stream().anyMatch(participantDTO -> participantDTO.id().equals(savedParticipant.getId())));
    }

    @Test
    public void addFavoriteEvent() throws SQLException {
        User user = createAndSaveUser("favorite_user", "Favorite User", 123456789, "password", "favorite@example.com");
        Event savedEvent = createAndSaveEvent("Favorite Event", "Favorite description", "Music", "Madrid",
                "2025-10-01", "20:00", 50.0, 150.0, 100, 20);

        UserDTO updatedUser = userService.addFavoriteEvent(user.getId(), savedEvent.getId());
        User userInRepository = userRepository.findById(user.getId()).orElseThrow();

        assertTrue(updatedUser.favoriteEvents().stream().anyMatch(eventDTO -> eventDTO.id().equals(savedEvent.getId())));
        assertTrue(userInRepository.getFavoriteEvents().stream().anyMatch(event -> event.getId().equals(savedEvent.getId())));
    }

    @Test
    public void removeFavoriteEvent() throws SQLException {
        User user = createAndSaveUser("favorite_user_remove", "Favorite User", 123456789, "password", "favorite-remove@example.com");
        Event savedEvent = createAndSaveEvent("Favorite Event Remove", "Favorite description", "Music", "Madrid",
                "2025-10-01", "20:00", 50.0, 150.0, 100, 20);

        userService.addFavoriteEvent(user.getId(), savedEvent.getId());
        UserDTO updatedUser = userService.removeFavoriteEvent(user.getId(), savedEvent.getId());
        User userInRepository = userRepository.findById(user.getId()).orElseThrow();

        assertFalse(updatedUser.favoriteEvents().stream().anyMatch(eventDTO -> eventDTO.id().equals(savedEvent.getId())));
        assertFalse(userInRepository.getFavoriteEvents().stream().anyMatch(event -> event.getId().equals(savedEvent.getId())));
    }

    @Test
    public void getFavoriteEvents() throws SQLException {
        User user = createAndSaveUser("favorite_user_get", "Favorite User", 123456789, "password", "favorite-get@example.com");
        Event savedEvent = createAndSaveEvent("Favorite Event Get", "Favorite description", "Music", "Madrid",
                "2025-10-01", "20:00", 50.0, 150.0, 100, 20);

        userService.addFavoriteEvent(user.getId(), savedEvent.getId());

        Page<EventDTO> favoriteEvents = userService.getFavoriteEvents(user.getId(), Pageable.unpaged());

        assertTrue(favoriteEvents.getContent().stream().anyMatch(eventDTO -> eventDTO.id().equals(savedEvent.getId())));
    }


}
