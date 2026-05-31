package es.goeventsnow.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;
    private double rating;
    private LocalDateTime createdAt;

    @ManyToOne
    private User userOwner;

    @ManyToOne
    private Event eventAssociated;

    public Review() {
    }

    public Review(String description, double rating, User userOwner, Event eventAssociated) {
        this.description = description;
        this.rating = rating;
        this.userOwner = userOwner;
        this.eventAssociated = eventAssociated;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public User getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(User userOwner) {
        this.userOwner = userOwner;
    }

    public Event getEventAssociated() {
        return eventAssociated;
    }

    public void setEventAssociated(Event eventAssociated) {
        this.eventAssociated = eventAssociated;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
