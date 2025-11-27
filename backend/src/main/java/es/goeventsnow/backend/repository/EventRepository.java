package es.goeventsnow.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.goeventsnow.backend.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
}
