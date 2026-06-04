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

        private static final String MUSIC_ARTIST = "Music Artist";
        private static final String COMEDIAN = "Comedian";
        private static final String TENNIS_PLAYER = "Professional Tennis Player";
        private static final String SCIENTIST = "Scientist";
        private static final String FILM_DIRECTOR = "Film Director";
        private static final String ACTOR = "Actor";
        private static final String TECH_INNOVATOR = "Tech Innovator";
        private static final String CHEF = "Chef";
        private static final String BASKETBALL_PLAYER = "Basketball Player";
        private static final String ACTRESS = "Actress";
        private static final String SPORTS = "Sports";
        private static final String CULTURE = "Culture";
        private static final String CINEMA = "Cinema";
        private static final String GASTRONOMY = "Gastronomy";
        private static final String MUSIC = "Music";
        private static final String COMEDY = "Comedy";
        private static final String TECHNOLOGY = "Technology";


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

                Participant badBunny = new Participant("Bad Bunny", MUSIC_ARTIST,
                                "Puerto Rican global superstar known for redefining reggaeton and Latin trap, headlining major international festivals and sold-out world tours.");

                Participant oliviaRodrigo = new Participant("Olivia Rodrigo", MUSIC_ARTIST,
                                "Grammy-winning artist recognized for emotional songwriting, powerful vocals and chart-topping pop-rock performances.");
                Participant juanDavila = new Participant("Juan Dávila", COMEDIAN,
                                "Spanish stand-up comedian known for his interactive, provocative and improvisational comedy shows. Famous for breaking the fourth wall and engaging directly with the audience in live performances across Spain.");

                Participant rosalia = new Participant("Rosalía", MUSIC_ARTIST,
                                "Internationally acclaimed Spanish singer blending flamenco, pop and experimental sounds, recognized for highly visual live shows.");

                Participant jannikSinner = new Participant("Jannik Sinner", TENNIS_PLAYER,
                                "Italian tennis player, who has rapidly risen in the ATP rankings with his aggressive playing style and strong performances in major tournaments.");

                Participant carlosAlcaraz = new Participant("Carlos Alcaraz", TENNIS_PLAYER,
                                "Spanish Grand Slam champion known for his explosive playing style, athleticism and rapid rise in world tennis.");

                Participant neilDegrasseTyson = new Participant("Neil deGrasse Tyson", SCIENTIST,
                                "Astrophysicist and science communicator known for making complex scientific topics accessible to global audiences.");

                Participant christopherNolan = new Participant("Christopher Nolan", FILM_DIRECTOR,
                                "Acclaimed filmmaker known for ambitious storytelling, large-scale productions and influential contributions to modern cinema.");

                Participant markRuffalo = new Participant("Mark Ruffalo", ACTOR,
                                "Renowned actor and environmental advocate, speaker at sustainability events and climate-awareness forums.");

                Participant elonMusk = new Participant("Elon Musk", TECH_INNOVATOR,
                                "Entrepreneur and technology leader associated with electric vehicles, space exploration, AI and future mobility.");
                Participant jordiRoca = new Participant("Jordi Roca", CHEF,
                                "Spanish-American chef and humanitarian known for innovative cuisine, culinary education and global food-relief initiatives.");

                Participant pauGasol = new Participant("Pau Gasol", BASKETBALL_PLAYER,
                                "Former professional basketball player, NBA champion and ambassador for sports, health and youth development.");
                Participant zendaya = new Participant(
                                "Zendaya",
                                ACTRESS,
                                "Award-winning actress and fashion icon, recognized for her roles in film and television and presence in international premieres.");

                Participant tomHolland = new Participant(
                                "Tom Holland",
                                ACTOR,
                                "Popular actor known for blockbuster films and appearances in global entertainment conventions and fan events.");

                Event globalLatinMusicFestival = new Event("Global Latin Music Festival",
                                "A large-scale live music festival bringing together leading Latin and international artists. The event includes full-stage performances, VIP fan zones, immersive lighting, food areas and a night focused on reggaeton, pop and flamenco fusion.",
                                MUSIC, "Madrid, WiZink Center", "2026-05-10", "21:00",
                                80.0, 250.0, 15000, 1000,
                                List.of(badBunny, oliviaRodrigo));

                Event oliviaRodrigoConcertExperience = new Event("Olivia Rodrigo Concert Experience",
                                "A concert experience focused on emotional songwriting, acoustic arrangements and powerful live vocals. The event is designed for fans who want a closer connection with the artist and the stories behind the songs.",
                                MUSIC, "Barcelona, Palau Sant Jordi", "2026-06-14", "20:30",
                                55.0, 160.0, 9000, 600,
                                List.of(oliviaRodrigo));

                Event juanDavilaComedyNight = new Event("Stand-Up Comedy Night: Juan Dávila Live",
                                "A live comedy show full of improvisation, audience interaction and unpredictable moments. Juan Dávila brings his characteristic bold humor to a theatre night where the crowd becomes part of the performance.",
                                COMEDY, "Fuenlabrada, Madrid", "2026-03-15", "21:00",
                                25.0, 60.0, 500, 80,
                                List.of(juanDavila));

                Event rolandGarrosChampionsExhibition = new Event("Roland Garros Champions Exhibition",
                                "A premium tennis exhibition featuring elite-level rallies, training demonstrations and a special match format inspired by Grand Slam competition. Fans will enjoy a close look at modern tennis intensity and technique.",
                                SPORTS, "Paris, Court Philippe-Chatrier", "2026-06-07", "15:00",
                                180.0, 600.0, 14000, 1200,
                                List.of(jannikSinner, carlosAlcaraz));

                Event futureSpaceAiConference = new Event("Future of Space and Artificial Intelligence",
                                "A technology conference exploring the future of space exploration, artificial intelligence, electric mobility and scientific communication. The event includes keynote talks, public Q&A and discussions about innovation and society.",
                                TECHNOLOGY, "San Francisco, USA", "2026-04-20", "11:00",
                                150.0, 350.0, 1200, 150,
                                List.of(neilDegrasseTyson, elonMusk));

                Event cinemaMastersForum = new Event("Cinema Masters: Storytelling and Performance",
                                "A cinema forum focused on directing, acting and the creative process behind major film productions. The session includes a director talk, actor discussion and audience questions about modern filmmaking.",
                                CINEMA, "London, BFI Southbank", "2026-09-18", "18:30",
                                70.0, 220.0, 900, 120,
                                List.of(christopherNolan, markRuffalo));

                Event eliteSportsLegendsTournament = new Event("Elite Sports Legends Tournament",
                                "A high-profile international sports event bringing together elite athletes from tennis and basketball. The event includes exhibition matches, skills challenges, fan interactions and training sessions led by world-class professionals. Attendees will experience top-level competition, behind-the-scenes insights and motivational talks on discipline, performance and success in professional sports.",
                                SPORTS, "Miami, USA", "2026-07-20", "18:00",
                                120.0, 400.0, 20000, 1500,
                                List.of(jannikSinner, carlosAlcaraz, pauGasol));

                Event topDessertMasterclass = new Event("Top Dessert Masterclass",
                                "A culinary masterclass focused on creative desserts, pastry techniques, sensory presentation and innovation in modern gastronomy. Attendees will learn about textures, plating and the creative process of high-level cuisine.",
                                GASTRONOMY, "Girona, Spain", "2026-06-18", "12:00",
                                200.0, 450.0, 80, 15,
                                List.of(jordiRoca));

                Event basketballLeadershipCamp = new Event("Basketball Leadership Camp",
                                "A sports and leadership event combining basketball training, personal development and talks about discipline, teamwork and health. Designed for young athletes and fans interested in professional sports culture.",
                                SPORTS, "Barcelona, Spain", "2026-08-02", "09:30",
                                35.0, 90.0, 600, 70,
                                List.of(pauGasol));

                Event youngHollywoodFanConvention = new Event("Young Hollywood Fan Convention",
                                "Entertainment convention featuring panels, meet-and-greet sessions, fan activities and conversations about blockbuster cinema, acting careers and international productions.",
                                CINEMA, "Los Angeles, USA", "2026-11-05", "19:00",
                                90.0, 280.0, 3000, 350,
                                List.of(zendaya, tomHolland));

                Event musicCultureSocialImpactSummit = new Event("Music, Culture and Social Impact Summit",
                                "A multidisciplinary event connecting music, cinema and social impact. Artists and public figures discuss creativity, fame, activism, identity and the influence of entertainment on younger generations.",
                                CULTURE, "New York, USA", "2026-12-13", "20:30",
                                120.0, 400.0, 5000, 500,
                                List.of(oliviaRodrigo, markRuffalo));

                User userRegistered = new User("user", "Registered user", 123456789, "user@gmail.com",
                                passwordEncoder.encode("pass"),
                                "USER");
                User userAdmin = new User("admin", "Administrator", 987654321, "admin@gmail.com",
                                passwordEncoder.encode("adminpass"), "USER", "ADMIN");

                setEventImage(globalLatinMusicFestival, "static/images/events/latinMusicFestival_event.jpg");
                setEventImage(oliviaRodrigoConcertExperience, "static/images/events/oliviaRodrigoConcert_event.jpg");
                setEventImage(juanDavilaComedyNight, "static/images/events/juanDavilaComedy_event.jpg");
                setEventImage(rolandGarrosChampionsExhibition, "static/images/events/rolandGarros_event.jpg");
                setEventImage(futureSpaceAiConference, "static/images/events/futureSpace_event.jpg");
                setEventImage(cinemaMastersForum, "static/images/events/cinemaForum_event.jpg");
                setEventImage(eliteSportsLegendsTournament, "static/images/events/sportsTournament_event.jpg");
                setEventImage(topDessertMasterclass, "static/images/events/gastronomyDessert_event.jpg");
                setEventImage(basketballLeadershipCamp, "static/images/events/basketballCamp_event.jpg");
                setEventImage(youngHollywoodFanConvention, "static/images/events/hollywoodConvention_event.jpg");
                setEventImage(musicCultureSocialImpactSummit, "static/images/events/socialImpact_event.jpg");

                setParticipantImage(badBunny, "static/images/participants/badbunny_participant.jpg");
                setParticipantImage(oliviaRodrigo, "static/images/participants/oliviaRodrigo_participant.jpg");
                setParticipantImage(juanDavila, "static/images/participants/juanDavila_participant.jpg");
                setParticipantImage(rosalia, "static/images/participants/rosalia_participant.jpg");
                setParticipantImage(jannikSinner, "static/images/participants/jannikSinner_participant.jpg");
                setParticipantImage(carlosAlcaraz, "static/images/participants/carlosAlcaraz_participant.jpg");
                setParticipantImage(neilDegrasseTyson, "static/images/participants/neilDegrasse_participant.jpg");
                setParticipantImage(christopherNolan, "static/images/participants/christopherNolan_participant.jpg");
                setParticipantImage(markRuffalo, "static/images/participants/markRuffalo_participant.jpg");
                setParticipantImage(elonMusk, "static/images/participants/elonMusk_participant.jpg");
                setParticipantImage(jordiRoca, "static/images/participants/jordiRoca_participant.jpg");
                setParticipantImage(pauGasol, "static/images/participants/pauGasol_participant.jpg");
                setParticipantImage(zendaya, "static/images/participants/zendaya_participant.jpg");
                setParticipantImage(tomHolland, "static/images/participants/tomHolland_participant.jpg");

                userRepository.saveAll(List.of(userRegistered, userAdmin));

                participantRepository.saveAll(List.of(badBunny, oliviaRodrigo, juanDavila,
                                rosalia, jannikSinner, carlosAlcaraz, neilDegrasseTyson,
                                christopherNolan, markRuffalo, elonMusk, jordiRoca,
                                pauGasol, zendaya, tomHolland));

                eventRepository.saveAll(List.of(globalLatinMusicFestival, oliviaRodrigoConcertExperience,
                                juanDavilaComedyNight, rolandGarrosChampionsExhibition,
                                futureSpaceAiConference, cinemaMastersForum, eliteSportsLegendsTournament,
                                topDessertMasterclass, basketballLeadershipCamp, youngHollywoodFanConvention,
                                musicCultureSocialImpactSummit));

        }

        public void setEventImage(Event event, String classpathResource) throws IOException {
                event.setImage(true);
                Resource image = new ClassPathResource(classpathResource);
                event.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
        }

        public void setParticipantImage(Participant participant, String classpathResource) throws IOException {
                participant.setParticipantImage(true);
                Resource participantImage = new ClassPathResource(classpathResource);
                participant.setParticipantImageFile(BlobProxy.generateProxy(participantImage.getInputStream(),
                                participantImage.contentLength()));
        }

}
