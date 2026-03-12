package es.goeventsnow.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import es.goeventsnow.backend.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
    
}
