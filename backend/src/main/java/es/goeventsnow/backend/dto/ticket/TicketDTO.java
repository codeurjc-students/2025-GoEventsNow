package es.goeventsnow.backend.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketDTO(
    Long id,
    @NotBlank
    String ticketType,
    @NotNull
    Double price,
    @NotNull
    Integer numTickets,
    @NotNull
    Long eventId ,
    Long userOwnerId
) {}
