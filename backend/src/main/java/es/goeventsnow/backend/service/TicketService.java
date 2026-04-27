package es.goeventsnow.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import es.goeventsnow.backend.dto.ticket.TicketDTO;
import es.goeventsnow.backend.dto.ticket.TicketMapper;
import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.model.Ticket;
import es.goeventsnow.backend.model.User;
import es.goeventsnow.backend.repository.EventRepository;
import es.goeventsnow.backend.repository.TicketRepository;
import es.goeventsnow.backend.repository.UserRepository;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketMapper ticketMapper;

    public Page<TicketDTO> getTicketsByUsername(String username, Pageable pageable) {
        userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return ticketRepository.findByUserOwnerUsername(username, pageable).map(this::toDTO);
    }

    public TicketDTO getTicketById(Long id, String username) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!ticket.getUserOwner().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ticket does not belong to the authenticated user");
        }
        return toDTO(ticket);
    }

    public TicketDTO addTicket(TicketDTO ticketDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Event event = eventRepository.findById(ticketDTO.eventId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        int requestedAmount = ticketDTO.numTickets();

        if (ticketDTO.ticketType().equalsIgnoreCase("VIP")) {
            if (event.getAvailableVipTickets() < requestedAmount) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Not enough VIP tickets available.");
            }
            event.setAvailableVipTickets(event.getAvailableVipTickets() - requestedAmount);

        } else if (ticketDTO.ticketType().equalsIgnoreCase("BASIC")) {
            if (event.getAvailableBasicTickets() < requestedAmount) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Not enough BASIC tickets available.");
            }
            event.setAvailableBasicTickets(event.getAvailableBasicTickets() - requestedAmount);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ticket type must be either VIP or BASIC.");
        }

        eventRepository.save(event);

        TicketDTO savedTicketDTO = new TicketDTO(null, ticketDTO.ticketType(), ticketDTO.price(),
                ticketDTO.numTickets(), event.getId(), user.getId());
        Ticket ticketSaved = toDomain(savedTicketDTO);
        return toDTO(ticketRepository.save(ticketSaved));
    }

    private TicketDTO toDTO(Ticket ticket) {
        return ticketMapper.toDTO(ticket);
    }

    private Ticket toDomain(TicketDTO ticketDTO) {
        return ticketMapper.toDomain(ticketDTO);
    }

}
