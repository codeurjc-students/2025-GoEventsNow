package es.goeventsnow.backend.service;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import es.goeventsnow.backend.dto.event.EventDTO;
import es.goeventsnow.backend.dto.event.EventMapper;
import es.goeventsnow.backend.dto.participant.ParticipantDTO;
import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.model.Participant;
import es.goeventsnow.backend.repository.EventRepository;
import es.goeventsnow.backend.repository.ParticipantRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private EventMapper eventMapper;

    public Page<EventDTO> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable).map(this::toDTO);
    }

    public EventDTO getEventById(Long id) {
        return toDTO(getEvent(id));
    }

    public Page<EventDTO> getEventsByParticipantId(Long participantId, Pageable pageable) {

        if (!participantRepository.existsById(participantId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found");
        }
        return eventRepository.findByParticipantsId(participantId, pageable).map(this::toDTO);
    }

    public EventDTO addEvent(EventDTO eventDTO) {
        Event eventSaved = toDomain(eventDTO);
        eventSaved.setParticipants(resolveParticipants(eventDTO.participants()));
        eventSaved.setId(null);
        eventSaved.setTickets(null);
        eventSaved.setReviews(null);
        eventRepository.save(eventSaved);
        return toDTO(eventSaved);
    }

    public EventDTO deleteEvent(long id) {
        Event event = getEvent(id);
        if (!event.getTickets().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Event cannot be deleted because it has associated tickets");
        }
        EventDTO eventDTO = toDTO(event);
        eventRepository.deleteById(id);
        return eventDTO;
    }

    public EventDTO replaceEvent(long id, EventDTO eventDTO) throws SQLException {
        Event eventSaved = getEvent(id);

        applyEventFields(eventSaved, eventDTO);
        eventSaved.setParticipants(resolveParticipants(eventDTO.participants()));

        eventRepository.save(eventSaved);
        return toDTO(eventSaved);
    }

    public void createEventImage(long id, InputStream inputStream, long size) {
        updateEventImage(getEvent(id), inputStream, size);
    }

    public Resource getEventImage(long id) throws SQLException {
        Event event = getEvent(id);
        ensureImageExists(event.getImageFile(), "Event image not found");
        return new InputStreamResource(event.getImageFile().getBinaryStream());
    }

    public void replaceEventImage(long id, InputStream inputStream, long size) {
        Event event = getEvent(id);
        ensureImageExists(event.getImageFile(), "Event image not found");
        updateEventImage(event, inputStream, size);
    }

    public void deleteEventImage(long id) {
        Event event = getEvent(id);
        ensureImageExists(event.getImageFile(), "Event image not found");
        event.setImage(false);
        event.setImageFile(null);
        eventRepository.save(event);
    }

    private Event getEvent(long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    private List<Participant> resolveParticipants(List<ParticipantDTO> participants) {
        return participants.stream()
                .map(participant -> participantRepository.findById(participant.id())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Participant with " + participant.id() + " does not exist")))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void applyEventFields(Event event, EventDTO eventDTO) {
        event.setTitle(eventDTO.title());
        event.setDescription(eventDTO.description());
        event.setCategory(eventDTO.category());
        event.setLocation(eventDTO.location());
        event.setDate(eventDTO.date());
        event.setTime(eventDTO.time());
        event.setBasicPrice(eventDTO.basicPrice());
        event.setVipPrice(eventDTO.vipPrice());
        event.setAvailableBasicTickets(eventDTO.availableBasicTickets());
        event.setAvailableVipTickets(eventDTO.availableVipTickets());
    }

    private void updateEventImage(Event event, InputStream inputStream, long size) {
        event.setImage(true);
        event.setImageFile(BlobProxy.generateProxy(inputStream, size));
        eventRepository.save(event);
    }

    private void ensureImageExists(Blob imageFile, String notFoundMessage) {
        if (imageFile == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, notFoundMessage);
        }
    }

    private EventDTO toDTO(Event event) {
        return eventMapper.toDTO(event);
    }

    private Event toDomain(EventDTO eventDTO) {
        return eventMapper.toDomain(eventDTO);
    }
}
