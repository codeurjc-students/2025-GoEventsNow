package es.goeventsnow.backend.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.goeventsnow.backend.service.EventService;

import org.springframework.web.bind.annotation.GetMapping;

import es.goeventsnow.backend.model.Event;


@RestController
@RequestMapping("/api/v1/events")
public class EventRestController {

    @Autowired
    private EventService eventService;

    @GetMapping("/")
    public Collection<Event> getEvents() {
        return eventService.getAllEvents();
    }
    
    
}
