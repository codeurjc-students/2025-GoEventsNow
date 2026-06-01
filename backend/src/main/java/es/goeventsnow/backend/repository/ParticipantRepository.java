package es.goeventsnow.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.goeventsnow.backend.model.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Optional<Participant> findById(Long id);
    Page<Participant> findByIdIn(List<Long> ids, Pageable pageable);
    
    @Query("""
        select p
        from Participant p
        where (:name is null or lower(p.name) like lower(concat('%', :name, '%')))
          and (:types is null or p.type in :types)
        """)
    Page<Participant> findParticipantsByFilters(
        @Param("name") String name,
        @Param("types") List<String> types,
        Pageable pageable
    );
    
}
