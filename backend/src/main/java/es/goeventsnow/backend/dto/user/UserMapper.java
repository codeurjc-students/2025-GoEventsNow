package es.goeventsnow.backend.dto.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import java.util.Collection;

import es.goeventsnow.backend.dto.ticket.TicketMapper;
import es.goeventsnow.backend.model.User;

@Mapper(componentModel = "spring", uses = {TicketMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "tickets", source = "tickets")
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "password", source = "encodedPassword")
    UserDTO toDTO (User user);

    @Mapping(target = "tickets", source = "tickets")
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "encodedPassword", source = "password")
    User toDomain (UserDTO userDTO);

    Collection<UserDTO> toDTOs (Collection<User> users);
    
}
