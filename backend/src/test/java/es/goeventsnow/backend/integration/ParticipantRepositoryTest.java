package es.goeventsnow.backend.integration;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import es.goeventsnow.backend.dto.participant.ParticipantDTO;
import es.goeventsnow.backend.model.Participant;
import es.goeventsnow.backend.service.ParticipantService;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class ParticipantRepositoryTest extends IntegrationTestBase {

    @Autowired
    private ParticipantService participantService;

    @Test
    public void shouldReturnAllSavedParticipantsThroughService() {
        Participant firstParticipant = createAndSaveParticipant("Participant One", "Music", "Biography of Participant One");
        Participant secondParticipant = createAndSaveParticipant("Participant Two", "Technology", "Biography of Participant Two");

        Page<ParticipantDTO> participants = participantService.getAllParticipants(PageRequest.of(0, 20));

        assertTrue(participants.getContent().stream().anyMatch(p -> p.name().equals(firstParticipant.getName())));
        assertTrue(participants.getContent().stream().anyMatch(p -> p.name().equals(secondParticipant.getName())));
    }

    @Test
    public void shouldAddParticipantThroughService() {
        ParticipantDTO participantToAdd = new ParticipantDTO(null, "Participant Three", "Art", "Biography of Participant Three", false,0);

        ParticipantDTO savedParticipantDTO = participantService.addParticipant(participantToAdd);
        Participant participantInRepository = participantRepository.findById(savedParticipantDTO.id()).orElseThrow();

        assertNotNull(savedParticipantDTO.id());
        assertEquals(participantToAdd.name(), participantInRepository.getName());
        assertEquals(participantToAdd.type(), participantInRepository.getType());
        assertEquals(participantToAdd.biography(), participantInRepository.getBiography());
        assertEquals(participantToAdd.numFollowers(), participantInRepository.getNumFollowers());
    }

    @Test
    public void shouldGetParticipantByIdThroughService() {
        Participant savedParticipant = createAndSaveParticipant("Participant Four", "Speaker", "Biography of Participant Four");

        ParticipantDTO retrievedParticipant = participantService.getParticipantById(savedParticipant.getId());

        assertEquals(savedParticipant.getId(), retrievedParticipant.id());
        assertEquals(savedParticipant.getName(), retrievedParticipant.name());
        assertEquals(savedParticipant.getType(), retrievedParticipant.type());
        assertEquals(savedParticipant.getBiography(), retrievedParticipant.biography());
    }

    @Test
    public void shouldThrowNotFoundWhenParticipantDoesNotExistThroughService() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> participantService.getParticipantById(999L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void shouldReplaceParticipantThroughService() throws SQLException {
        Participant savedParticipant = createAndSaveParticipant("Original Participant", "Music", "Original biography");
        ParticipantDTO updatedParticipant = new ParticipantDTO(savedParticipant.getId(), "Updated Participant",
                "Technology", "Updated biography", false,0);

        ParticipantDTO replacedParticipant = participantService.replaceParticipant(savedParticipant.getId(), updatedParticipant);
        Participant participantInRepository = participantRepository.findById(replacedParticipant.id()).orElseThrow();

        assertEquals(savedParticipant.getId(), participantInRepository.getId());
        assertEquals("Updated Participant", participantInRepository.getName());
        assertEquals("Technology", participantInRepository.getType());
        assertEquals("Updated biography", participantInRepository.getBiography());
        assertEquals(0, participantInRepository.getNumFollowers());
    }

    @Test
    public void shouldThrowNotFoundWhenReplacingMissingParticipantThroughService() {
        ParticipantDTO updatedParticipant = new ParticipantDTO(null, "Missing Participant", "Music", "Missing biography", false,0);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> participantService.replaceParticipant(999L, updatedParticipant));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void shouldDeleteParticipantThroughService() {
        Participant savedParticipant = createAndSaveParticipant("Participant To Delete", "Music", "Biography");

        ParticipantDTO deletedParticipant = participantService.deleteParticipant(savedParticipant.getId());

        assertEquals(savedParticipant.getId(), deletedParticipant.id());
        assertTrue(participantRepository.findById(savedParticipant.getId()).isEmpty());
    }

    @Test
    public void shouldThrowConflictWhenDeletingParticipantAssociatedWithEventThroughService() {
        Participant savedParticipant = createAndSaveParticipant("Associated Participant", "Music", "Biography");
        createAndSaveEventWithParticipant(savedParticipant);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> participantService.deleteParticipant(savedParticipant.getId()));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(participantRepository.findById(savedParticipant.getId()).isPresent());
    }

    @Test
    public void shouldThrowNotFoundWhenDeletingMissingParticipantThroughService() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> participantService.deleteParticipant(999L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

}
