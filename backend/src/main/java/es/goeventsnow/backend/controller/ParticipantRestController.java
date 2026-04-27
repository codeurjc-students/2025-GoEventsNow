package es.goeventsnow.backend.controller;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import es.goeventsnow.backend.dto.participant.ParticipantDTO;
import es.goeventsnow.backend.service.ParticipantService;

@RestController
@RequestMapping("/api/v1/participants")
public class ParticipantRestController {
    
    @Autowired
    private ParticipantService participantService;

    @GetMapping("/")
    public Page<ParticipantDTO> getParticipants(Pageable pageable) {
        return participantService.getAllParticipants(pageable);
    }

    @GetMapping("/{id}")
    public ParticipantDTO getParticipantById(@PathVariable long id) {
        return participantService.getParticipantById(id);
    }

    @PostMapping("/")
    public ResponseEntity<ParticipantDTO> postParticipant(@RequestBody ParticipantDTO participantDTO) {
        ParticipantDTO savedParticipantDTO = participantService.addParticipant(participantDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedParticipantDTO.id())
                .toUri();

        return ResponseEntity.created(location).body(savedParticipantDTO);
    }

    @DeleteMapping("/{id}")
    public ParticipantDTO deleteParticipant(@PathVariable long id) {
        return participantService.deleteParticipant(id);
    }

    @PutMapping("/{id}")
    public ParticipantDTO replaceParticipant(@PathVariable long id, @RequestBody ParticipantDTO participantDTO) throws SQLException {
        return participantService.replaceParticipant(id, participantDTO);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Object> createParticipantImage(@PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException {

        URI location = fromCurrentRequest().build().toUri();
        participantService.createParticipantImage(id, imageFile.getInputStream(), imageFile.getSize());
        return ResponseEntity.created(location).build();
   
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> getParticipantImage(@PathVariable long id) throws IOException, SQLException {

        Resource participantImage = participantService.getParticipantImage(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE,"image/jpeg").body(participantImage);
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<Object> replaceParticipantImage(@PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException {

        participantService.replaceParticipantImage(id, imageFile.getInputStream(), imageFile.getSize());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteParticipantImage(@PathVariable long id) {

        participantService.deleteParticipantImage(id);
        return ResponseEntity.noContent().build();
    }
}
