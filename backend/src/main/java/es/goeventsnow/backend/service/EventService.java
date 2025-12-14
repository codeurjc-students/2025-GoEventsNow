package es.goeventsnow.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.repository.EventRepository;

@Service
public class EventService {
    
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents(){
       return eventRepository.findAll();
    }

    public Event addEvent(Event event){
        return eventRepository.save(event);
    }

    
}
