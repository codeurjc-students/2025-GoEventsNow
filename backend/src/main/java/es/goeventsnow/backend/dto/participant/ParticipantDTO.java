package es.goeventsnow.backend.dto.participant;


public record ParticipantDTO(
Long id,
String name,
String type,
String biography,
Boolean participantImage
) { }
