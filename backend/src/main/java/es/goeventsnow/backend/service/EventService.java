package es.goeventsnow.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.repository.EventRepository;

@Service
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents(){
       return eventRepository.findAll();
    }


    
}
