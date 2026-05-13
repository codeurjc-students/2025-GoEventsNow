package es.goeventsnow.backend.dto.ticket;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.goeventsnow.backend.model.Ticket;
import java.util.List;
import java.util.Collection;

@Mapper(componentModel = "spring")
public interface TicketMapper {


        @Mapping(target = "eventId", source = "event.id")
        @Mapping(target = "userOwnerId", source = "userOwner.id")
        TicketDTO toDTO(Ticket ticket);


        @Mapping(target = "event.id", source = "eventId")
        @Mapping(target = "userOwner.id", source = "userOwnerId")
        Ticket toDomain(TicketDTO ticketDTO);

        List<TicketDTO> toDTOs(Collection<Ticket> tickets);
    
}
