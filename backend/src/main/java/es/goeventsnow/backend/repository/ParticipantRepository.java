package es.goeventsnow.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import es.goeventsnow.backend.model.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    public Optional<Participant> findById(Long id);
    Page<Participant> findByIdIn(List<Long> ids, Pageable pageable);
    
}
