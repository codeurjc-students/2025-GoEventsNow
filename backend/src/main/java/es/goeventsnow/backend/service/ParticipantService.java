package es.goeventsnow.backend.service;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

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
import es.goeventsnow.backend.repository.EventRepository;
import es.goeventsnow.backend.repository.ParticipantRepository;

@Service
public class ParticipantService {

    private static final String NOT_FOUND_IMAGE = "Participant image not found";

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
        return toDTO(getParticipant(id));
    }

    public ParticipantDTO addParticipant(ParticipantDTO participantDTO) {
        Participant participant = toDomain(participantDTO);
        participant.setId(null);
        return toDTO(participantRepository.save(participant));
    }

    public ParticipantDTO deleteParticipant(long id) {
        Participant participant = getParticipant(id);
        if (!eventRepository.findByParticipantsId(id, Pageable.unpaged()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Participant cannot be deleted because it is associated with one or more events");
        }
        ParticipantDTO participantDTO = toDTO(participant);
        participantRepository.deleteById(id);
        return participantDTO;
    }

    public ParticipantDTO replaceParticipant(long id, ParticipantDTO participantDTO) throws SQLException {
        Participant participantSaved = getParticipant(id);
        Participant updatedParticipant = toDomain(participantDTO);
        updatedParticipant.setId(participantSaved.getId());
        updatedParticipant.setParticipantImage(participantSaved.getParticipantImage());
        updatedParticipant.setParticipantImageFile(participantSaved.getParticipantImageFile());
        participantRepository.save(updatedParticipant);
        return toDTO(updatedParticipant);
    }

    public void createParticipantImage(long id, InputStream inputStream, long size) {
        updateParticipantImage(getParticipant(id), inputStream, size);
    }

    public Resource getParticipantImage(long id) throws SQLException {
        Participant participant = getParticipant(id);
        ensureImageExists(participant.getParticipantImageFile(), NOT_FOUND_IMAGE);
        return new InputStreamResource(participant.getParticipantImageFile().getBinaryStream());
    }

    public void replaceParticipantImage(long id, InputStream inputStream, long size) {
        Participant participant = getParticipant(id);
        ensureImageExists(participant.getParticipantImageFile(), NOT_FOUND_IMAGE);
        updateParticipantImage(participant, inputStream, size);
    }

    public void deleteParticipantImage(long id) {
        Participant participant = getParticipant(id);
        ensureImageExists(participant.getParticipantImageFile(), NOT_FOUND_IMAGE);
        participant.setParticipantImage(false);
        participant.setParticipantImageFile(null);
        participantRepository.save(participant);
    }

    public Page<ParticipantDTO> getParticipants(String name, List<String> types, Pageable pageable) {
        String normalizedName = name != null && !name.trim().isEmpty() ? name.trim() : null;
        List<String> normalizedTypes = types != null && !types.isEmpty() ? types : null;

        return participantRepository
                .findParticipantsByFilters(normalizedName, normalizedTypes, pageable)
                .map(this::toDTO);
    }

    private Participant getParticipant(long id) {
        return participantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found"));
    }

    private void updateParticipantImage(Participant participant, InputStream inputStream, long size) {
        participant.setParticipantImage(true);
        participant.setParticipantImageFile(BlobProxy.generateProxy(inputStream, size));
        participantRepository.save(participant);
    }

    private void ensureImageExists(Blob imageFile, String notFoundMessage) {
        if (imageFile == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, notFoundMessage);
        }
    }

    private ParticipantDTO toDTO(Participant participant) {
        return participantMapper.toDTO(participant);
    }

    private Participant toDomain(ParticipantDTO participantDTO) {
        return participantMapper.toDomain(participantDTO);
    }

}
