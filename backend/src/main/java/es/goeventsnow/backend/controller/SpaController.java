package es.goeventsnow.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping({
        "/{path:^(?!api)[^\\.]*}",
        "/**/{path:^(?!api)[^\\.]*}"
    })
    public String redirect() {
        return "forward:/index.html";
    }

}