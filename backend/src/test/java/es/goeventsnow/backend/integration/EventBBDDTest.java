package es.goeventsnow.backend.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

        Event event1 = new Event ("BBDD-Testing1 ","Testing1","USA","05-10-2025",null);
        Event event2 = new Event ("BBDD-Testing2","Testing2","Madrid","16-08-2025",null);
        eventRepository.save(event1);
        eventRepository.save(event2);
        EventDTO event1DTO = eventMapper.toDTO(event1);
        EventDTO event2DTO = eventMapper.toDTO(event2);

        Collection<EventDTO> eventListService = eventService.getAllEvents();

        assertTrue(eventListService.stream().anyMatch(e -> e.title().equals(event1DTO.title())));
        assertTrue(eventListService.stream().anyMatch(e -> e.title().equals(event2DTO.title())));

    }

    @Test
    public void addEventBBDDTest () {

        Event eventTest = new Event ("Summer Music Festival 2025","Music","Los Angeles","15-07-2025",null);

        EventDTO eventCreatedDTO = eventService.addEvent(eventMapper.toDTO(eventTest));
        Event eventInRepository = eventRepository.findById(eventCreatedDTO.id()).orElseThrow();

        assertEquals(eventTest.getTitle(),eventInRepository.getTitle(), "The title should match");
        assertEquals(eventTest.getCategory(),eventInRepository.getCategory(),"The category should match");
        assertEquals(eventTest.getDate(),eventInRepository.getDate(),"The date should match");
        assertEquals(eventTest.getLocation(),eventInRepository.getLocation(),"The location should match");
        
    }
    
}
 