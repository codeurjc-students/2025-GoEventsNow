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
        Participant participant3 = new Participant("Michael Jordan", "Basketball Player",
                "Legendary Basketball Player");
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

        Event event1 = new Event("Spring Boot 4.0 Workshop", "Intensive workshop on the framework's new features.",
                "Technology", "Fuenlabrada, Madrid", "2026-03-15", "10:00", 50.0, 120.0, 100, 20,
                List.of(participant1));
        Event event2 = new Event("Art Exhibition", "International contemporary art exhibition.", "Culture", "Barcelona",
                "2026-02-12", "17:00", 15.0, 40.0, 200, 50, List.of(participant2));
        Event event3 = new Event("Basketball Tournament", "Regional amateur team competition.", "Sports",
                "Getafe, Madrid", "2026-08-02", "09:30", 10.0, 25.0, 500, 50, List.of(participant3));
        Event event4 = new Event("Keynote: Future of AI", "The brightest minds discuss the impact of AI.", "Technology",
                "San Francisco, USA", "2026-04-20", "11:00", 150.0, 350.0, 1000, 150, List.of(participant4));
        Event event5 = new Event("Motomami World Tour", "The highly acclaimed world tour arrives in the capital.",
                "Music", "Madrid, WiZink Center", "2026-05-10", "21:00", 80.0, 250.0, 15000, 1000,
                List.of(participant5));
        Event event6 = new Event("Avant-Garde Cooking Masterclass", "Exclusive techniques with Michelin-starred chefs.",
                "Gastronomy", "London", "2026-06-18", "12:00", 200.0, 450.0, 40, 10, List.of(participant6));
        Event event7 = new Event("World Cup Final", "The most anticipated sports event of the year.", "Sports",
                "Miami, USA", "2026-07-15", "20:00", 500.0, 2500.0, 60000, 5000, List.of(participant7));
        Event event8 = new Event("International Science Congress",
                "Debates and presentations on the latest scientific advances.", "Education", "Paris", "2026-09-22",
                "09:00", 120.0, 300.0, 2000, 200, List.of(participant8));
        Event event9 = new Event("Movie Premiere", "World premiere with the original cast in attendance.", "Cinema",
                "Hollywood, CA", "2026-11-05", "19:00", 60.0, 200.0, 800, 150, List.of(participant9));
        Event event10 = new Event("Roland Garros Final", "The ultimate clay court grand slam match.", "Sports", "Paris",
                "2026-06-07", "15:00", 180.0, 600.0, 14000, 1200, List.of(participant10));
        Event event11 = new Event("The Eras Tour Encore", "Special closing tour concert with surprises.", "Music",
                "New York", "2026-12-13", "20:30", 120.0, 400.0, 55000, 3000, List.of(participant11));
        Event event12 = new Event("Global Health Forum", "Meeting of professionals on health policies.", "Health",
                "Geneva", "2026-10-30", "10:00", 0.0, 50.0, 1500, 200, null);
        Event event13 = new Event("Mexican Retrospective", "Tribute to the great muralists of the 20th century.", "Art",
                "Mexico City", "2026-11-01", "16:00", 20.0, 60.0, 300, 80, List.of(participant13));

        User user1 = new User("user", "Registered user", 123456789, "user@gmail.com", passwordEncoder.encode("pass"),
                "USER");
        User user2 = new User("admin", "Administrator", 987654321, "admin@gmail.com",
                passwordEncoder.encode("adminpass"), "USER", "ADMIN");

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

    public void setEventImage(Event event, Participant participant, String classpathResource) throws IOException {
        event.setImage(true);
        Resource image = new ClassPathResource(classpathResource);
        event.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));

        participant.setParticipantImage(true);
        Resource participantImage = new ClassPathResource(classpathResource);
        participant.setParticipantImageFile(
                BlobProxy.generateProxy(participantImage.getInputStream(), participantImage.contentLength()));
    }

}
