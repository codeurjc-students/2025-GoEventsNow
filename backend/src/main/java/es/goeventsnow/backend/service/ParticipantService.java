package es.goeventsnow.backend.service;

import java.io.InputStream;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import es.goeventsnow.backend.dto.participant.ParticipantDTO;
import es.goeventsnow.backend.dto.participant.ParticipantMapper;
import es.goeventsnow.backend.model.Participant;
import es.goeventsnow.backend.repository.ParticipantRepository;
import es.goeventsnow.backend.repository.EventRepository;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ParticipantMapper participantMapper;

    public Page<ParticipantDTO> getAllParticipants(Pageable pageable) {
        return participantRepository.findAll(pageable).map(this::toDTO);
    }

    public ParticipantDTO getParticipantById(Long id) {
        return toDTO(participantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found")));
    }

    public ParticipantDTO addParticipant(ParticipantDTO participantDTO) {
        Participant participant = toDomain(participantDTO);
        participant.setId(null);
        return toDTO(participantRepository.save(participant));
    }

    public ParticipantDTO deleteParticipant(long id) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found"));
        if (!eventRepository.findByParticipantsId(id, Pageable.unpaged()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Participant cannot be deleted because it is associated with one or more events");
        }
        ParticipantDTO participantDTO = toDTO(participant);
        participantRepository.deleteById(id);
        return participantDTO;
    }

    public ParticipantDTO replaceParticipant(long id, ParticipantDTO participantDTO) throws SQLException {
        if (participantRepository.existsById(id)) {
            Participant participantSaved = participantRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found"));
            Participant updatedParticipant = toDomain(participantDTO);
            updatedParticipant.setId(participantSaved.getId());
            updatedParticipant.setParticipantImage(participantSaved.getParticipantImage());
            updatedParticipant.setParticipantImageFile(participantSaved.getParticipantImageFile());
            participantRepository.save(updatedParticipant);
            return toDTO(updatedParticipant);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found");
        }

    }

    public void createParticipantImage(long id, InputStream inputStream, long size) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found"));
        participant.setParticipantImage(true);
        participant.setParticipantImageFile(BlobProxy.generateProxy(inputStream, size));
        participantRepository.save(participant);
    }

    public Resource getParticipantImage(long id) throws SQLException {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found"));

        if (participant.getParticipantImageFile() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant image not found");
        } else {
            return new InputStreamResource(participant.getParticipantImageFile().getBinaryStream());
        }
    }

    public void replaceParticipantImage(long id, InputStream inputStream, long size) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found"));

        if (participant.getParticipantImageFile() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant image not found");
        }

        participant.setParticipantImage(true);
        participant.setParticipantImageFile(BlobProxy.generateProxy(inputStream, size));
        participantRepository.save(participant);
    }

    public void deleteParticipantImage(long id) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found"));

        if (participant.getParticipantImageFile() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant image not found");
        }

        participant.setParticipantImage(false);
        participant.setParticipantImageFile(null);
        participantRepository.save(participant);
    }

    private ParticipantDTO toDTO(Participant participant) {
        return participantMapper.toDTO(participant);
    }

    private Participant toDomain(ParticipantDTO participantDTO) {
        return participantMapper.toDomain(participantDTO);
    }

}
