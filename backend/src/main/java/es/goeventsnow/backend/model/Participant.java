package es.goeventsnow.backend.model;

import java.sql.Blob;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;

@Entity
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String type;
    private String biography;
    private boolean participantImage;

    @Lob
    @JsonIgnore
    private Blob participantImageFile;

    @ManyToMany (mappedBy = "participants")

    private List<Event> events;

    public Participant() {}

    public Participant(String name, String type, String biography) {
        this.name = name;
        this.type = type;
        this.biography = biography;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public boolean getParticipantImage() {
        return participantImage;
    }

    public void setParticipantImage(boolean participantImage) {
        this.participantImage = participantImage;
    }

    public Blob getParticipantImageFile() {
        return participantImageFile;
    }

    public void setParticipantImageFile(Blob participantImageFile) {
        this.participantImageFile = participantImageFile;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}