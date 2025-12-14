package es.goeventsnow.backend.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.service.EventService;
import jakarta.transaction.Transactional;
import es.goeventsnow.backend.repository.EventRepository;

@SpringBootTest
@Transactional
public class EventBBDDTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;


    @Test
    public void getAllEventsBBDDTest () {

        Event event1 = new Event ("BBDD-Testing1 ","Testing1","USA","05-10-2025");
        Event event2 = new Event ("BBDD-Testing2","Testing2","Madrid","16-08-2025");
        eventRepository.save(event1);
        eventRepository.save(event2);

        List<Event> eventListService = eventService.getAllEvents();

        assertTrue(eventListService.stream().anyMatch(e -> e.getTitle().equals(event1.getTitle())));
        assertTrue(eventListService.stream().anyMatch(e -> e.getTitle().equals(event2.getTitle())));


    }

    @Test
    public void addEventBBDDTest () {

        Event eventTest = new Event ("Summer Music Festival 2025","Music","Los Angeles","15-07-2025");

        Event eventCreated = eventService.addEvent(eventTest);
        Event eventInRepository = eventRepository.findById(eventCreated.getId()).orElseThrow();

        assertEquals(eventTest.getTitle(),eventInRepository.getTitle(), "The title should match");
        assertEquals(eventTest.getCategory(),eventInRepository.getCategory(),"The category should match");
        assertEquals(eventTest.getDate(),eventInRepository.getDate(),"The date should match");
        assertEquals(eventTest.getLocation(),eventInRepository.getLocation(),"The location should match");
        
    }
    
}
