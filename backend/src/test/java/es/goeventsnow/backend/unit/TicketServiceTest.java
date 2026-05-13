package es.goeventsnow.backend.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.goeventsnow.backend.dto.ticket.TicketDTO;
import es.goeventsnow.backend.dto.ticket.TicketMapper;
import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.model.Ticket;
import es.goeventsnow.backend.model.User;
import es.goeventsnow.backend.repository.EventRepository;
import es.goeventsnow.backend.repository.TicketRepository;
import es.goeventsnow.backend.repository.UserRepository;
import es.goeventsnow.backend.service.TicketService;

public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private TicketService ticketService;

    private Ticket firstMockTicket;
    private TicketDTO firstMockTicketDTO;
    private User firstMockUser;
    private Event firstMockEvent;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        firstMockUser = new User("user", "User Name", 123456789, "user@example.com", "encoded-password",
                "USER");
        firstMockUser.setId(1L);

        firstMockEvent = new Event("MockExample1", "Description 1", "Test", "None", "00-00-0000", "00:00",
                10.0, 20.0, 100, 50, new ArrayList<>());
        firstMockEvent.setId(1L);

        firstMockTicket = new Ticket();
        firstMockTicket.setId(1L);
        firstMockTicket.setTicketType("BASIC");
        firstMockTicket.setPrice(50.0);
        firstMockTicket.setNumTickets(1);
        firstMockTicket.setEvent(firstMockEvent);
        firstMockTicket.setUserOwner(firstMockUser);

        firstMockTicketDTO = new TicketDTO(1L, "BASIC", 50.0, 1, 1L, 1L);
    }

    @Test
    public void getTicketsByUsernameTest() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Ticket> ticketPage = new PageImpl<>(List.of(firstMockTicket), pageable, 1);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(firstMockUser));
        when(ticketRepository.findByUserOwnerUsername("user", pageable)).thenReturn(ticketPage);
        when(ticketMapper.toDTO(firstMockTicket)).thenReturn(firstMockTicketDTO);

        Page<TicketDTO> result = ticketService.getTicketsByUsername("user", pageable);

        assertNotNull(result);
        assertEquals(1, result.getNumberOfElements());
        assertEquals("BASIC", result.getContent().get(0).ticketType());
        verify(userRepository, times(1)).findByUsername("user");
        verify(ticketRepository, times(1)).findByUserOwnerUsername("user", pageable);
    }

    @Test
    public void getTicketsByUsernameUserNotFoundTest() {
        Pageable pageable = PageRequest.of(0, 20);

        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ticketService.getTicketsByUsername("missing", pageable));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(ticketRepository, times(0)).findByUserOwnerUsername(any(String.class), any(Pageable.class));
    }

    @Test
    public void getTicketByIdTest() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(firstMockTicket));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(firstMockUser));
        when(ticketMapper.toDTO(firstMockTicket)).thenReturn(firstMockTicketDTO);

        TicketDTO result = ticketService.getTicketById(1L, "user");

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("BASIC", result.ticketType());
    }

    @Test
    public void getTicketByIdNotFoundTest() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ticketService.getTicketById(1L, "user"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void getTicketByIdUserNotFoundTest() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(firstMockTicket));
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ticketService.getTicketById(1L, "missing"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void getTicketByIdForbiddenTest() {
        User otherUser = new User("other", "Other Name", 987654321, "other@example.com", "encoded-password",
                "USER");
        otherUser.setId(2L);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(firstMockTicket));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(otherUser));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ticketService.getTicketById(1L, "user"));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    public void addTicketTest() {
        TicketDTO inputTicketDTO = new TicketDTO(null, "BASIC", 50.0, 1, 1L, 1L);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(firstMockUser));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(firstMockEvent));
        when(ticketMapper.toDomain(any(TicketDTO.class))).thenReturn(firstMockTicket);
        when(ticketRepository.save(firstMockTicket)).thenReturn(firstMockTicket);
        when(ticketMapper.toDTO(firstMockTicket)).thenReturn(firstMockTicketDTO);

        TicketDTO result = ticketService.addTicket(inputTicketDTO, "user");

        assertNotNull(result);
        assertEquals("BASIC", result.ticketType());
        assertEquals(99, firstMockEvent.getAvailableBasicTickets());
        verify(eventRepository, times(1)).save(firstMockEvent);
        verify(ticketRepository, times(1)).save(firstMockTicket);
    }

    @Test
    public void addTicketUserNotFoundTest() {
        TicketDTO inputTicketDTO = new TicketDTO(null, "BASIC", 50.0, 1, 1L, 1L);

        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ticketService.addTicket(inputTicketDTO, "missing"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void addTicketEventNotFoundTest() {
        TicketDTO inputTicketDTO = new TicketDTO(null, "BASIC", 50.0, 1, 1L, 1L);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(firstMockUser));
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ticketService.addTicket(inputTicketDTO, "user"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void addTicketInvalidTypeTest() {
        TicketDTO inputTicketDTO = new TicketDTO(null, "GOLD", 50.0, 1, 1L, 1L);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(firstMockUser));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(firstMockEvent));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ticketService.addTicket(inputTicketDTO, "user"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void addTicketNotEnoughBasicTicketsTest() {
        TicketDTO inputTicketDTO = new TicketDTO(null, "BASIC", 50.0, 9999, 1L, 1L);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(firstMockUser));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(firstMockEvent));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> ticketService.addTicket(inputTicketDTO, "user"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}
