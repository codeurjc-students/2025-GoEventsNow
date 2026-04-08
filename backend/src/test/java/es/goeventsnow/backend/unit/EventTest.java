package es.goeventsnow.backend.unit;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

        event1 = new Event("MockExample1", "Description 1", "Test", "None", "00-00-0000", "00:00", 10.0, 20.0, 100, 50, null);
        event2 = new Event("MockExample2", "Description 2", "Test", "None", "00-00-0000", "00:00", 10.0, 20.0, 100, 50, null);
        
        eventDTO1 = new EventDTO(1L, "MockExample1", "Description 1", "Test", "None", "00-00-0000", "00:00", 10.0, 20.0, 100, 50, false, new ArrayList<>(), new ArrayList<>());
        eventDTO2 = new EventDTO(2L, "MockExample2", "Description 2", "Test", "None", "00-00-0000", "00:00", 10.0, 20.0, 100, 50, false, new ArrayList<>(), new ArrayList<>());

        eventList.add(event1);
        eventList.add(event2);


    }

    @Test
    public void getAllEventsTest(){

        Pageable pageable = PageRequest.of(0, 20);
        Page<Event> eventPage = new PageImpl<>(List.of(event1, event2), pageable, 2);

        when(eventMapper.toDTO(event1)).thenReturn(eventDTO1);
        when(eventMapper.toDTO(event2)).thenReturn(eventDTO2);
        
        when(eventRepository.findAll(pageable)).thenReturn(eventPage);

        when(eventMapper.toDTOs(eventList)).thenReturn(List.of(eventDTO1, eventDTO2));

        Page<EventDTO> eventListService = eventService.getAllEvents(pageable);

        assertEquals(2, eventListService.getNumberOfElements());
        assertTrue(eventListService.getContent().stream().anyMatch(e -> e.title().equals(eventDTO1.title())));
        assertTrue(eventListService.getContent().stream().anyMatch(e -> e.title().equals(eventDTO2.title())));

        verify(eventRepository, times(1)).findAll(pageable);
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

