package es.goeventsnow.backend.dto.event;

import java.util.List;
import es.goeventsnow.backend.dto.participant.ParticipantDTO;
import es.goeventsnow.backend.dto.ticket.TicketDTO;

public record EventDTO (
    Long id,
    String title,
    String category,
    String location,
    String date,
    Boolean image,
    List<ParticipantDTO> participants,
    List<TicketDTO> tickets
) {}