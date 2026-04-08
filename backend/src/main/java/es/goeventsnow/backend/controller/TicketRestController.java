
package es.goeventsnow.backend.controller;

import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.goeventsnow.backend.dto.ticket.TicketDTO;
import es.goeventsnow.backend.dto.ticket.TicketMapperImpl;
import es.goeventsnow.backend.service.TicketService;
import es.goeventsnow.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketRestController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public Collection<TicketDTO> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public TicketDTO getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

    @PostMapping("/")
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDTO, HttpServletRequest request)  throws SQLException {

        

        Principal principal = request.getUserPrincipal();

        if (principal != null) {

            TicketDTO createdTicketDTO = new TicketDTO(null, ticketDTO.ticketType(), ticketDTO.price(), ticketDTO.numTickets(), ticketDTO.eventId(), userService.findByUsername(principal.getName()).id());
            TicketDTO savedTicketDTO =  ticketService.addTicket(createdTicketDTO);
            
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTicketDTO.id())
                .toUri();

        return ResponseEntity.created(location).body(savedTicketDTO);
        }else {
			throw new NoSuchElementException();
		}

    }

}