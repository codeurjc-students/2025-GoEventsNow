package es.goeventsnow.backend.controller;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.goeventsnow.backend.dto.event.EventDTO;
import es.goeventsnow.backend.service.EventService;

import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/v1/events")
public class EventRestController {

    @Autowired
    private EventService eventService;

    @GetMapping("/")
    public Collection<EventDTO> getEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public EventDTO getEventById(@PathVariable long id) {
        return eventService.getEventById(id);
    }

    @PostMapping("/")
    public ResponseEntity<EventDTO> postEvent(@RequestBody EventDTO eventDTO) {
        EventDTO savedEventDTO = eventService.addEvent(eventDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedEventDTO.id())
                .toUri();

        return ResponseEntity.created(location).body(savedEventDTO);
    }

    @DeleteMapping("/{id}")
    public EventDTO deleteEvent(@PathVariable long id){
        return eventService.deleteEvent(id);
    }

    @PutMapping("/{id}")
    public EventDTO replaceEvent(@PathVariable long id, @RequestBody EventDTO eventDTO) throws SQLException {
        return eventService.replaceEvent(id, eventDTO);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Object> createEventImage(@PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException {

        URI location = fromCurrentRequest().build().toUri();
        eventService.createEventImage(id, imageFile.getInputStream(), imageFile.getSize());
        return ResponseEntity.created(location).build();
   
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> getEventImage(@PathVariable long id) throws IOException, SQLException {

        Resource eventImage = eventService.getEventImage(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE,"image/jpeg").body(eventImage);
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<Object> replaceEventImage(@PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException {

        eventService.replaceEventImage(id, imageFile.getInputStream(), imageFile.getSize());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteEventImage(@PathVariable long id) {

        eventService.deleteEventImage(id);
        return ResponseEntity.noContent().build();
    }
    
}
