package es.goeventsnow.backend.service;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.goeventsnow.backend.dto.event.EventDTO;
import es.goeventsnow.backend.dto.event.EventMapper;
import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.repository.EventRepository;


@Service
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    public Page<EventDTO> getAllEvents(Pageable pageable){
       return eventRepository.findAll(pageable).map(this::toDTO);
    }

    public EventDTO getEventById(Long id){
        return toDTO(eventRepository.findById(id).orElseThrow());
    }

    public EventDTO addEvent(EventDTO eventDTO){
        Event eventSaved = toDomain(eventDTO);
        eventRepository.save(eventSaved);
        return toDTO(eventSaved);
    }

    public EventDTO deleteEvent(long id) {
        Event event = eventRepository.findById(id).orElseThrow();
        EventDTO eventDTO = toDTO(event);
        eventRepository.deleteById(id);
        return eventDTO;    
    }

    public EventDTO replaceEvent(long id, EventDTO eventDTO) throws SQLException {
        if (eventRepository.existsById(id)){
            Event eventSaved = eventRepository.findById(id).orElseThrow();
            Event updatedEvent = toDomain(eventDTO);
            updatedEvent.setId(eventSaved.getId());
            updatedEvent.setImage(eventSaved.getImage());
            updatedEvent.setImageFile(eventSaved.getImageFile());
            eventRepository.save(updatedEvent);
            return toDTO(updatedEvent);
        } else {
            throw new NoSuchElementException();
        }

    }

    public void createEventImage(long id, InputStream inputStream, long size) {
        Event event = eventRepository.findById(id).orElseThrow();
        event.setImage(true);
        event.setImageFile(BlobProxy.generateProxy(inputStream, size));
        eventRepository.save(event);
    }

    public Resource getEventImage(long id) throws SQLException {
        Event event = eventRepository.findById(id).orElseThrow();

        if (event.getImageFile() == null) {
            throw new NoSuchElementException();
        } else {
            return new InputStreamResource(event.getImageFile().getBinaryStream());
        }
    }

    public void replaceEventImage(long id, InputStream inputStream, long size) {
        Event event = eventRepository.findById(id).orElseThrow();

        if (event.getImageFile() == null) {
            throw new NoSuchElementException();
        }

        event.setImage(true);
        event.setImageFile(BlobProxy.generateProxy(inputStream, size));
        eventRepository.save(event);
    }

    public void deleteEventImage(long id) {
        Event event = eventRepository.findById(id).orElseThrow();

        if (event.getImageFile() == null) {
            throw new NoSuchElementException();
        }

        event.setImage(false);
        event.setImageFile(null);
        eventRepository.save(event);
    }

    private EventDTO toDTO ( Event event) {
        return eventMapper.toDTO(event);
    }

    private Collection<EventDTO> toDTOs (Collection<Event> events) {
        return eventMapper.toDTOs(events);
    }

    private Event toDomain (EventDTO eventDTO) {
        return eventMapper.toDomain(eventDTO);
    }
}
