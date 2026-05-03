package es.goeventsnow.backend.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import es.goeventsnow.backend.dto.participant.ParticipantDTO;
import es.goeventsnow.backend.dto.participant.ParticipantMapper;
import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.model.Participant;
import es.goeventsnow.backend.repository.EventRepository;
import es.goeventsnow.backend.repository.ParticipantRepository;
import es.goeventsnow.backend.service.ParticipantService;

public class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ParticipantMapper participantMapper;

    @InjectMocks
    private ParticipantService participantService;

    private Participant firstMockParticipant;
    private Participant secondMockParticipant;
    private ParticipantDTO firstMockParticipantDTO;
    private ParticipantDTO secondMockParticipantDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        firstMockParticipant = new Participant("Mock Participant 1", "Singer", "Biography 1");
        firstMockParticipant.setId(1L);

        secondMockParticipant = new Participant("Mock Participant 2", "Speaker", "Biography 2");
        secondMockParticipant.setId(2L);

        firstMockParticipantDTO = new ParticipantDTO(1L, "Mock Participant 1", "Singer", "Biography 1", false);
        secondMockParticipantDTO = new ParticipantDTO(2L, "Mock Participant 2", "Speaker", "Biography 2", false);
    }

    @Test
    public void getAllParticipantsTest() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Participant> participantPage = new PageImpl<>(
                List.of(firstMockParticipant, secondMockParticipant), pageable, 2);

        when(participantRepository.findAll(pageable)).thenReturn(participantPage);
        when(participantMapper.toDTO(firstMockParticipant)).thenReturn(firstMockParticipantDTO);
        when(participantMapper.toDTO(secondMockParticipant)).thenReturn(secondMockParticipantDTO);

        Page<ParticipantDTO> result = participantService.getAllParticipants(pageable);

        assertEquals(2, result.getNumberOfElements());
        assertEquals("Mock Participant 1", result.getContent().get(0).name());
        assertEquals("Mock Participant 2", result.getContent().get(1).name());
        verify(participantRepository, times(1)).findAll(pageable);
        verify(participantMapper, times(2)).toDTO(any(Participant.class));
    }

    @Test
    public void getParticipantByIdTest() {
        when(participantRepository.findById(1L)).thenReturn(Optional.of(firstMockParticipant));
        when(participantMapper.toDTO(firstMockParticipant)).thenReturn(firstMockParticipantDTO);

        ParticipantDTO result = participantService.getParticipantById(1L);

        assertNotNull(result);
        assertEquals("Mock Participant 1", result.name());
        verify(participantRepository, times(1)).findById(1L);
        verify(participantMapper, times(1)).toDTO(firstMockParticipant);
    }

    @Test
    public void getParticipantByIdNotFoundTest() {
        when(participantRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> participantService.getParticipantById(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(participantMapper, never()).toDTO(any(Participant.class));
    }

    @Test
    public void addParticipantTest() {
        ParticipantDTO newParticipantDTO = new ParticipantDTO(99L, "New Participant", "DJ", "New bio", false);
        Participant newParticipant = new Participant("New Participant", "DJ", "New bio");
        Participant savedParticipant = new Participant("New Participant", "DJ", "New bio");
        savedParticipant.setId(3L);
        ParticipantDTO savedParticipantDTO = new ParticipantDTO(3L, "New Participant", "DJ", "New bio", false);

        when(participantMapper.toDomain(newParticipantDTO)).thenReturn(newParticipant);
        when(participantRepository.save(newParticipant)).thenReturn(savedParticipant);
        when(participantMapper.toDTO(savedParticipant)).thenReturn(savedParticipantDTO);

        ParticipantDTO result = participantService.addParticipant(newParticipantDTO);

        assertEquals(3L, result.id());
        assertEquals("New Participant", result.name());
        assertEquals(null, newParticipant.getId());
        verify(participantMapper, times(1)).toDomain(newParticipantDTO);
        verify(participantRepository, times(1)).save(newParticipant);
        verify(participantMapper, times(1)).toDTO(savedParticipant);
    }

    @Test
    public void deleteParticipantTest() {
        when(participantRepository.findById(1L)).thenReturn(Optional.of(firstMockParticipant));
        when(eventRepository.findByParticipantsId(1L, Pageable.unpaged())).thenReturn(Page.empty());
        when(participantMapper.toDTO(firstMockParticipant)).thenReturn(firstMockParticipantDTO);

        ParticipantDTO result = participantService.deleteParticipant(1L);

        assertEquals("Mock Participant 1", result.name());
        verify(participantRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteParticipantNotFoundTest() {
        when(participantRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> participantService.deleteParticipant(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(eventRepository, never()).findByParticipantsId(any(Long.class), any(Pageable.class));
        verify(participantRepository, never()).deleteById(1L);
    }

    @Test
    public void deleteParticipantWithEventsThrowsConflictTest() {
        Event event = new Event("Event", "Description", "Music", "None", "01-01-2026", "20:00",
                10.0, 20.0, 100, 50, List.of(firstMockParticipant));
        Page<Event> eventPage = new PageImpl<>(List.of(event));

        when(participantRepository.findById(1L)).thenReturn(Optional.of(firstMockParticipant));
        when(eventRepository.findByParticipantsId(1L, Pageable.unpaged())).thenReturn(eventPage);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> participantService.deleteParticipant(1L));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(participantRepository, never()).deleteById(1L);
        verify(participantMapper, never()).toDTO(any(Participant.class));
    }

    @Test
    public void replaceParticipantTest() throws SQLException {
        ParticipantDTO updateParticipantDTO = new ParticipantDTO(10L, "Updated Participant", "Actor",
                "Updated bio", false);
        Participant updatedParticipant = new Participant("Updated Participant", "Actor", "Updated bio");
        ParticipantDTO updatedParticipantDTO = new ParticipantDTO(1L, "Updated Participant", "Actor",
                "Updated bio", false);

        when(participantRepository.existsById(1L)).thenReturn(true);
        when(participantRepository.findById(1L)).thenReturn(Optional.of(firstMockParticipant));
        when(participantMapper.toDomain(updateParticipantDTO)).thenReturn(updatedParticipant);
        when(participantMapper.toDTO(updatedParticipant)).thenReturn(updatedParticipantDTO);

        ParticipantDTO result = participantService.replaceParticipant(1L, updateParticipantDTO);

        assertEquals(1L, result.id());
        assertEquals("Updated Participant", result.name());
        assertEquals(1L, updatedParticipant.getId());
        verify(participantRepository, times(1)).save(updatedParticipant);
    }

    @Test
    public void replaceParticipantNotFoundTest() {
        when(participantRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> participantService.replaceParticipant(1L, firstMockParticipantDTO));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(participantRepository, never()).save(any(Participant.class));
    }

    @Test
    public void createParticipantImageTest() {
        InputStream inputStream = new ByteArrayInputStream("image".getBytes());

        when(participantRepository.findById(1L)).thenReturn(Optional.of(firstMockParticipant));

        participantService.createParticipantImage(1L, inputStream, 5L);

        assertTrue(firstMockParticipant.getParticipantImage());
        assertNotNull(firstMockParticipant.getParticipantImageFile());
        verify(participantRepository, times(1)).save(firstMockParticipant);
    }

    @Test
    public void getParticipantImageTest() throws SQLException {
        Blob image = BlobProxy.generateProxy(new ByteArrayInputStream("image".getBytes()), 5L);
        firstMockParticipant.setParticipantImageFile(image);

        when(participantRepository.findById(1L)).thenReturn(Optional.of(firstMockParticipant));

        Resource result = participantService.getParticipantImage(1L);

        assertNotNull(result);
        verify(participantRepository, times(1)).findById(1L);
    }

    @Test
    public void getParticipantImageNotFoundTest() {
        when(participantRepository.findById(1L)).thenReturn(Optional.of(firstMockParticipant));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> participantService.getParticipantImage(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void replaceParticipantImageTest() {
        firstMockParticipant.setParticipantImageFile(BlobProxy.generateProxy(
                new ByteArrayInputStream("old".getBytes()), 3L));
        InputStream inputStream = new ByteArrayInputStream("new".getBytes());

        when(participantRepository.findById(1L)).thenReturn(Optional.of(firstMockParticipant));

        participantService.replaceParticipantImage(1L, inputStream, 3L);

        assertTrue(firstMockParticipant.getParticipantImage());
        assertNotNull(firstMockParticipant.getParticipantImageFile());
        verify(participantRepository, times(1)).save(firstMockParticipant);
    }

    @Test
    public void replaceParticipantImageWithoutExistingImageThrowsNotFoundTest() {
        InputStream inputStream = new ByteArrayInputStream("new".getBytes());

        when(participantRepository.findById(1L)).thenReturn(Optional.of(firstMockParticipant));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> participantService.replaceParticipantImage(1L, inputStream, 3L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(participantRepository, never()).save(any(Participant.class));
    }

    @Test
    public void deleteParticipantImageTest() {
        firstMockParticipant.setParticipantImage(true);
        firstMockParticipant.setParticipantImageFile(BlobProxy.generateProxy(
                new ByteArrayInputStream("image".getBytes()), 5L));

        when(participantRepository.findById(1L)).thenReturn(Optional.of(firstMockParticipant));

        participantService.deleteParticipantImage(1L);

        assertFalse(firstMockParticipant.getParticipantImage());
        assertEquals(null, firstMockParticipant.getParticipantImageFile());
        verify(participantRepository, times(1)).save(firstMockParticipant);
    }

    @Test
    public void deleteParticipantImageWithoutExistingImageThrowsNotFoundTest() {
        when(participantRepository.findById(1L)).thenReturn(Optional.of(firstMockParticipant));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> participantService.deleteParticipantImage(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(participantRepository, never()).save(any(Participant.class));
    }
}
