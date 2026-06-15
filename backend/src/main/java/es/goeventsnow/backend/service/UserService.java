package es.goeventsnow.backend.service;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import es.goeventsnow.backend.dto.event.EventDTO;
import es.goeventsnow.backend.dto.event.EventMapper;
import es.goeventsnow.backend.dto.participant.ParticipantDTO;
import es.goeventsnow.backend.dto.participant.ParticipantMapper;
import es.goeventsnow.backend.dto.review.ReviewDTO;
import es.goeventsnow.backend.dto.ticket.TicketDTO;
import es.goeventsnow.backend.dto.user.NewUserDTO;
import es.goeventsnow.backend.dto.user.UserDTO;
import es.goeventsnow.backend.dto.user.UserMapper;
import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.model.Participant;
import es.goeventsnow.backend.model.User;
import es.goeventsnow.backend.repository.EventRepository;
import es.goeventsnow.backend.repository.ParticipantRepository;
import es.goeventsnow.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {

    private static final String NOT_FOUND_IMAGE = "User profile image not found";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private ParticipantMapper participantMapper;

    public Collection<UserDTO> getAllUsers() {
        return toDTOs(userRepository.findAll());
    }

    public UserDTO findByUsername(String username) {
        return toDTO(getUserByUsername(username));
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public UserDTO findById(Long id) {
        return toDTO(getUser(id));
    }

    public UserDTO createUser(UserDTO userDTO) throws SQLException {

        if (userDTO.id() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID must be null for new user creation");
        }

        if (userExists(userDTO.username())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        User user = toDomain(userDTO);

        userRepository.save(user);

        return toDTO(user);
    }

    public UserDTO replaceUser(long id, UserDTO updateUserDTO) throws SQLException {

        User existingUser = getUser(id);

        existingUser.setFullname(updateUserDTO.fullname());
        existingUser.setPhone(updateUserDTO.phone());
        existingUser.setEmail(updateUserDTO.email());

        userRepository.save(existingUser);
        return toDTO(existingUser);

    }

    public UserDTO createOrReplaceUser(Long id, UserDTO userDTO) throws SQLException {
        return id == null ? createUser(userDTO) : replaceUser(id, userDTO);
    }

    public UserDTO UserCreationReplacement(Long userId, NewUserDTO newUserDTO, Boolean removeImage,
            PasswordEncoder passwordEncoder) throws IOException, SQLException {
        UserDTO userDTO = buildReplacementUserDTO(userId, newUserDTO, removeImage, passwordEncoder);

        UserDTO newUser = createOrReplaceUser(userId, userDTO);

        MultipartFile imageField = newUserDTO.profileImageFile();
        if (imageField != null && !imageField.isEmpty()) {
            createProfilePhoto(newUser.id(), imageField.getInputStream(), imageField.getSize());
        }

        return newUser;
    }

    public void createProfilePhoto(long id, InputStream inputStream, long size) {
        updateProfilePhoto(getUser(id), inputStream, size);
    }

    public Resource getProfilePhoto(long id) throws SQLException {
        User user = getUser(id);
        ensureImageExists(user.getProfileImageFile(), NOT_FOUND_IMAGE);
        return new InputStreamResource(user.getProfileImageFile().getBinaryStream());
    }

    public void replaceProfilePhoto(long id, InputStream inputStream, long size) {
        User user = getUser(id);
        ensureImageExists(user.getProfileImageFile(), NOT_FOUND_IMAGE);
        updateProfilePhoto(user, inputStream, size);
    }

    public void deleteProfilePhoto(long id) {
        User user = getUser(id);
        ensureImageExists(user.getProfileImageFile(), NOT_FOUND_IMAGE);
        user.setProfileImage(false);
        user.setProfileImageFile(null);
        userRepository.save(user);
    }

    public UserDTO getAuthenticatedUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            return findByUsername(principal.getName());
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user not found");
        }
    }

    public UserDTO addFavoriteEvent(long userId, long eventId) throws SQLException {
        User user = getUser(userId);
        Event event = getEvent(eventId);
        if (user.getFavoriteEvents().contains(event)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event is already in favorites");
        }
        user.getFavoriteEvents().add(event);
        return toDTO(userRepository.save(user));
    }

    public UserDTO removeFavoriteEvent(long userId, long eventId) throws SQLException {
        User user = getUser(userId);
        Event event = getEvent(eventId);
        if (!user.getFavoriteEvents().contains(event)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event is not in favorites");
        }
        user.getFavoriteEvents().remove(event);
        return toDTO(userRepository.save(user));
    }

    public Page<EventDTO> getFavoriteEvents(long userId, Pageable pageable) throws SQLException {
        User user = getUser(userId);
        List<Event> favoriteEvents = user.getFavoriteEvents();

        if (favoriteEvents == null || favoriteEvents.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Long> ids = favoriteEvents.stream().map(Event::getId).toList();
        return eventRepository.findByIdIn(ids, pageable).map(eventMapper::toDTO);
    }

    public UserDTO followParticipant(long userId, long participantId) throws SQLException {
        User user = getUser(userId);
        Participant participant = getParticipant(participantId);
        if (user.getFollowedParticipants().contains(participant)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Participant is already being followed");
        }
        user.getFollowedParticipants().add(participant);
        updateParticipantFollowers(participant, 1);
        return toDTO(userRepository.save(user));
    }

    public UserDTO unfollowParticipant(long userId, long participantId) throws SQLException {
        User user = getUser(userId);
        Participant participant = getParticipant(participantId);
        if (!user.getFollowedParticipants().contains(participant)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Participant is not being followed");
        }
        user.getFollowedParticipants().remove(participant);
        updateParticipantFollowers(participant, -1);
        return toDTO(userRepository.save(user));
    }

    public Page<ParticipantDTO> getFollowedParticipants(long userId, Pageable pageable) throws SQLException {
        User user = getUser(userId);
        List<Participant> followedParticipants = user.getFollowedParticipants();

        if (followedParticipants == null || followedParticipants.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Long> ids = followedParticipants.stream().map(Participant::getId).toList();
        return participantRepository.findByIdIn(ids, pageable).map(participantMapper::toDTO);
    }

    private UserDTO toDTO(User user) {
        return userMapper.toDTO(user);
    }

    private User getUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private Event getEvent(long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    private Participant getParticipant (long id) {
        return participantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found"));
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private void updateParticipantFollowers(Participant participant, int delta) {
        participant.setNumFollowers(participant.getNumFollowers() + delta);
        participantRepository.save(participant);
    }

    private UserDTO buildReplacementUserDTO(Long userId, NewUserDTO newUserDTO, Boolean removeImage,
            PasswordEncoder passwordEncoder) {
        boolean image = false;
        String userName = newUserDTO.username();
        String password = encodedPassword(newUserDTO, passwordEncoder);
        Integer numTicketsBought = 0;
        List<TicketDTO> tickets = null;
        List<ReviewDTO> reviews = null;
        List<EventDTO> favoriteEvents = null;
        List<ParticipantDTO> followedParticipants = null;
        String favoriteGenre = "None";
        List<String> roles = List.of("USER");

        if (userId != null) {
            UserDTO oldUser = findById(userId);
            image = !Boolean.TRUE.equals(removeImage) && Boolean.TRUE.equals(oldUser.profileImage());
            userName = oldUser.username();
            numTicketsBought = oldUser.tickets() != null
                    ? oldUser.tickets().stream().mapToInt(TicketDTO::numTickets).sum()
                    : 0;
            password = oldUser.password();
            favoriteGenre = oldUser.favoriteGenre();
            tickets = oldUser.tickets();
            reviews = oldUser.reviews();
            favoriteEvents = oldUser.favoriteEvents();
            followedParticipants = oldUser.followedParticipants();
            roles = oldUser.roles();
        }

        return new UserDTO(userId, newUserDTO.fullname(), userName, newUserDTO.phone(),
                newUserDTO.email(), password, numTicketsBought, favoriteGenre, image, tickets, roles, reviews,
                favoriteEvents, followedParticipants);
    }

    private String encodedPassword(NewUserDTO newUserDTO, PasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(newUserDTO.password());
    }

    private void updateProfilePhoto(User user, InputStream inputStream, long size) {
        user.setProfileImage(true);
        user.setProfileImageFile(BlobProxy.generateProxy(inputStream, size));
        userRepository.save(user);
    }

    private void ensureImageExists(Blob imageFile, String notFoundMessage) {
        if (imageFile == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, notFoundMessage);
        }
    }

    private Collection<UserDTO> toDTOs(Collection<User> users) {
        return userMapper.toDTOs(users);
    }

    private User toDomain(UserDTO userDTO) {
        return userMapper.toDomain(userDTO);
    }

}
