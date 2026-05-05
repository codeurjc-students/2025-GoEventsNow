package es.goeventsnow.backend.service;

import java.io.InputStream;
import java.sql.SQLException;

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
        return toDTO(eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found")));
    }

    public Page<EventDTO> getEventsByParticipantId(Long participantId, Pageable pageable) {

        if (!participantRepository.existsById(participantId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found");
        }
        return eventRepository.findByParticipantsId(participantId, pageable).map(this::toDTO);
    }

    public EventDTO addEvent(EventDTO eventDTO) {
        Event eventSaved = toDomain(eventDTO);
        if (eventDTO.participants() != null) {
            eventSaved.setParticipants(
                    eventDTO.participants().stream()
                            .map(p -> participantRepository.findById(p.id())
                                    .orElseThrow(() -> new ResponseStatusException(
                                            HttpStatus.BAD_REQUEST,
                                            "Participant with " + p.id() + " does not exist")))
                            .toList());
        }
        eventSaved.setId(null);
        eventSaved.setTickets(null);
        eventRepository.save(eventSaved);
        return toDTO(eventSaved);
    }

    public EventDTO deleteEvent(long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        if (!event.getTickets().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Event cannot be deleted because it has associated tickets");
        }
        EventDTO eventDTO = toDTO(event);
        eventRepository.deleteById(id);
        return eventDTO;
    }

    public EventDTO replaceEvent(long id, EventDTO eventDTO) throws SQLException {
        if (eventRepository.existsById(id)) {
            Event eventSaved = eventRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
            Event updatedEvent = toDomain(eventDTO);
            if (eventDTO.participants() != null) {
                eventSaved.setParticipants(
                        eventDTO.participants().stream()
                                .map(p -> participantRepository.findById(p.id())
                                        .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.BAD_REQUEST,
                                                "Participant with " + p.id() + " does not exist")))
                                .toList());
            }
            eventSaved.setTitle(eventDTO.title());
            eventSaved.setDescription(eventDTO.description());
            eventSaved.setCategory(eventDTO.category());
            eventSaved.setLocation(eventDTO.location());
            eventSaved.setDate(eventDTO.date());
            eventSaved.setTime(eventDTO.time());
            eventSaved.setBasicPrice(eventDTO.basicPrice());
            eventSaved.setVipPrice(eventDTO.vipPrice());
            eventSaved.setAvailableBasicTickets(eventDTO.availableBasicTickets());
            eventSaved.setAvailableVipTickets(eventDTO.availableVipTickets());
            eventSaved.setParticipants(updatedEvent.getParticipants());

            eventRepository.save(eventSaved);
            return toDTO(eventSaved);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }

    }

    public void createEventImage(long id, InputStream inputStream, long size) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        event.setImage(true);
        event.setImageFile(BlobProxy.generateProxy(inputStream, size));
        eventRepository.save(event);
    }

    public Resource getEventImage(long id) throws SQLException {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        if (event.getImageFile() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event image not found");
        } else {
            return new InputStreamResource(event.getImageFile().getBinaryStream());
        }
    }

    public void replaceEventImage(long id, InputStream inputStream, long size) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        if (event.getImageFile() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event image not found");
        }

        event.setImage(true);
        event.setImageFile(BlobProxy.generateProxy(inputStream, size));
        eventRepository.save(event);
    }

    public void deleteEventImage(long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        if (event.getImageFile() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event image not found");
        }

        event.setImage(false);
        event.setImageFile(null);
        eventRepository.save(event);
    }

    private EventDTO toDTO(Event event) {
        return eventMapper.toDTO(event);
    }

    private Event toDomain(EventDTO eventDTO) {
        return eventMapper.toDomain(eventDTO);
    }
}
