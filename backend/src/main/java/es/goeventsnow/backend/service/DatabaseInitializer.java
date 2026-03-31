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
        Participant participant4 = new Participant("Elon Musk", "Technology", "Visionary Entrepreneur");
        Participant participant5 = new Participant("Rosalía", "Music", "Grammy Winner Artist");
        Participant participant6 = new Participant("Gordon Ramsay", "Chef", "World Class Michelin Star Chef");
        Participant participant7 = new Participant("Lionel Messi", "Football Player", "World Champion Athlete");
        Participant participant8 = new Participant("Marie Curie", "Scientist", "Nobel Prize in Physics and Chemistry");
        Participant participant9 = new Participant("Steven Spielberg", "Director", "Acclaimed Film Director");
        Participant participant10 = new Participant("Rafael Nadal", "Tennis Player", "King of Clay");
        Participant participant11 = new Participant("Taylor Swift", "Music", "Global Pop Icon");
        Participant participant12 = new Participant("Bill Gates", "Philanthropist", "Co-founder of Microsoft");
        Participant participant13 = new Participant("Frida Kahlo", "Painter", "Iconic Surrealist Painter");

        Event event1 = new Event("Taller de Spring Boot 4.0", "Tecnología", "Fuenlabrada, Madrid", "2026-03-15",List.of(participant1));
        Event event2 = new Event("Exposición Arte", "Cultura", "Barcelona", "2026-02-12", List.of(participant2));
        Event event3 = new Event("Torneo de Baloncesto", "Deportes", "Getafe, Madrid", "2026-08-02", List.of(participant3));
        Event event4 = new Event("Keynote: Futuro de la IA", "Tecnología", "San Francisco, USA", "2026-04-20", List.of(participant4));
        Event event5 = new Event("Motomami World Tour", "Música", "Madrid, WiZink Center", "2026-05-10", List.of(participant5));
        Event event6 = new Event("Masterclass Cocina de Vanguardia", "Gastronomía", "Londres", "2026-06-18", List.of(participant6));
        Event event7 = new Event("Final Copa del Mundo", "Deportes", "Miami, USA", "2026-07-15", List.of(participant7));
        Event event8 = new Event("Congreso Internacional de Ciencia", "Educación", "París", "2026-09-22", List.of(participant8));
        Event event9 = new Event("Estreno Cinematográfico", "Cine", "Hollywood, CA", "2026-11-05", List.of(participant9));
        Event event10 = new Event("Final Roland Garros", "Deportes", "París", "2026-06-07", List.of(participant10));
        Event event11 = new Event("The Eras Tour Encore", "Música", "Nueva York", "2026-12-13", List.of(participant11));
        Event event12 = new Event("Foro Global de Salud", "Salud", "Ginebra", "2026-10-30", null);
        Event event13 = new Event("Retrospectiva Mexicana", "Arte", "Ciudad de México", "2026-11-01", List.of(participant13));

        User user1 = new User("user","Registered user", 123456789, "user@gmail.com", passwordEncoder.encode("pass"),"USER");
        User user2 = new User("admin", "Administrator", 987654321, "admin@gmail.com", passwordEncoder.encode("adminpass"), "USER", "ADMIN");

            setEventImage(event1, participant1, "static/images/events/event1.jpg");
            setEventImage(event2, participant2, "static/images/events/event1.jpg");
            setEventImage(event3, participant3, "static/images/events/event1.jpg");
            setEventImage(event4, participant4, "static/images/events/event1.jpg");
            setEventImage(event5, participant5, "static/images/events/event1.jpg");
            setEventImage(event6, participant6, "static/images/events/event1.jpg");
            setEventImage(event7, participant7, "static/images/events/event1.jpg");
            setEventImage(event8, participant8, "static/images/events/event1.jpg");
            setEventImage(event9, participant9, "static/images/events/event1.jpg");
            setEventImage(event10, participant10, "static/images/events/event1.jpg");
            setEventImage(event11, participant11, "static/images/events/event1.jpg");
            setEventImage(event12, participant12, "static/images/events/event1.jpg");
            setEventImage(event13, participant13, "static/images/events/event1.jpg");


        participantRepository.save(participant1);
        participantRepository.save(participant2);
        participantRepository.save(participant3);
        participantRepository.save(participant4);
        participantRepository.save(participant5);
        participantRepository.save(participant6);
        participantRepository.save(participant7);
        participantRepository.save(participant8);
        participantRepository.save(participant9);
        participantRepository.save(participant10);
        participantRepository.save(participant11);
        participantRepository.save(participant12);
        participantRepository.save(participant13);


        userRepository.save(user1);
        userRepository.save(user2);

        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);
        eventRepository.save(event4);
        eventRepository.save(event5);   
        eventRepository.save(event6);
        eventRepository.save(event7);
        eventRepository.save(event8);
        eventRepository.save(event9);
        eventRepository.save(event10);
        eventRepository.save(event11);
        eventRepository.save(event12);
        eventRepository.save(event13);


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
