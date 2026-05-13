package es.goeventsnow.backend.dto.user;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewUserDTO (
 @NotBlank String fullname,
 @NotBlank String username,
 @NotNull Integer phone,
 @NotBlank String email,
 @NotBlank String password,
 MultipartFile profileImageFile

){}