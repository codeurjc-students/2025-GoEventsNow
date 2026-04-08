package es.goeventsnow.backend.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.service.EventService;
import jakarta.transaction.Transactional;
import es.goeventsnow.backend.repository.EventRepository;
import es.goeventsnow.backend.dto.event.EventDTO;
import es.goeventsnow.backend.dto.event.EventMapper;

@SpringBootTest
@Transactional
public class EventBBDDTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;


    @Test
    public void getAllEventsBBDDTest () {

        Event event1 = new Event("BBDD-Testing1", "Description 1", "Testing1", "USA", "2025-10-05", "10:00", 20.0, 50.0, 100, 20, null);
        Event event2 = new Event("BBDD-Testing2", "Description 2", "Testing2", "Madrid", "2025-08-16", "18:00", 30.0, 70.0, 200, 50, null);
        eventRepository.save(event1);
        eventRepository.save(event2);
        EventDTO event1DTO = eventMapper.toDTO(event1);
        EventDTO event2DTO = eventMapper.toDTO(event2);

        Pageable pageable = PageRequest.of(0, 20);

        Page<EventDTO> eventListService = eventService.getAllEvents(pageable);

        assertTrue(eventListService.getContent().stream().anyMatch(e -> e.title().equals(event1DTO.title())));
        assertTrue(eventListService.getContent().stream().anyMatch(e -> e.title().equals(event2DTO.title())));

    }

    @Test
    public void addEventBBDDTest () {

        Event eventTest = new Event("Summer Music Festival 2025", "Amazing music festival", "Music", "Los Angeles", "2025-07-15", "12:00", 50.0, 150.0, 5000, 500, null);

        EventDTO eventCreatedDTO = eventService.addEvent(eventMapper.toDTO(eventTest));
        Event eventInRepository = eventRepository.findById(eventCreatedDTO.id()).orElseThrow();

        assertEquals(eventTest.getTitle(), eventInRepository.getTitle(), "The title should match");
        assertEquals(eventTest.getDescription(), eventInRepository.getDescription(), "The description should match");
        assertEquals(eventTest.getCategory(), eventInRepository.getCategory(), "The category should match");
        assertEquals(eventTest.getLocation(), eventInRepository.getLocation(), "The location should match");
        assertEquals(eventTest.getDate(), eventInRepository.getDate(), "The date should match");
        assertEquals(eventTest.getTime(), eventInRepository.getTime(), "The time should match");
        assertEquals(eventTest.getBasicPrice(), eventInRepository.getBasicPrice(), "The basic price should match");
        assertEquals(eventTest.getVipPrice(), eventInRepository.getVipPrice(), "The VIP price should match");
        assertEquals(eventTest.getAvailableBasicTickets(), eventInRepository.getAvailableBasicTickets(), "The available basic tickets should match");
        assertEquals(eventTest.getAvailableVipTickets(), eventInRepository.getAvailableVipTickets(), "The available VIP tickets should match");
        
    }
    
}
 