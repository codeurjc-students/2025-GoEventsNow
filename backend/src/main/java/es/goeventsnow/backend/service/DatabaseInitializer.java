package es.goeventsnow.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.repository.EventRepository;
import jakarta.annotation.PostConstruct;

@Service
public class DatabaseInitializer {

    @Autowired
    private EventRepository eventRepository;

    @PostConstruct
    public void init() {
        eventRepository.save(new Event("Taller de Spring Boot 4.0", "Tecnología", "Fuenlabrada, Madrid", "15-03-2026"));
        eventRepository.save(new Event("Exposición Arte", "Cultura", "Barcelona", "12-02-2026"));
        eventRepository.save(new Event("Torneo de Baloncesto", "Deportes", "Getafe, Madrid", "02-08-2026"));
    }

    
    
}
