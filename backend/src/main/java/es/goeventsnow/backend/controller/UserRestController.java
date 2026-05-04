package es.goeventsnow.backend.controller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.goeventsnow.backend.dto.user.UserDTO;
import es.goeventsnow.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        if (principal != null) {
            UserDTO userDTO = userService.findByUsername(principal.getName());
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> replaceUser(@PathVariable long id, @RequestBody UserDTO userDTO,
            HttpServletRequest request) throws SQLException {
        validateAuthenticatedUser(id, request);
        return ResponseEntity.ok(userService.replaceUser(id, userDTO));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> getProfilePhoto(@PathVariable long id, HttpServletRequest request)
            throws IOException, SQLException {
        validateAuthenticatedUser(id, request);
        Resource profilePhoto = userService.getProfilePhoto(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").body(profilePhoto);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Object> createUserImage(@PathVariable long id, @RequestParam MultipartFile imageFile,
            HttpServletRequest request) throws IOException {
        validateAuthenticatedUser(id, request);
        URI location = fromCurrentRequest().build().toUri();
        userService.createProfilePhoto(id, imageFile.getInputStream(), imageFile.getSize());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<Object> replaceUserImage(@PathVariable long id, @RequestParam MultipartFile imageFile,
            HttpServletRequest request) throws IOException {
        validateAuthenticatedUser(id, request);
        userService.replaceProfilePhoto(id, imageFile.getInputStream(), imageFile.getSize());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteUserImage(@PathVariable long id, HttpServletRequest request)
            throws IOException {
        validateAuthenticatedUser(id, request);
        userService.deleteProfilePhoto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> exists(@RequestParam String username) {
        return ResponseEntity.ok(userService.userExists(username));
    }

    private void validateAuthenticatedUser(Long id, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();

        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You need to log in first.");
        }

        UserDTO authenticatedUser = userService.getAuthenticatedUser(request);
        if (!authenticatedUser.id().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to access this resource.");
        }
    }
}
