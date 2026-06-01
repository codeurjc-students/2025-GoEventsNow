package es.goeventsnow.backend.dto.event;

import java.util.List;
import es.goeventsnow.backend.dto.participant.ParticipantDTO;
import es.goeventsnow.backend.dto.review.ReviewDTO;
import es.goeventsnow.backend.dto.ticket.TicketDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EventDTO (
    Long id,
    @NotBlank
    String title,
    @NotBlank
    String description,
    @NotBlank
    String category,
    @NotBlank
    String location,
    @NotBlank
    String date,
    @NotBlank
    String time,
    @NotNull
    Double basicPrice,
    @NotNull
    Double vipPrice,
    @NotNull
    Integer availableBasicTickets,
    @NotNull
    Integer availableVipTickets,
    Boolean image,
    @NotNull
    @Size(min=1, message="At least one participant is required")
    List<ParticipantDTO> participants,
    List<TicketDTO> tickets,
    List<ReviewDTO> reviews
) {}