package es.goeventsnow.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class EventController {
    

    @GetMapping("/")
    public String getMainPage(Model model) {
        
        return "mainPage";
    }
    
}
