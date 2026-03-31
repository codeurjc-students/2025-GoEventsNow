package es.goeventsnow.backend.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.goeventsnow.backend.dto.ticket.TicketDTO;
import es.goeventsnow.backend.dto.ticket.TicketMapper;
import es.goeventsnow.backend.model.Ticket;
import es.goeventsnow.backend.model.User;
import es.goeventsnow.backend.repository.TicketRepository;
import es.goeventsnow.backend.repository.UserRepository;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketMapper ticketMapper;

    public Collection<TicketDTO> getAllTickets(){
       return toDTOs(ticketRepository.findAll());
    }

    public TicketDTO getTicketById(Long id){
        return toDTO(ticketRepository.findById(id).orElseThrow());
    }

    public TicketDTO addTicket(TicketDTO ticketDTO){
        Ticket ticketSaved = toDomain(ticketDTO);
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
