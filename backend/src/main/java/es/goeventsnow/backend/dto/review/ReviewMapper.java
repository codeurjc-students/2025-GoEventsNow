package es.goeventsnow.backend.dto.review;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import es.goeventsnow.backend.model.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "eventAssociatedId", source = "eventAssociated.id")
    @Mapping(target = "userOwnerId", source = "userOwner.id")
    ReviewDTO toDTO(Review review);

    @Mapping(target = "eventAssociated.id", source = "eventAssociatedId")
    @Mapping(target = "userOwner.id", source = "userOwnerId")
    Review toDomain(ReviewDTO reviewDTO);

    List<ReviewDTO> toDTOs(Collection<Review> reviews);

}
