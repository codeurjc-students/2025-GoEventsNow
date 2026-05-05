package es.goeventsnow.backend.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.annotation.Validated;

import es.goeventsnow.backend.security.jwt.UserLoginService;
import es.goeventsnow.backend.security.jwt.AuthResponse;
import es.goeventsnow.backend.security.jwt.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@Validated
@RequestMapping("/api/v1/auth")
public class LoginController {

    @Autowired
    private UserLoginService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {

        return userService.login(response, loginRequest);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue(name = "RefreshToken", required = false) String refreshToken, HttpServletResponse response) {

        return userService.refresh(response, refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logOut(HttpServletResponse response) {
        return ResponseEntity.ok(new AuthResponse(AuthResponse.Status.SUCCESS, userService.logout(response)));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestParam("username") @NotBlank String username,
            @RequestParam("fullname") @NotBlank String fullname,
            @RequestParam("email") @NotBlank String email,
            @RequestParam("password") @NotBlank String password,
            @RequestParam("phone") @NotBlank String phone,
            @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile,
            HttpServletResponse response) {
    
        return userService.register(response, username, fullname, email, password, phone, profileImageFile);
    }

}
