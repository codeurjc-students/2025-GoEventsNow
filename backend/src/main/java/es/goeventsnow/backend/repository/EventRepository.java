package es.goeventsnow.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.goeventsnow.backend.model.Event;
import java.util.Optional;


public interface EventRepository extends JpaRepository<Event, Long> {

     Optional<Event> findById(Long id);

}
