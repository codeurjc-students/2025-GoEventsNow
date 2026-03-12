package es.goeventsnow.backend.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.goeventsnow.backend.dto.ticket.TicketDTO;
import es.goeventsnow.backend.dto.user.NewUserDTO;
import es.goeventsnow.backend.dto.user.UserDTO;
import es.goeventsnow.backend.dto.user.UserMapper;
import es.goeventsnow.backend.model.User;
import es.goeventsnow.backend.repository.UserRepository;

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
        return toDTO(userRepository.findByUsername(username).orElse(null));
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public UserDTO findById(Long id) {
        return toDTO(userRepository.findById(id).orElse(null));
    }

    public UserDTO createUser(UserDTO userDTO) throws SQLException {

		if (userDTO == null ||userDTO.id() != null) {
			throw new IllegalArgumentException();
		}

		User user = toDomain(userDTO);

		userRepository.save(user);

		return toDTO(user);
	}

	public UserDTO replaceUser(long id, UserDTO updateUserDTO) throws SQLException {

		if (userRepository.existsById(id)) {
			User updatedUser = toDomain(updateUserDTO);
			User existingUser = userRepository.findById(id).orElseThrow();
			updatedUser.setProfileImageFile(existingUser.getProfileImageFile());
			updatedUser.setProfileImage(existingUser.getProfileImage());
			updatedUser.setId(id);
			userRepository.save(updatedUser);
			return toDTO(updatedUser);
		} else {
			throw new NoSuchElementException();
		}

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

    public UserDTO UserCreationReplacement(Long userId, NewUserDTO newUserDTO, Boolean removeImage, PasswordEncoder passwordEncoder) throws IOException, SQLException {
		boolean image = false;
		String userName = newUserDTO.username();
		String password = null;
		Integer numTicketsBought = 0;
		String favoriteGenre = "None";
		List<String> roles = List.of("USER");

		if (newUserDTO.password() != null && !newUserDTO.password().isEmpty()) {
			password = passwordEncoder.encode(newUserDTO.password());
		}

		if (userId != null) {
			UserDTO oldUser = findById(userId);
			image = removeImage != null && removeImage ? false : oldUser.profileImage();
			userName = oldUser.username();
			password = oldUser.password();
			favoriteGenre = oldUser.favoriteGenre();
			roles = oldUser.roles();
		}

		UserDTO userDTO = new UserDTO(userId, newUserDTO.fullname(), userName, newUserDTO.phone(),
				newUserDTO.email(), password, numTicketsBought, favoriteGenre, image, roles);

		UserDTO newUser = createOrReplaceUser(userId, userDTO);

		MultipartFile imageField = newUserDTO.profileImageFile();
		if (imageField != null && !imageField.isEmpty()) {
			createProfilePhoto(newUser.id(), imageField.getInputStream(), imageField.getSize());
		}

		return newUser;
	}

    
    public void createProfilePhoto(long id, InputStream inputStream, long size) {
        User user = userRepository.findById(id).orElseThrow();
        user.setProfileImage(true);
        user.setProfileImageFile(BlobProxy.generateProxy(inputStream, size));
        userRepository.save(user);
    }

    public Resource getProfilePhoto(long id) throws SQLException {
        User user = userRepository.findById(id).orElseThrow();

        if (user.getProfileImageFile() == null) {
            throw new NoSuchElementException();
        } else {
            return new InputStreamResource(user.getProfileImageFile().getBinaryStream());
        }
    }

    public void replaceProfilePhoto(long id, InputStream inputStream, long size) {
        User user = userRepository.findById(id).orElseThrow();

        if (user.getProfileImageFile() == null) {
            throw new NoSuchElementException();
        }

        user.setProfileImage(true);
        user.setProfileImageFile(BlobProxy.generateProxy(inputStream, size));
        userRepository.save(user);
    }

    public void deleteProfilePhoto(long id) {
        User user = userRepository.findById(id).orElseThrow();

        if (user.getProfileImageFile() == null) {
            throw new NoSuchElementException();
        }

        user.setProfileImage(false);
        user.setProfileImageFile(null);
        userRepository.save(user);
    }

    private UserDTO toDTO (User user) {
        return userMapper.toDTO(user);
    }

    private Collection<UserDTO> toDTOs (Collection<User> users) {
        return userMapper.toDTOs(users);
    }

    private User toDomain (UserDTO userDTO) {
        return userMapper.toDomain(userDTO);
    }

}
