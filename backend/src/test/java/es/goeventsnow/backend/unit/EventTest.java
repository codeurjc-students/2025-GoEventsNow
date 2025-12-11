package es.goeventsnow.backend.unit;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.repository.EventRepository;
import es.goeventsnow.backend.service.EventService;

public class EventTest {

    private EventRepository eventRepository;
    private EventService eventService;
    private Event event1,event2;
    private final List<Event> eventList = new ArrayList<>();


    @BeforeEach
    public void setUp(){

        eventRepository= mock(EventRepository.class);
        eventService= new EventService(eventRepository);
        event1 = new Event("MockExample1", "Test", "None", "00-00-0000");
        event2 = new Event("MockExample2", "Test", "None", "00-00-0000");
        eventList.add(event1);
        eventList.add(event2);


    }

    @Test
    public void getAllEventsTest(){
        
        when(eventRepository.findAll()).thenReturn(eventList);

        List<Event> eventListService = eventService.getAllEvents();

        assertEquals(2, eventListService.size());
        assertEquals(event1.getTitle(), eventListService.get(0).getTitle());
        assertEquals(event2.getTitle(), eventListService.get(1).getTitle());

        verify(eventRepository, times(1)).findAll();


    }

    @Test
    public void addEventTest() {

        when(eventRepository.save(event1)).thenReturn(event1);

        Event eventAddTest = eventService.addEvent(event1);

        verify(eventRepository, times(1)).save(any(Event.class));
        assertEquals(eventAddTest.getTitle(),event1.getTitle());

    }

    
}
