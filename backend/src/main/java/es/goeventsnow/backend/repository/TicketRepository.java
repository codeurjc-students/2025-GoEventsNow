package es.goeventsnow.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.goeventsnow.backend.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket,Long> {

	Page<Ticket> findByUserOwnerUsername(String username, Pageable pageable);

	@Query("select t.event.title, coalesce(sum(t.numTickets), 0) from Ticket t group by t.event.title order by t.event.title")
	List<Object[]> findTicketsSoldByEvent();

	@Query("select t.event.category, coalesce(sum(t.numTickets), 0) from Ticket t group by t.event.category order by t.event.category")
	List<Object[]> findTicketsSoldByCategory();
}
