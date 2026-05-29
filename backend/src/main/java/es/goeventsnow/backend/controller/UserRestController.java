package es.goeventsnow.backend.controller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import es.goeventsnow.backend.dto.participant.ParticipantDTO;
import es.goeventsnow.backend.dto.user.UserDTO;
import es.goeventsnow.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

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

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable long id) {
        UserDTO userDTO = userService.findById(id);
        if (userDTO != null) {
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> replaceUser(@PathVariable long id, @Valid @RequestBody UserDTO userDTO,
            HttpServletRequest request) throws SQLException {
        validateAuthenticatedUser(id, request);
        return ResponseEntity.ok(userService.replaceUser(id, userDTO));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> getProfilePhoto(@PathVariable long id)
            throws IOException, SQLException {
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

    @PostMapping("/{id}/following/{participantId}")
    public ResponseEntity<UserDTO> followParticipant(@PathVariable long id, @PathVariable long participantId, HttpServletRequest request) throws SQLException {
        validateAuthenticatedUser(id, request);
        UserDTO authenticatedUser = userService.getAuthenticatedUser(request);
        UserDTO updatedUser = userService.followParticipant(authenticatedUser.id(), participantId);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}/following/{participantId}")
    public ResponseEntity<UserDTO> unfollowParticipant(@PathVariable long id, @PathVariable long participantId, HttpServletRequest request) throws SQLException {
        validateAuthenticatedUser(id, request);
        UserDTO authenticatedUser = userService.getAuthenticatedUser(request);
        UserDTO updatedUser = userService.unfollowParticipant(authenticatedUser.id(), participantId);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<Page<ParticipantDTO>> getFollowedParticipants(@PathVariable long id, Pageable pageable, HttpServletRequest request) throws SQLException {
        validateAuthenticatedUser(id, request);
        Page<ParticipantDTO> followedParticipants = userService.getFollowedParticipants(id, pageable);
        return ResponseEntity.ok(followedParticipants);
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
