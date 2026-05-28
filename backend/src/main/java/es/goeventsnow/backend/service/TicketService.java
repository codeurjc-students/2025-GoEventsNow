package es.goeventsnow.backend.service;

import java.util.List;

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
        getUser(username);
        return ticketRepository.findByUserOwnerUsername(username, pageable).map(this::toDTO);
    }

    public TicketDTO getTicketById(Long id, String username) {
        Ticket ticket = getTicket(id);
        User user = getUser(username);

        if (!ticket.getUserOwner().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ticket does not belong to the authenticated user");
        }
        return toDTO(ticket);
    }

    public TicketDTO addTicket(TicketDTO ticketDTO, String username) {
        User user = getUser(username);
        Event event = getEvent(ticketDTO.eventId());

        int requestedAmount = ticketDTO.numTickets();

        reduceAvailableTickets(event, ticketDTO.ticketType(), requestedAmount);

        eventRepository.save(event);

        TicketDTO savedTicketDTO = new TicketDTO(null, ticketDTO.ticketType(), ticketDTO.price(),
                ticketDTO.numTickets(), event.getId(), user.getId());
        Ticket ticketSaved = toDomain(savedTicketDTO);
        return toDTO(ticketRepository.save(ticketSaved));
    }

    public List<Object[]> getTicketsSoldByEvent() {
        return ticketRepository.findTicketsSoldByEvent();
    }

    public List<Object[]> getTicketsSoldByCategory() {
        return ticketRepository.findTicketsSoldByCategory();
    }

    private TicketDTO toDTO(Ticket ticket) {
        return ticketMapper.toDTO(ticket);
    }

    private Ticket toDomain(TicketDTO ticketDTO) {
        return ticketMapper.toDomain(ticketDTO);
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    private Ticket getTicket(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));
    }

    private void reduceAvailableTickets(Event event, String ticketType, int requestedAmount) {
        if (ticketType.equalsIgnoreCase("VIP")) {
            if (event.getAvailableVipTickets() < requestedAmount) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Not enough VIP tickets available.");
            }
            event.setAvailableVipTickets(event.getAvailableVipTickets() - requestedAmount);
            return;
        }

        if (ticketType.equalsIgnoreCase("BASIC")) {
            if (event.getAvailableBasicTickets() < requestedAmount) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Not enough BASIC tickets available.");
            }
            event.setAvailableBasicTickets(event.getAvailableBasicTickets() - requestedAmount);
            return;
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Ticket type must be either VIP or BASIC.");
    }

}
