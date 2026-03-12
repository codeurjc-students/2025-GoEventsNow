package es.goeventsnow.backend.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.dto.event.EventDTO;
import es.goeventsnow.backend.dto.event.EventMapper;
import es.goeventsnow.backend.repository.EventRepository;
import es.goeventsnow.backend.service.EventService;

public class EventTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventService eventService;

    private Event event1, event2;
    private EventDTO eventDTO1, eventDTO2;
    private final List<Event> eventList = new ArrayList<>();


    @BeforeEach
    public void setUp(){

        MockitoAnnotations.openMocks(this);

        event1 = new Event("MockExample1", "Test", "None", "00-00-0000", null);
        event2 = new Event("MockExample2", "Test", "None", "00-00-0000", null);
        eventDTO1 = new EventDTO(1L, "MockExample1", "Test", "None", "00-00-0000", false, new ArrayList<>(), new ArrayList<>());
        eventDTO2 = new EventDTO(2L, "MockExample2", "Test", "None", "00-00-0000", false, new ArrayList<>(), new ArrayList<>());

        eventList.add(event1);
        eventList.add(event2);


    }

    @Test
    public void getAllEventsTest(){
        
        when(eventRepository.findAll()).thenReturn(eventList);

        when(eventMapper.toDTOs(eventList)).thenReturn(List.of(eventDTO1, eventDTO2));

        Collection<EventDTO> eventListService = eventService.getAllEvents();

        assertEquals(2, eventListService.size());
        List<EventDTO> resultList = new ArrayList<>(eventListService);
        assertEquals(eventDTO1.title(), resultList.get(0).title());
        
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    public void addEventTest() {

        when(eventRepository.save(any(Event.class))).thenReturn(event1);
        when(eventMapper.toDTO(event1)).thenReturn(eventDTO1);
        when(eventMapper.toDomain(any(EventDTO.class))).thenReturn(event1);

        EventDTO eventAddTest = eventService.addEvent(eventDTO1);

        verify(eventRepository, times(1)).save(any(Event.class));
        assertEquals(eventDTO1.title(), eventAddTest.title());
    }

    
}

