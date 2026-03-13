package es.goeventsnow.backend.service;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Collection;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import es.goeventsnow.backend.dto.participant.ParticipantDTO;
import es.goeventsnow.backend.dto.participant.ParticipantMapper;
import es.goeventsnow.backend.model.Participant;
import es.goeventsnow.backend.repository.ParticipantRepository;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private ParticipantMapper participantMapper;

    public Collection<ParticipantDTO> getAllParticipants(){
       return toDTOs(participantRepository.findAll());
    }

    public ParticipantDTO getParticipantById(Long id){
        return toDTO(participantRepository.findById(id).orElseThrow());
    }

    public ParticipantDTO addParticipant(ParticipantDTO participantDTO){
        Participant participant = toDomain(participantDTO);
        return toDTO(participantRepository.save(participant));
    }

    public ParticipantDTO deleteParticipant(long id) {
        Participant participant = participantRepository.findById(id).orElseThrow();
        ParticipantDTO participantDTO = toDTO(participant);
        participantRepository.deleteById(id);
        return participantDTO;    
    }

    public ParticipantDTO replaceParticipant(long id, ParticipantDTO participantDTO) throws SQLException {
        if (participantRepository.existsById(id)){
            Participant participantSaved = participantRepository.findById(id).orElseThrow();
            Participant updatedParticipant = toDomain(participantDTO);
            updatedParticipant.setId(participantSaved.getId());
            updatedParticipant.setParticipantImage(participantSaved.getParticipantImage());
            updatedParticipant.setParticipantImageFile(participantSaved.getParticipantImageFile());
            participantRepository.save(updatedParticipant);
            return toDTO(updatedParticipant);
        } else {
            throw new NoSuchElementException();
        }

    }

    public void createParticipantImage(long id, InputStream inputStream, long size) {
        Participant participant = participantRepository.findById(id).orElseThrow();
        participant.setParticipantImage(true);
        participant.setParticipantImageFile(BlobProxy.generateProxy(inputStream, size));
        participantRepository.save(participant);
    }

    public Resource getParticipantImage(long id) throws SQLException {
        Participant participant = participantRepository.findById(id).orElseThrow();

        if (participant.getParticipantImageFile() == null) {
            throw new NoSuchElementException();
        } else {
            return new InputStreamResource(participant.getParticipantImageFile().getBinaryStream());
        }
    }

    public void replaceParticipantImage(long id, InputStream inputStream, long size) {
        Participant participant = participantRepository.findById(id).orElseThrow();

        if (participant.getParticipantImageFile() == null) {
            throw new NoSuchElementException();
        }

        participant.setParticipantImage(true);
        participant.setParticipantImageFile(BlobProxy.generateProxy(inputStream, size));
        participantRepository.save(participant);
    }

    public void deleteParticipantImage(long id) {
        Participant participant = participantRepository.findById(id).orElseThrow();

        if (participant.getParticipantImageFile() == null) {
            throw new NoSuchElementException();
        }

        participant.setParticipantImage(false);
        participant.setParticipantImageFile(null);
        participantRepository.save(participant);
    }

    private ParticipantDTO toDTO (Participant participant) {
        return participantMapper.toDTO(participant);
    }

    private Collection<ParticipantDTO> toDTOs (Collection<Participant> participants) {
        return participantMapper.toDTOs(participants);
    }

    private Participant toDomain (ParticipantDTO participantDTO) {
        return participantMapper.toDomain(participantDTO);
    }
    
}
