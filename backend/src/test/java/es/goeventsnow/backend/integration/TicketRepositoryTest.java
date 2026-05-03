package es.goeventsnow.backend.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import es.goeventsnow.backend.dto.ticket.TicketDTO;
import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.model.Ticket;
import es.goeventsnow.backend.model.User;
import es.goeventsnow.backend.service.TicketService;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class TicketRepositoryTest extends IntegrationTestBase {

    @Autowired
    private TicketService ticketService;

    @Test
    public void shouldGetTicketsByUsernameThroughService() {
        User savedUser = createAndSaveUser("ticket_user_list", "Ticket User", 123456789, "password", "ticket-list@example.com");
        Event savedEvent = createAndSaveEvent("Concert", "Live concert", "Music", "Venue", "2025-10-01", "20:00", 50.0, 150.0, 100, 20);
        Ticket firstTicket = createAndSaveTicket(savedEvent, savedUser, "VIP", 150.0, 2);
        Ticket secondTicket = createAndSaveTicket(savedEvent, savedUser, "BASIC", 50.0, 4);

        Page<TicketDTO> ticketsByUsername = ticketService.getTicketsByUsername(savedUser.getUsername(), PageRequest.of(0, 20));

        assertEquals(2, ticketsByUsername.getNumberOfElements());
        assertTrue(ticketsByUsername.getContent().stream().anyMatch(ticketDTO -> ticketDTO.id().equals(firstTicket.getId())));
        assertTrue(ticketsByUsername.getContent().stream().anyMatch(ticketDTO -> ticketDTO.id().equals(secondTicket.getId())));

    }

    @Test
    public void shouldThrowNotFoundWhenUsernameDoesNotExistThroughService() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ticketService.getTicketsByUsername("nonexistent_user", PageRequest.of(0, 20)));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void shouldGetTicketByIdThroughService() {
        User savedUser = createAndSaveUser("ticket_user_by_id", "Ticket User", 123456789, "password", "ticket-by-id@example.com");
        Event savedEvent = createAndSaveEvent("Concert", "Live concert", "Music", "Venue", "2025-10-01", "20:00", 50.0, 150.0, 100, 20);
        Ticket savedTicket = createAndSaveTicket(savedEvent, savedUser, "VIP", 150.0, 2);

        TicketDTO retrievedTicket = ticketService.getTicketById(savedTicket.getId(), savedUser.getUsername());

        assertEquals(savedTicket.getId(), retrievedTicket.id());
        assertEquals(savedTicket.getTicketType(), retrievedTicket.ticketType());
        assertEquals(savedTicket.getPrice(), retrievedTicket.price());
        assertEquals(savedTicket.getNumTickets(), retrievedTicket.numTickets());
        assertEquals(savedEvent.getId(), retrievedTicket.eventId());
        assertEquals(savedUser.getId(), retrievedTicket.userOwnerId());
    }

    @Test
    public void shouldThrowNotFoundWhenTicketDoesNotExistThroughService() {
        User savedUser = createAndSaveUser("ticket_user_missing_ticket", "Ticket User", 123456789, "password", "ticket-missing@example.com");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ticketService.getTicketById(999L, savedUser.getUsername()));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void shouldThrowForbiddenWhenTicketBelongsToAnotherUserThroughService() {
        User owner = createAndSaveUser("ticket_owner", "Ticket Owner", 123456789, "password", "ticket-owner@example.com");
        User otherUser = createAndSaveUser("ticket_other_user", "Other User", 987654321, "password", "ticket-other@example.com");
        Event savedEvent = createAndSaveEvent("Concert", "Live concert", "Music", "Venue", "2025-10-01", "20:00", 50.0, 150.0, 100, 20);
        Ticket savedTicket = createAndSaveTicket(savedEvent, owner, "BASIC", 50.0, 1);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ticketService.getTicketById(savedTicket.getId(), otherUser.getUsername()));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    public void shouldAddBasicTicketThroughService() {
        User savedUser = createAndSaveUser("ticket_user_add_basic", "Ticket User", 123456789, "password", "ticket-basic@example.com");
        Event savedEvent = createAndSaveEvent("Concert", "Live concert", "Music", "Venue", "2025-10-01", "20:00", 50.0, 150.0, 100, 20);
        TicketDTO newTicket = new TicketDTO(null, "BASIC", 50.0, 2, savedEvent.getId(), null);

        TicketDTO savedTicket = ticketService.addTicket(newTicket, savedUser.getUsername());
        Event updatedEvent = eventRepository.findById(savedEvent.getId()).orElseThrow();

        assertNotNull(savedTicket.id());
        assertEquals("BASIC", savedTicket.ticketType());
        assertEquals(50.0, savedTicket.price());
        assertEquals(2, savedTicket.numTickets());
        assertEquals(savedEvent.getId(), savedTicket.eventId());
        assertEquals(savedUser.getId(), savedTicket.userOwnerId());
        assertEquals(98, updatedEvent.getAvailableBasicTickets());
    }

    @Test
    public void shouldAddVipTicketThroughService() {
        User savedUser = createAndSaveUser("ticket_user_add_vip", "Ticket User", 123456789, "password", "ticket-vip@example.com");
        Event savedEvent = createAndSaveEvent("Concert", "Live concert", "Music", "Venue", "2025-10-01", "20:00", 50.0, 150.0, 100, 20);
        TicketDTO newTicket = new TicketDTO(null, "VIP", 150.0, 3, savedEvent.getId(), null);

        TicketDTO savedTicket = ticketService.addTicket(newTicket, savedUser.getUsername());
        Event updatedEvent = eventRepository.findById(savedEvent.getId()).orElseThrow();

        assertNotNull(savedTicket.id());
        assertEquals("VIP", savedTicket.ticketType());
        assertEquals(3, savedTicket.numTickets());
        assertEquals(17, updatedEvent.getAvailableVipTickets());
    }

    @Test
    public void shouldThrowBadRequestWhenTicketTypeIsInvalidThroughService() {
        User savedUser = createAndSaveUser("ticket_user_invalid_type", "Ticket User", 123456789, "password", "ticket-invalid@example.com");
        Event savedEvent = createAndSaveEvent("Concert", "Live concert", "Music", "Venue", "2025-10-01", "20:00", 50.0, 150.0, 100, 20);
        TicketDTO newTicket = new TicketDTO(null, "GOLD", 50.0, 2, savedEvent.getId(), null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ticketService.addTicket(newTicket, savedUser.getUsername()));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void shouldThrowBadRequestWhenThereAreNotEnoughTicketsThroughService() {
        User savedUser = createAndSaveUser("ticket_user_not_enough", "Ticket User", 123456789, "password", "ticket-not-enough@example.com");
        Event savedEvent = createAndSaveEvent("Concert", "Live concert", "Music", "Venue", "2025-10-01", "20:00", 50.0, 150.0, 1, 1);
        TicketDTO newTicket = new TicketDTO(null, "VIP", 150.0, 2, savedEvent.getId(), null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ticketService.addTicket(newTicket, savedUser.getUsername()));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void shouldThrowNotFoundWhenAddingTicketForMissingEventThroughService() {
        User savedUser = createAndSaveUser("ticket_user_missing_event", "Ticket User", 123456789, "password", "ticket-missing-event@example.com");
        TicketDTO newTicket = new TicketDTO(null, "BASIC", 50.0, 1, 999L, null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ticketService.addTicket(newTicket, savedUser.getUsername()));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

}
