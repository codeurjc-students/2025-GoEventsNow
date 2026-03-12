package es.goeventsnow.backend.dto.ticket;

public record TicketDTO(
    Long id,
    String ticketType,
    Double price,
    Integer numTickets,
    Long eventId 
) {}
