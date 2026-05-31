package es.goeventsnow.backend.dto.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.Collection;
import java.util.List;
import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.dto.ticket.TicketMapper;
import es.goeventsnow.backend.dto.participant.ParticipantMapper;
import es.goeventsnow.backend.dto.review.ReviewMapper;

import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {TicketMapper.class, ParticipantMapper.class, ReviewMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {

    @Mapping(target = "participants", source = "participants")
    @Mapping(target = "tickets", source = "tickets")
    @Mapping(target = "reviews", source = "reviews")
    EventDTO toDTO(Event event);

    List<EventDTO> toDTOs(Collection<Event> events);

    @Mapping(target = "participants", source = "participants")
    @Mapping(target = "tickets", source = "tickets")
    @Mapping(target = "reviews", source = "reviews")
    Event toDomain(EventDTO eventDTO);
    
}
