package es.goeventsnow.backend.unit;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import es.goeventsnow.backend.dto.event.EventDTO;
import es.goeventsnow.backend.dto.event.EventMapper;
import es.goeventsnow.backend.dto.participant.ParticipantDTO;
import es.goeventsnow.backend.model.Ticket;
import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.repository.EventRepository;
import es.goeventsnow.backend.repository.ParticipantRepository;
import es.goeventsnow.backend.service.EventService;

public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventService eventService;

    private Event firstMockEvent, secondMockEvent;
    private EventDTO firstMockEventDTO, secondMockEventDTO;
    private final List<Event> eventList = new ArrayList<>();

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        firstMockEvent = new Event("MockExample1", "Description 1", "Test", "None", "00-00-0000", "00:00", 10.0, 20.0,
                100, 50, null);
        secondMockEvent = new Event("MockExample2", "Description 2", "Test", "None", "00-00-0000", "00:00", 10.0, 20.0,
                100, 50, null);

        firstMockEventDTO = new EventDTO(1L, "MockExample1", "Description 1", "Test", "None", "00-00-0000", "00:00",
                10.0, 20.0, 100, 50, false, new ArrayList<>(), new ArrayList<>());
        secondMockEventDTO = new EventDTO(2L, "MockExample2", "Description 2", "Test", "None", "00-00-0000", "00:00",
                10.0, 20.0, 100, 50, false, new ArrayList<>(), new ArrayList<>());

        eventList.add(firstMockEvent);
        eventList.add(secondMockEvent);

    }

    @Test
    public void getAllEventsTest() {

        Pageable pageable = PageRequest.of(0, 20);
        Page<Event> eventPage = new PageImpl<>(List.of(firstMockEvent, secondMockEvent), pageable, 2);

        when(eventMapper.toDTO(firstMockEvent)).thenReturn(firstMockEventDTO);
        when(eventMapper.toDTO(secondMockEvent)).thenReturn(secondMockEventDTO);
        when(eventRepository.findAll(pageable)).thenReturn(eventPage);

        Page<EventDTO> eventListFromService = eventService.getAllEvents(pageable);

        assertEquals(2, eventListFromService.getNumberOfElements());
        assertEquals(firstMockEventDTO.title(), eventListFromService.getContent().get(0).title());
        assertEquals(secondMockEventDTO.title(), eventListFromService.getContent().get(1).title());

        verify(eventRepository, times(1)).findAll(pageable);
    }

    @Test
    public void getEventByIdTest() {

        when(eventRepository.findById(1L)).thenReturn(Optional.of(firstMockEvent));
        when(eventMapper.toDTO(firstMockEvent)).thenReturn(firstMockEventDTO);

        EventDTO eventFromService = eventService.getEventById(1L);

        assertEquals(firstMockEventDTO.title(), eventFromService.title());
        assertEquals(firstMockEventDTO.description(), eventFromService.description());
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    public void getEventByIdNotFoundTest() {

        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> eventService.getEventById(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void getAllEventsByParticipantTest() {

        Pageable pageable = PageRequest.of(0, 20);
        Page<Event> eventPage = new PageImpl<>(List.of(firstMockEvent, secondMockEvent), pageable, 2);

        when(participantRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.findByParticipantsId(1L, pageable)).thenReturn(eventPage);
        when(eventMapper.toDTO(firstMockEvent)).thenReturn(firstMockEventDTO);
        when(eventMapper.toDTO(secondMockEvent)).thenReturn(secondMockEventDTO);

        Page<EventDTO> eventParticipantListFromService = eventService.getEventsByParticipantId(1L, pageable);

        assertEquals(2, eventParticipantListFromService.getNumberOfElements());
        assertEquals(firstMockEventDTO.title(), eventParticipantListFromService.getContent().get(0).title());
        assertEquals(secondMockEventDTO.title(), eventParticipantListFromService.getContent().get(1).title());

        verify(participantRepository, times(1)).existsById(1L);
        verify(eventRepository, times(1)).findByParticipantsId(1L, pageable);
    }

    @Test
    public void getAllEventsByParticipantNotFoundTest() {

        Pageable pageable = PageRequest.of(0, 20);

        when(participantRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> eventService.getEventsByParticipantId(1L, pageable));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verify(eventRepository, times(0)).findByParticipantsId(any(Long.class), any(Pageable.class));
    }

    @Test
    public void deleteEventTest() {
        firstMockEvent.setTickets(new ArrayList<>());
        when(eventRepository.findById(1L)).thenReturn(Optional.of(firstMockEvent));
        when(eventMapper.toDTO(firstMockEvent)).thenReturn(firstMockEventDTO);

        EventDTO deletedEventDTO = eventService.deleteEvent(1L);

        assertEquals(firstMockEventDTO.title(), deletedEventDTO.title());
        verify(eventRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteEventNotFoundTest() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> eventService.deleteEvent(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void deleteEventWithTicketsTest() {

        Ticket mockTicket = new Ticket();
        firstMockEvent.setTickets(List.of(mockTicket));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(firstMockEvent));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> eventService.deleteEvent(1L));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    @Test
    public void addEventTest() {

        when(eventRepository.save(any(Event.class))).thenReturn(firstMockEvent);
        when(participantRepository.existsById(any(Long.class))).thenReturn(true);
        when(eventMapper.toDTO(firstMockEvent)).thenReturn(firstMockEventDTO);
        when(eventMapper.toDomain(any(EventDTO.class))).thenReturn(firstMockEvent);

        EventDTO addedEventDTO = eventService.addEvent(firstMockEventDTO);

        verify(eventRepository, times(1)).save(any(Event.class));
        assertEquals(firstMockEventDTO.title(), addedEventDTO.title());
    }

    @Test
    public void addNonExistentParticipantEventTest() {
        EventDTO eventDTOWithParticipant = new EventDTO(1L, "MockExample1", "Description 1", "Test", "None",
                "00-00-0000", "00:00",
                10.0, 20.0, 100, 50, false,
                List.of(new ParticipantDTO(999L, "Unknown", "Type", "Bio",
                        false, 0)),
                new ArrayList<>());

        when(eventMapper.toDomain(any(EventDTO.class))).thenReturn(firstMockEvent);
        when(participantRepository.existsById(999L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> eventService.addEvent(eventDTOWithParticipant));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(eventRepository, times(0)).save(any(Event.class));
    }

    @Test
    public void replaceEventTest() throws SQLException {
        when(eventRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(firstMockEvent));
        when(participantRepository.existsById(any(Long.class))).thenReturn(true);
        when(eventMapper.toDomain(any(EventDTO.class))).thenReturn(firstMockEvent);
        when(eventMapper.toDTO(firstMockEvent)).thenReturn(firstMockEventDTO);
        when(eventRepository.save(any(Event.class))).thenReturn(firstMockEvent);

        EventDTO replacedEventDTO = eventService.replaceEvent(1L, firstMockEventDTO);

        verify(eventRepository, times(1)).save(any(Event.class));
        assertEquals(firstMockEventDTO.title(), replacedEventDTO.title());
    }

    @Test
    public void replaceEventNotFoundTest() throws SQLException {
        when(eventRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> eventService.replaceEvent(1L, firstMockEventDTO));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void replaceEventWithNonExistentParticipantTest() throws SQLException {
        EventDTO eventDTOWithParticipant = new EventDTO(1L, "MockExample1", "Description 1", "Test", "None",
                "00-00-0000", "00:00",
                10.0, 20.0, 100, 50, false,
                List.of(new ParticipantDTO(999L, "Unknown", "Type", "Bio",
                        false, 0)),
                new ArrayList<>());

        when(eventRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(firstMockEvent));
        when(participantRepository.existsById(999L)).thenReturn(false);
        when(eventMapper.toDomain(any(EventDTO.class))).thenReturn(firstMockEvent);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> eventService.replaceEvent(1L, eventDTOWithParticipant));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void createEventImageTest() {
        byte[] imageBytes = "fake-image".getBytes();
        InputStream inputStream = new ByteArrayInputStream(imageBytes);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(firstMockEvent));

        eventService.createEventImage(1L, inputStream, imageBytes.length);

        assertTrue(firstMockEvent.getImage());
        assertNotNull(firstMockEvent.getImageFile());

        verify(eventRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).save(firstMockEvent);
    }

    @Test
    public void createEventImageNotFoundTest() {
        byte[] imageBytes = "fake-image".getBytes();
        InputStream inputStream = new ByteArrayInputStream(imageBytes);

        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> eventService.createEventImage(999L, inputStream, imageBytes.length));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(eventRepository, times(0)).save(any(Event.class));
    }

    @Test
    public void getEventImageTest() throws Exception {
        byte[] imageBytes = "fake-image".getBytes();
        Blob blob = BlobProxy.generateProxy(new ByteArrayInputStream(imageBytes), imageBytes.length);

        firstMockEvent.setImage(true);
        firstMockEvent.setImageFile(blob);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(firstMockEvent));

        Resource result = eventService.getEventImage(1L);

        assertNotNull(result);
        assertNotNull(result.getInputStream());

        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    public void getEventImageNotFoundTest() {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> eventService.getEventImage(999L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void getEventImageFileNotFoundTest() {
        firstMockEvent.setImage(false);
        firstMockEvent.setImageFile(null);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(firstMockEvent));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> eventService.getEventImage(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void replaceEventImageTest() {
        byte[] oldImageBytes = "old-image".getBytes();
        Blob oldBlob = BlobProxy.generateProxy(new ByteArrayInputStream(oldImageBytes), oldImageBytes.length);

        byte[] newImageBytes = "new-image".getBytes();
        InputStream newInputStream = new ByteArrayInputStream(newImageBytes);

        firstMockEvent.setImage(true);
        firstMockEvent.setImageFile(oldBlob);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(firstMockEvent));

        eventService.replaceEventImage(1L, newInputStream, newImageBytes.length);

        assertTrue(firstMockEvent.getImage());
        assertNotNull(firstMockEvent.getImageFile());

        verify(eventRepository, times(1)).save(firstMockEvent);
    }

    @Test
    public void replaceEventImageNotFoundTest() {
        byte[] imageBytes = "fake-image".getBytes();
        InputStream inputStream = new ByteArrayInputStream(imageBytes);

        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> eventService.replaceEventImage(999L, inputStream, imageBytes.length));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(eventRepository, times(0)).save(any(Event.class));
    }

    @Test
    public void replaceEventImageFileNotFoundTest() {
        byte[] imageBytes = "fake-image".getBytes();
        InputStream inputStream = new ByteArrayInputStream(imageBytes);

        firstMockEvent.setImage(false);
        firstMockEvent.setImageFile(null);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(firstMockEvent));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> eventService.replaceEventImage(1L, inputStream, imageBytes.length));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(eventRepository, times(0)).save(any(Event.class));
    }

    @Test
    public void deleteEventImageTest() {
        byte[] imageBytes = "fake-image".getBytes();
        Blob blob = BlobProxy.generateProxy(new ByteArrayInputStream(imageBytes), imageBytes.length);

        firstMockEvent.setImage(true);
        firstMockEvent.setImageFile(blob);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(firstMockEvent));

        eventService.deleteEventImage(1L);

        assertFalse(firstMockEvent.getImage());
        assertNull(firstMockEvent.getImageFile());

        verify(eventRepository, times(1)).save(firstMockEvent);
    }

    @Test
    public void deleteEventImageNotFoundTest() {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> eventService.deleteEventImage(999L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(eventRepository, times(0)).save(any(Event.class));
    }

    @Test
    public void deleteEventImageFileNotFoundTest() {
        firstMockEvent.setImage(false);
        firstMockEvent.setImageFile(null);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(firstMockEvent));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> eventService.deleteEventImage(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(eventRepository, times(0)).save(any(Event.class));
    }

}
