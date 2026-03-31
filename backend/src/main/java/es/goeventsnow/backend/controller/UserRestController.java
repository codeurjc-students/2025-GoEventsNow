package es.goeventsnow.backend.controller;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
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
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.goeventsnow.backend.dto.event.EventDTO;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import es.goeventsnow.backend.dto.user.UserDTO;
import es.goeventsnow.backend.service.UserService;

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
    public UserDTO replaceUser(@PathVariable long id, @RequestBody UserDTO userDTO) throws SQLException {
        return userService.replaceUser(id, userDTO);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> getProfilePhoto(@PathVariable long id) throws IOException, SQLException {

        Resource profilePhoto = userService.getProfilePhoto(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE,"image/jpeg").body(profilePhoto);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Object> createUserImage(@PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException {

        URI location = fromCurrentRequest().build().toUri();
        userService.createProfilePhoto(id, imageFile.getInputStream(), imageFile.getSize());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<Object> replaceUserImage(@PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException {

        userService.replaceProfilePhoto(id, imageFile.getInputStream(), imageFile.getSize());
        return ResponseEntity.noContent().build();
    }

}
