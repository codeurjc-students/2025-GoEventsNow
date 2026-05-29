package es.goeventsnow.backend.dto.participant;

import jakarta.validation.constraints.NotBlank;

public record ParticipantDTO(
Long id,
@NotBlank
String name,
@NotBlank
String type,
@NotBlank
String biography,
Boolean participantImage,
Integer numFollowers
) { }
