package es.goeventsnow.backend.controller;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.goeventsnow.backend.dto.event.EventDTO;
import es.goeventsnow.backend.service.EventService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/events")
public class EventRestController {

    @Autowired
    private EventService eventService;

    @GetMapping("/")
    public Page<EventDTO> getEvents(@RequestParam(required = false) Long participantId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category, @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice, @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir, Pageable pageable) {

        Sort sort = buildEventSort(sortBy, sortDir);
        Pageable effectivePageable = buildPageable(pageable, sort);

        return eventService.getEvents(participantId, title, category, minPrice, maxPrice, effectivePageable);
    }

    @GetMapping("/{id}")
    public EventDTO getEventById(@PathVariable long id) {
        return eventService.getEventById(id);
    }

    @PostMapping("/")
    public ResponseEntity<EventDTO> postEvent(@Valid @RequestBody EventDTO eventDTO) {
        EventDTO savedEventDTO = eventService.addEvent(eventDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedEventDTO.id())
                .toUri();

        return ResponseEntity.created(location).body(savedEventDTO);
    }

    @DeleteMapping("/{id}")
    public EventDTO deleteEvent(@PathVariable long id) {
        return eventService.deleteEvent(id);
    }

    @PutMapping("/{id}")
    public EventDTO replaceEvent(@PathVariable long id, @Valid @RequestBody EventDTO eventDTO) throws SQLException {
        return eventService.replaceEvent(id, eventDTO);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Object> createEventImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
            throws IOException {

        URI location = fromCurrentRequest().build().toUri();
        eventService.createEventImage(id, imageFile.getInputStream(), imageFile.getSize());
        return ResponseEntity.created(location).build();

    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> getEventImage(@PathVariable long id) throws IOException, SQLException {

        Resource eventImage = eventService.getEventImage(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").body(eventImage);
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<Object> replaceEventImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
            throws IOException {

        eventService.replaceEventImage(id, imageFile.getInputStream(), imageFile.getSize());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteEventImage(@PathVariable long id) {
        eventService.deleteEventImage(id);
        return ResponseEntity.noContent().build();
    }

    private Sort buildEventSort(String sortBy, String sortDir) {
        if (sortBy == null) {
            return Sort.unsorted();
        }

        String dir = sortDir != null ? sortDir.toLowerCase() : null;

        return switch (sortBy) {
            case "recent" -> Sort.by(
                    "asc".equals(dir) ? Sort.Direction.ASC : Sort.Direction.DESC,
                    "id");
            case "price" -> Sort.by(
                    "desc".equals(dir) ? Sort.Direction.DESC : Sort.Direction.ASC,
                    "basicPrice");
            case "title" -> Sort.by(
                    "desc".equals(dir) ? Sort.Direction.DESC : Sort.Direction.ASC,
                    "title");
            case "category" -> Sort.by(
                    "desc".equals(dir) ? Sort.Direction.DESC : Sort.Direction.ASC,
                    "category");
            default -> Sort.unsorted();
        };
    }

    private Pageable buildPageable(Pageable pageable, Sort sort) {
        if (sort.isUnsorted()) {
            return pageable;
        }

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

}
