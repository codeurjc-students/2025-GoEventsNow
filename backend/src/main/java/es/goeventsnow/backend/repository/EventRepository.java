package es.goeventsnow.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.goeventsnow.backend.model.Event;
import java.util.Optional;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    public Optional<Event> findById(Long id);
    
}
