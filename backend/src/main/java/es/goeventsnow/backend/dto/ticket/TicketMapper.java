package es.goeventsnow.backend.dto.ticket;

import org.mapstruct.Mapper;
import es.goeventsnow.backend.model.Ticket;
import java.util.List;
import java.util.Collection;

@Mapper(componentModel = "spring")
public interface TicketMapper {

        TicketDTO toDTO(Ticket ticket);
    
        Ticket toDomain(TicketDTO ticketDTO);

        List<TicketDTO> toDTOs(Collection<Ticket> tickets);
    
}
