package es.goeventsnow.backend.dto.user;
import org.springframework.web.multipart.MultipartFile;

public record NewUserDTO (
 String fullname,
 String username,
 Integer phone,
String email,
 String password,
 MultipartFile profileImageFile

){}