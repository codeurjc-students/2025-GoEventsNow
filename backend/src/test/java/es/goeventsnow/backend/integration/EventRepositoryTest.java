package es.goeventsnow.backend.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import es.goeventsnow.backend.dto.event.EventDTO;
import es.goeventsnow.backend.dto.participant.ParticipantDTO;
import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.model.Participant;
import es.goeventsnow.backend.model.Ticket;
import es.goeventsnow.backend.model.User;
import es.goeventsnow.backend.service.EventService;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class EventRepositoryTest extends IntegrationTestBase {

    @Autowired
    private EventService eventService;

    @Test
    public void shouldReturnAllSavedEventsThroughService() {
        Event firstEvent = createAndSaveEvent("Database Testing Event 1", "Description 1", "Testing 1", "USA",
                "2025-10-05", "10:00", 20.0, 50.0, 100, 20, null);
        Event secondEvent = createAndSaveEvent("Database Testing Event 2", "Description 2", "Testing 2", "Madrid",
                "2025-08-16", "18:00", 30.0, 70.0, 200, 50, null);

        Page<EventDTO> events = eventService.getAllEvents(PageRequest.of(0, 20));

        assertTrue(events.getContent().stream().anyMatch(event -> event.title().equals(firstEvent.getTitle())));
        assertTrue(events.getContent().stream().anyMatch(event -> event.title().equals(secondEvent.getTitle())));
    }

    @Test
    public void shouldAddEventThroughService() {
        Participant participant = createAndSaveParticipant("Summer Artist", "Music", "Festival artist");
        EventDTO eventToAdd = createEventDTO(null, "Summer Music Festival 2025", "Amazing music festival", "Music",
                "Los Angeles", "2025-07-15", "12:00", 50.0, 150.0, 5000, 500,
                List.of(toParticipantDTO(participant)));

        EventDTO savedEvent = eventService.addEvent(eventToAdd);
        Event eventInRepository = eventRepository.findById(savedEvent.id()).orElseThrow();

        assertNotNull(savedEvent.id());
        assertEquals(eventToAdd.title(), eventInRepository.getTitle());
        assertEquals(eventToAdd.description(), eventInRepository.getDescription());
        assertEquals(eventToAdd.category(), eventInRepository.getCategory());
        assertEquals(eventToAdd.location(), eventInRepository.getLocation());
        assertEquals(eventToAdd.date(), eventInRepository.getDate());
        assertEquals(eventToAdd.time(), eventInRepository.getTime());
        assertEquals(eventToAdd.basicPrice(), eventInRepository.getBasicPrice());
        assertEquals(eventToAdd.vipPrice(), eventInRepository.getVipPrice());
        assertEquals(eventToAdd.availableBasicTickets(), eventInRepository.getAvailableBasicTickets());
        assertEquals(eventToAdd.availableVipTickets(), eventInRepository.getAvailableVipTickets());
        assertEquals(1, eventInRepository.getParticipants().size());
        assertEquals(participant.getId(), eventInRepository.getParticipants().get(0).getId());
    }

    @Test
    public void shouldRejectEventCreationWithMissingParticipantThroughService() {
        ParticipantDTO missingParticipant = new ParticipantDTO(999L, "Missing", "Music", "Missing participant", false);
        EventDTO eventToAdd = createEventDTO(null, "Invalid Event", "Description", "Music", "Madrid",
                "2025-07-15", "12:00", 50.0, 150.0, 100, 20, List.of(missingParticipant));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> eventService.addEvent(eventToAdd));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void shouldUpdateEventThroughService() throws SQLException {
        Event savedEvent = createAndSaveEvent("Tech Conference 2025", "Innovative tech conference", "Technology",
                "San Francisco", "2025-09-20", "09:00", 100.0, 300.0, 1000, 200, null);
        Participant participant = createAndSaveParticipant("Updated Participant", "Technology",
                "Updated participant biography");
        EventDTO eventToUpdate = createEventDTO(savedEvent.getId(), "Tech Conference 2025 - Updated",
                "Updated description", "Technology", "San Francisco", "2025-09-20", "09:00", 120.0, 350.0, 900,
                150, List.of(toParticipantDTO(participant)));

        EventDTO updatedEvent = eventService.replaceEvent(savedEvent.getId(), eventToUpdate);
        Event eventInRepository = eventRepository.findById(updatedEvent.id()).orElseThrow();

        assertEquals(eventToUpdate.title(), eventInRepository.getTitle());
        assertEquals(eventToUpdate.description(), eventInRepository.getDescription());
        assertEquals(eventToUpdate.category(), eventInRepository.getCategory());
        assertEquals(eventToUpdate.location(), eventInRepository.getLocation());
        assertEquals(eventToUpdate.basicPrice(), eventInRepository.getBasicPrice());
        assertEquals(eventToUpdate.vipPrice(), eventInRepository.getVipPrice());
        assertEquals(1, eventInRepository.getParticipants().size());
        assertEquals(participant.getId(), eventInRepository.getParticipants().get(0).getId());
    }

    @Test
    public void shouldThrowNotFoundWhenUpdatingMissingEventThroughService() {
        EventDTO eventToUpdate = createEventDTO(null, "Missing Event", "Description", "Technology", "Madrid",
                "2025-09-20", "09:00", 120.0, 350.0, 900, 150, null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> eventService.replaceEvent(999L, eventToUpdate));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void shouldDeleteEventThroughService() {
        Event savedEvent = createAndSaveEvent("Event To Delete", "Delete description", "Technology", "San Francisco",
                "2025-09-20", "09:00", 100.0, 300.0, 1000, 200, null);

        EventDTO deletedEvent = eventService.deleteEvent(savedEvent.getId());

        assertEquals(savedEvent.getId(), deletedEvent.id());
        assertEquals(savedEvent.getTitle(), deletedEvent.title());
        assertTrue(eventRepository.findById(savedEvent.getId()).isEmpty());
    }

    @Test
    public void shouldThrowConflictWhenDeletingEventWithTicketsThroughService() {
        Event savedEvent = createAndSaveEvent("Event With Tickets", "Ticketed event", "Technology", "San Francisco",
                "2025-09-20", "09:00", 100.0, 300.0, 1000, 200, null);
        User user = createAndSaveUser("event_ticket_user", "password", "event-ticket-user@example.com");
        Ticket ticket = createAndSaveTicket("VIP", 100.0, 1, savedEvent, user);
        savedEvent.setTickets(new ArrayList<>(List.of(ticket)));
        eventRepository.save(savedEvent);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> eventService.deleteEvent(savedEvent.getId()));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(eventRepository.findById(savedEvent.getId()).isPresent());
    }

    @Test
    public void shouldThrowNotFoundWhenDeletingMissingEventThroughService() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> eventService.deleteEvent(999L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void shouldGetEventByIdThroughService() {
        Event savedEvent = createAndSaveEvent("Event By Id", "Find by id description", "Technology", "San Francisco",
                "2025-09-20", "09:00", 100.0, 300.0, 1000, 200, null);

        EventDTO retrievedEvent = eventService.getEventById(savedEvent.getId());

        assertEquals(savedEvent.getId(), retrievedEvent.id());
        assertEquals(savedEvent.getTitle(), retrievedEvent.title());
        assertEquals(savedEvent.getDescription(), retrievedEvent.description());
        assertEquals(savedEvent.getCategory(), retrievedEvent.category());
        assertEquals(savedEvent.getLocation(), retrievedEvent.location());
    }

    @Test
    public void shouldThrowNotFoundWhenEventDoesNotExistThroughService() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> eventService.getEventById(999L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void shouldGetEventsByParticipantIdThroughService() {
        Participant participant = createAndSaveParticipant("Alberto", "Music", "Biography of Alberto");
        Event savedEvent = createAndSaveEvent("Participant Event", "Event with participant", "Technology",
                "San Francisco", "2025-09-20", "09:00", 100.0, 300.0, 1000, 200, List.of(participant));

        Page<EventDTO> eventsByParticipantId = eventService.getEventsByParticipantId(participant.getId(),
                PageRequest.of(0, 20));

        assertTrue(eventsByParticipantId.getContent().stream().anyMatch(event -> event.id().equals(savedEvent.getId())));
    }

    @Test
    public void shouldThrowNotFoundWhenParticipantDoesNotExistThroughService() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> eventService.getEventsByParticipantId(999L, PageRequest.of(0, 20)));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void shouldCreateAndGetAndDeleteEventImageThroughService() throws SQLException {
        Event savedEvent = createAndSaveEvent("Image Event", "Event with image", "Testing", "Online", "2025-12-01",
                "20:00", 15.0, 25.0, 50, 10, null);
        byte[] imageBytes = "fake-image".getBytes();

        eventService.createEventImage(savedEvent.getId(), new ByteArrayInputStream(imageBytes), imageBytes.length);
        Event eventWithImage = eventRepository.findById(savedEvent.getId()).orElseThrow();
        Resource imageResource = eventService.getEventImage(savedEvent.getId());

        assertTrue(eventWithImage.getImage());
        assertNotNull(eventWithImage.getImageFile());
        assertNotNull(imageResource);

        eventService.deleteEventImage(savedEvent.getId());
        Event eventWithoutImage = eventRepository.findById(savedEvent.getId()).orElseThrow();

        assertFalse(eventWithoutImage.getImage());
        assertEquals(null, eventWithoutImage.getImageFile());
    }

    @Test
    public void shouldThrowNotFoundWhenGettingMissingEventImageThroughService() {
        Event savedEvent = createAndSaveEvent("No Image Event", "Event without image", "Testing", "Online",
                "2025-12-01", "20:00", 15.0, 25.0, 50, 10, null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> eventService.getEventImage(savedEvent.getId()));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

}
