package es.goeventsnow.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.goeventsnow.backend.model.Event;


public interface EventRepository extends JpaRepository<Event, Long> {

     Optional<Event> findById(Long id);
     Page<Event> findByParticipantsId(Long participantId, Pageable pageable);
     Page<Event> findByIdIn(List<Long> ids, Pageable pageable);

     @Query("""
           select distinct e
           from Event e
           left join e.participants p
           where (:participantId is null or p.id = :participantId)
             and (:title is null or lower(e.title) like lower(concat('%', :title, '%')))
             and (:category is null or e.category = :category)
             and (:minPrice is null or e.basicPrice >= :minPrice)
             and (:maxPrice is null or e.basicPrice <= :maxPrice)
           """)
     Page<Event> findEventsByFilters(
           @Param("participantId") Long participantId,
           @Param("title") String title,
           @Param("category") String category,
           @Param("minPrice") Double minPrice,
           @Param("maxPrice") Double maxPrice,
           Pageable pageable);
}
