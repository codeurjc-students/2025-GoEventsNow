package es.goeventsnow.backend.service;

import java.io.IOException;
import java.util.List;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.model.Participant;
import es.goeventsnow.backend.model.User;
import es.goeventsnow.backend.repository.EventRepository;
import es.goeventsnow.backend.repository.ParticipantRepository;
import es.goeventsnow.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;


@Service
public class DatabaseInitializer {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() throws IOException {

        Participant participant1 = new Participant("Bad Bunny", "Music", "Great Artist");
        Participant participant2 = new Participant("Pablo Picasso", "Painter", "Famous Painter");
        Participant participant3 = new Participant("Michael Jordan", "Basketball Player", "Legendary Basketball Player");

        Event event1 = new Event("Taller de Spring Boot 4.0", "Tecnología", "Fuenlabrada, Madrid", "15-03-2026",List.of(participant1));
        Event event2 = new Event("Exposición Arte", "Cultura", "Barcelona", "12-02-2026", List.of(participant2));
        Event event3 = new Event("Torneo de Baloncesto", "Deportes", "Getafe, Madrid", "02-08-2026", List.of(participant3));

        User user1 = new User("user","Registered user", 123456789, "user@gmail.com", passwordEncoder.encode("pass"),"USER");
        User user2 = new User("admin", "Administrator", 987654321, "admin@gmail.com", passwordEncoder.encode("adminpass"), "USER", "ADMIN");

        participantRepository.save(participant1);
        participantRepository.save(participant2);
        participantRepository.save(participant3);

        userRepository.save(user1);
        userRepository.save(user2);

        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        setEventImage(event1, participant1, "static/images/events/event1.jpg");
        setEventImage(event2, participant2, "static/images/events/event1.jpg");
        setEventImage(event3, participant3, "static/images/events/event1.jpg");

    }

    public void setEventImage(Event event, Participant participant,String classpathResource) throws IOException {
        event.setImage(true);
        Resource image = new ClassPathResource(classpathResource);
        event.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));

        participant.setParticipantImage(true);
        Resource participantImage = new ClassPathResource(classpathResource);
        participant.setParticipantImageFile(BlobProxy.generateProxy(participantImage.getInputStream(), participantImage.contentLength()));
    }

}
