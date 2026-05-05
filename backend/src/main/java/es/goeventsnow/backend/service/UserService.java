package es.goeventsnow.backend.service;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import es.goeventsnow.backend.dto.ticket.TicketDTO;
import es.goeventsnow.backend.dto.user.NewUserDTO;
import es.goeventsnow.backend.dto.user.UserDTO;
import es.goeventsnow.backend.dto.user.UserMapper;
import es.goeventsnow.backend.model.User;
import es.goeventsnow.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public Collection<UserDTO> getAllUsers() {
        return toDTOs(userRepository.findAll());
    }

    public UserDTO findByUsername(String username) {
        return toDTO(userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public UserDTO findById(Long id) {
        return toDTO(userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
    }

    public UserDTO createUser(UserDTO userDTO) throws SQLException {

        if (userDTO == null || userDTO.id() != null) {
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

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (updateUserDTO.fullname() != null) {
            existingUser.setFullname(updateUserDTO.fullname());
        }
        if (updateUserDTO.phone() != null) {
            existingUser.setPhone(updateUserDTO.phone());
        }
        if (updateUserDTO.email() != null) {
            existingUser.setEmail(updateUserDTO.email());
        }

        userRepository.save(existingUser);
        return toDTO(existingUser);

    }

    public UserDTO createOrReplaceUser(Long id, UserDTO userDTO) throws SQLException {

        UserDTO user;
        if (id == null) {
            user = createUser(userDTO);
        } else {
            user = replaceUser(id, userDTO);
        }
        return user;
    }

    public UserDTO UserCreationReplacement(Long userId, NewUserDTO newUserDTO, Boolean removeImage,
            PasswordEncoder passwordEncoder) throws IOException, SQLException {
        boolean image = false;
        String userName = newUserDTO.username();
        String password = null;
        Integer numTicketsBought = 0;
        List<TicketDTO> tickets = null;
        String favoriteGenre = "None";
        List<String> roles = List.of("USER");

        if (newUserDTO.password() != null && !newUserDTO.password().isEmpty()) {
            password = passwordEncoder.encode(newUserDTO.password());
        }

        if (userId != null) {
            UserDTO oldUser = findById(userId);
            image = Boolean.TRUE.equals(removeImage) ? false : Boolean.TRUE.equals(oldUser.profileImage());
            userName = oldUser.username();
            numTicketsBought = oldUser.tickets() != null
                    ? oldUser.tickets().stream().mapToInt(TicketDTO::numTickets).sum()
                    : 0;
            password = oldUser.password();
            favoriteGenre = oldUser.favoriteGenre();
            tickets = oldUser.tickets();
            roles = oldUser.roles();
        }

        UserDTO userDTO = new UserDTO(userId, newUserDTO.fullname(), userName, newUserDTO.phone(),
                newUserDTO.email(), password, numTicketsBought, favoriteGenre, image, tickets, roles);

        UserDTO newUser = createOrReplaceUser(userId, userDTO);

        MultipartFile imageField = newUserDTO.profileImageFile();
        if (imageField != null && !imageField.isEmpty()) {
            createProfilePhoto(newUser.id(), imageField.getInputStream(), imageField.getSize());
        }

        return newUser;
    }

    public void createProfilePhoto(long id, InputStream inputStream, long size) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setProfileImage(true);
        user.setProfileImageFile(BlobProxy.generateProxy(inputStream, size));
        userRepository.save(user);
    }

    public Resource getProfilePhoto(long id) throws SQLException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getProfileImageFile() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User profile image not found");
        } else {
            return new InputStreamResource(user.getProfileImageFile().getBinaryStream());
        }
    }

    public void replaceProfilePhoto(long id, InputStream inputStream, long size) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getProfileImageFile() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User profile image not found");
        }

        user.setProfileImage(true);
        user.setProfileImageFile(BlobProxy.generateProxy(inputStream, size));
        userRepository.save(user);
    }

    public void deleteProfilePhoto(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getProfileImageFile() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User profile image not found");
        }

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

    private UserDTO toDTO(User user) {
        return userMapper.toDTO(user);
    }

    private Collection<UserDTO> toDTOs(Collection<User> users) {
        return userMapper.toDTOs(users);
    }

    private User toDomain(UserDTO userDTO) {
        return userMapper.toDomain(userDTO);
    }

}
