package es.goeventsnow.backend.dto.participant;

import org.mapstruct.Mapper;
import java.util.List;
import java.util.Collection;
import es.goeventsnow.backend.model.Participant;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {
    
    ParticipantDTO toDTO(Participant participant);

    Participant toDomain(ParticipantDTO participantDTO);

    List<ParticipantDTO> toDTOs(Collection<Participant> participants);
}
