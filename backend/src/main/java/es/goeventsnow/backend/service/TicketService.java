package es.goeventsnow.backend.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Collection<TicketDTO> getTicketsByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
       return toDTOs(user.getTickets());
    }

    public TicketDTO getTicketById(Long id, String username) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        User user = userRepository.findByUsername(username).orElseThrow();

        if (!ticket.getUserOwner().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Ticket does not belong to the specified user");
        }
        return toDTO(ticket);
    }

    public TicketDTO addTicket(TicketDTO ticketDTO, String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        Event event = eventRepository.findById(ticketDTO.eventId()).orElseThrow();

        int requestedAmount = ticketDTO.numTickets();

        if (ticketDTO.ticketType().equalsIgnoreCase("VIP")) {
            if (event.getAvailableVipTickets() < requestedAmount) {
                throw new IllegalStateException("Not enough VIP tickets available.");
            }
            event.setAvailableVipTickets(event.getAvailableVipTickets() - requestedAmount);
        } else if (ticketDTO.ticketType().equalsIgnoreCase("BASIC")) {
            if (event.getAvailableBasicTickets() < requestedAmount) {
                throw new IllegalStateException("Not enough basic tickets available.");
            }
            event.setAvailableBasicTickets(event.getAvailableBasicTickets() - requestedAmount);
        } else {
            throw new IllegalArgumentException("Invalid ticket type. Must be 'VIP' or 'BASIC'.");
        }

        eventRepository.save(event);

        TicketDTO savedTicketDTO = new TicketDTO(null, ticketDTO.ticketType(), ticketDTO.price(), ticketDTO.numTickets(), event.getId(), user.getId());
        Ticket ticketSaved = toDomain(savedTicketDTO);
        return toDTO(ticketRepository.save(ticketSaved));
    }


    private TicketDTO toDTO (Ticket ticket) {
        return ticketMapper.toDTO(ticket);
    }

    private Collection<TicketDTO> toDTOs (Collection<Ticket> tickets) {
        return ticketMapper.toDTOs(tickets);
    }

    private Ticket toDomain (TicketDTO ticketDTO) {
        return ticketMapper.toDomain(ticketDTO);
    }

    
}
