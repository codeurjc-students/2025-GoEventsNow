package es.goeventsnow.backend.dto.user;

import java.util.List;

public record UserDTO(
    Long id,
    String fullname,
    String username,
    Integer phone,
    String email,
    String password,
    Integer numTicketsBought,
    String favoriteGenre,
    Boolean profileImage,
    List<String> roles
) {}
