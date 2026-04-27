package es.goeventsnow.backend.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import es.goeventsnow.backend.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket,Long> {

	Page<Ticket> findByUserOwnerUsername(String username, Pageable pageable);
}
