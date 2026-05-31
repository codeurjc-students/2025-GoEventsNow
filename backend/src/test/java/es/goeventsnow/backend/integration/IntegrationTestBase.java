package es.goeventsnow.backend.integration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import es.goeventsnow.backend.dto.event.EventDTO;
import es.goeventsnow.backend.dto.participant.ParticipantDTO;
import es.goeventsnow.backend.dto.user.UserDTO;
import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.model.Participant;
import es.goeventsnow.backend.model.Ticket;
import es.goeventsnow.backend.model.User;
import es.goeventsnow.backend.repository.EventRepository;
import es.goeventsnow.backend.repository.ParticipantRepository;
import es.goeventsnow.backend.repository.TicketRepository;
import es.goeventsnow.backend.repository.UserRepository;

public abstract class IntegrationTestBase {

    @Autowired
    protected EventRepository eventRepository;

    @Autowired
    protected ParticipantRepository participantRepository;

    @Autowired
    protected TicketRepository ticketRepository;

    @Autowired
    protected UserRepository userRepository;

    protected User createAndSaveUser(String username, String fullname, Integer phone, String password, String email) {
        User user = new User(username, fullname, phone, email, password, "USER");
        user.setNumTicketsBought(0);
        user.setFavoriteGenre("None");
        user.setTickets(new ArrayList<>());
        user.setRoles(new ArrayList<>(List.of("USER")));
        return userRepository.save(user);
    }

    protected User createAndSaveUser(String username, String password, String email) {
        return createAndSaveUser(username, username, 123456789, password, email);
    }

    protected Participant createAndSaveParticipant(String name, String type, String biography) {
        Participant participant = new Participant(name, type, biography);
        participant.setEvents(new ArrayList<>());
        return participantRepository.save(participant);
    }

    protected Event createAndSaveEvent(String title, String description, String category, String location, String date,
            String time, double basicPrice, double vipPrice, int availableBasicTickets, int availableVipTickets) {
        return createAndSaveEvent(title, description, category, location, date, time, basicPrice, vipPrice,
                availableBasicTickets, availableVipTickets, null);
    }

    protected Event createAndSaveEvent(String title, String description, String category, String location, String date,
            String time, double basicPrice, double vipPrice, int availableBasicTickets, int availableVipTickets,
            List<Participant> participants) {
        List<Participant> eventParticipants = participants == null || participants.isEmpty()
                ? new ArrayList<>(List.of(createAndSaveParticipant("Default Participant", "Default Type",
                        "Default participant biography")))
                : new ArrayList<>(participants);
        Event event = new Event(title, description, category, location, date, time, basicPrice, vipPrice,
                availableBasicTickets, availableVipTickets, eventParticipants);
        event.setTickets(new ArrayList<>());
        return eventRepository.save(event);
    }

    protected Event createAndSaveEventWithParticipant(Participant participant) {
        return createAndSaveEvent("Participant Integration Event", "Event with participant", "Music", "Madrid",
                "2025-10-01", "20:00", 50.0, 150.0, 100, 20, List.of(participant));
    }

    protected Ticket createAndSaveTicket(Event event, User user, String ticketType, Double price, Integer numTickets) {
        Ticket ticket = new Ticket(event, price, ticketType, numTickets);
        ticket.setUserOwner(user);
        return ticketRepository.save(ticket);
    }

    protected Ticket createAndSaveTicket(String type, double price, Integer quantity, Event event, User user) {
        return createAndSaveTicket(event, user, type, price, quantity);
    }

    protected UserDTO createUserDTO(Long id, String fullname, String username, Integer phone, String email,
            String password) {
        return new UserDTO(id, fullname, username, phone, email, password, 0, "None", false, null,
            List.of("USER"), new ArrayList<>(), new ArrayList<>());
    }

    protected EventDTO createEventDTO(Long id, String title, String description, String category, String location,
            String date, String time, Double basicPrice, Double vipPrice, Integer availableBasicTickets,
            Integer availableVipTickets, List<ParticipantDTO> participants) {
        return new EventDTO(id, title, description, category, location, date, time, basicPrice, vipPrice,
            availableBasicTickets, availableVipTickets, false, participants, null);
    }

    protected ParticipantDTO toParticipantDTO(Participant participant) {
        return new ParticipantDTO(participant.getId(), participant.getName(), participant.getType(),
                participant.getBiography(), participant.getParticipantImage(), participant.getNumFollowers());
    }

}