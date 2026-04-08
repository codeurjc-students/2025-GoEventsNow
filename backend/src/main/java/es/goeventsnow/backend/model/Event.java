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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;
    private String category;
    private String location;
    private String date;
    private String time;
    private Double basicPrice;
    private Double vipPrice;
    private int availableBasicTickets;
    private int availableVipTickets;
    private boolean image;

    @Lob
    @JsonIgnore
    private Blob imageFile;

    @ManyToMany
    private List<Participant> participants;

    @OneToMany(mappedBy = "event")
    private List<Ticket> tickets;

    public Event() {
    }

    public Event(String title, String description, String category, String location, String date, String time,
            Double basicPrice, Double vipPrice, int availableBasicTickets, int availableVipTickets,
            List<Participant> participants) {
        super();
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.time = time;
        this.date = date;
        this.basicPrice = basicPrice;
        this.vipPrice = vipPrice;
        this.availableBasicTickets = availableBasicTickets;
        this.availableVipTickets = availableVipTickets;
        this.participants = participants;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean getImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public Blob getImageFile() {
        return imageFile;
    }

    public void setImageFile(Blob imageFile) {
        this.imageFile = imageFile;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getBasicPrice() {
        return basicPrice;
    }

    public void setBasicPrice(Double basicPrice) {
        this.basicPrice = basicPrice;
    }

    public Double getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(Double vipPrice) {
        this.vipPrice = vipPrice;
    }

    public int getAvailableBasicTickets() {
        return availableBasicTickets;
    }

    public void setAvailableBasicTickets(int availableBasicTickets) {
        this.availableBasicTickets = availableBasicTickets;
    }

    public int getAvailableVipTickets() {
        return availableVipTickets;
    }

    public void setAvailableVipTickets(int availableVipTickets) {
        this.availableVipTickets = availableVipTickets;
    }

}
