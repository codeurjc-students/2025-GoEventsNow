package es.goeventsnow.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.goeventsnow.backend.service.EventService;


@Controller
public class EventController {
    
    @Autowired
    private EventService eventService;

    @GetMapping("/")
    public String getMainPage(Model model) {
        
        model.addAttribute("events", eventService.getAllEvents());
        return "mainPage";
    }
    
}
