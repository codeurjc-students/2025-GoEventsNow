package es.goeventsnow.backend.dto.review;

import java.time.LocalDateTime;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewDTO(
        Long id,
        @NotBlank 
        String description,
        @NotNull 
        @DecimalMin(value = "0.0")
        @DecimalMax(value = "5.0")
        Double rating,
        @NotNull 
        Long eventAssociatedId,
        Long userOwnerId,
        LocalDateTime createdAt) {}
