package es.goeventsnow.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.goeventsnow.backend.model.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    public Optional<Participant> findById(Long id);
    
}
