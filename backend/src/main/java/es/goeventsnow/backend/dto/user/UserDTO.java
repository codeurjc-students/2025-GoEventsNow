package es.goeventsnow.backend.dto.user;

import java.util.List;

import es.goeventsnow.backend.dto.participant.ParticipantDTO;
import es.goeventsnow.backend.dto.ticket.TicketDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDTO(
    Long id,
    @NotBlank
    String fullname,
    String username,
    @NotNull
    Integer phone,
    @NotBlank
    String email,
    String password,
    Integer numTicketsBought,
    String favoriteGenre,
    Boolean profileImage,
    List<TicketDTO> tickets,
    List<String> roles,
    List<ParticipantDTO> followedParticipants
) {}
