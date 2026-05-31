package es.goeventsnow.backend.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import es.goeventsnow.backend.model.Review;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    Page<Review> findAllByEventAssociatedId(Long eventId, Pageable pageable);
    Page<Review> findAllByUserOwnerUsername(String username, Pageable pageable);
    
}
