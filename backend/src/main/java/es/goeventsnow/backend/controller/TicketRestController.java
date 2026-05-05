
package es.goeventsnow.backend.controller;

import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.goeventsnow.backend.dto.ticket.TicketDTO;
import es.goeventsnow.backend.service.TicketService;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/v1/tickets")
public class TicketRestController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/")
    public ResponseEntity<Page<TicketDTO>> getAllTickets(Principal principal, Pageable pageable) {
        if (principal != null) {
            return ResponseEntity.ok(ticketService.getTicketsByUsername(principal.getName(), pageable));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id, Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(ticketService.getTicketById(id, principal.getName()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<TicketDTO> createTicket(@Valid @RequestBody TicketDTO ticketDTO, Principal principal)  throws SQLException {

        if (principal != null) {

            TicketDTO savedTicketDTO =  ticketService.addTicket(ticketDTO, principal.getName());
            
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTicketDTO.id())
                .toUri();

        return ResponseEntity.created(location).body(savedTicketDTO);
        }else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

    }

}