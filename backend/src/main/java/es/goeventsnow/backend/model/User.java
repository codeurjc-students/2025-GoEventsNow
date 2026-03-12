package es.goeventsnow.backend.model;

import java.sql.Blob;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String fullname;
    private String email;
    private Integer phone;
    private Integer numTicketsBought;
    private String favoriteGenre;

    private boolean profileImage;
    private String encodedPassword;
    
    @Lob
    private Blob profileImageFile;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    public User() {
        this.numTicketsBought = 0;
    }

    public User(String username, String fullname, Integer phone, String email, String encodedPassword, String... roles) {
        super();
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.encodedPassword = encodedPassword;
        this.profileImage = false;
        this.favoriteGenre = "None";
        this.roles = List.of(roles);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(boolean profileImage) {
        this.profileImage = profileImage;
    }

    public Blob getProfileImageFile() {
        return profileImageFile;
    }

    public void setProfileImageFile(Blob profileImageFile) {
        this.profileImageFile = profileImageFile;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public Integer getNumTicketsBought() {
        return numTicketsBought;
    }

    public void setNumTicketsBought(Integer numTicketsBought) {
        this.numTicketsBought = numTicketsBought;
    }

    public String getFavoriteGenre() {
        return favoriteGenre;
    }

    public void setFavoriteGenre(String favoriteGenre) {
        this.favoriteGenre = favoriteGenre;
    }

}
