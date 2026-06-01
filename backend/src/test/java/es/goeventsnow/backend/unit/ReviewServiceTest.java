
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.goeventsnow.backend.dto.review.ReviewDTO;
import es.goeventsnow.backend.dto.review.ReviewMapper;
import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.model.Review;
import es.goeventsnow.backend.model.User;
import es.goeventsnow.backend.repository.EventRepository;
import es.goeventsnow.backend.repository.ReviewRepository;
import es.goeventsnow.backend.repository.UserRepository;
import es.goeventsnow.backend.service.ReviewService;

public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewService reviewService;

    private Review firstMockReview;
    private ReviewDTO firstMockReviewDTO;
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

        firstMockReview = new Review();
        firstMockReview.setId(1L);
        firstMockReview.setRating(4.5);
        firstMockReview.setDescription("Great event");
        firstMockReview.setEventAssociated(firstMockEvent);
        firstMockReview.setUserOwner(firstMockUser);

        firstMockReviewDTO = new ReviewDTO(1L, "Great event", 4.5, 1L, 1L, null);
    }

    @Test
    public void getReviewsByUsernameTest() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Review> reviewPage = new PageImpl<>(List.of(firstMockReview), pageable, 1);

        when(reviewRepository.findAllByUserOwnerUsername("user", pageable)).thenReturn(reviewPage);
        when(reviewMapper.toDTO(firstMockReview)).thenReturn(firstMockReviewDTO);

        Page<ReviewDTO> result = reviewService.getReviewsByUsername("user", pageable);

        assertNotNull(result);
        assertEquals(1, result.getNumberOfElements());
        assertEquals("Great event", result.getContent().get(0).description());
        verify(reviewRepository, times(1)).findAllByUserOwnerUsername("user", pageable);
    }

    @Test
    public void getReviewsByIdTest() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(firstMockReview));
        when(reviewMapper.toDTO(firstMockReview)).thenReturn(firstMockReviewDTO);

        ReviewDTO result = reviewService.getReviewById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Great event", result.description());
    }

    @Test
    public void getReviewByEventIdTest() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Review> reviewPage = new PageImpl<>(List.of(firstMockReview), pageable, 1);

        when(reviewRepository.findAllByEventAssociatedId(1L, pageable)).thenReturn(reviewPage);
        when(reviewMapper.toDTO(firstMockReview)).thenReturn(firstMockReviewDTO);

        Page<ReviewDTO> result = reviewService.getReviewsByEventId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getNumberOfElements());
        assertEquals("Great event", result.getContent().get(0).description());
    }

    @Test
    public void getAllReviewsTest() {

        Pageable pageable = PageRequest.of(0, 20);
        Page<Review> reviewPage = new PageImpl<>(List.of(firstMockReview), pageable, 1);

        when(reviewRepository.findAll(pageable)).thenReturn(reviewPage);
        when(reviewMapper.toDTO(firstMockReview)).thenReturn(firstMockReviewDTO);

        Page<ReviewDTO> result = reviewService.getAllReviews(pageable);

        assertNotNull(result);
        assertEquals(1, result.getNumberOfElements());
        assertEquals("Great event", result.getContent().get(0).description());
    }

    @Test
    public void addReviewTest() {
        ReviewDTO inputReviewDTO = new ReviewDTO(null, "Great event", 5.0, 1L, 1L, null);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(firstMockUser));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(firstMockEvent));
        when(reviewMapper.toDomain(any(ReviewDTO.class))).thenReturn(firstMockReview);

        when(reviewRepository.save(any(Review.class))).thenReturn(firstMockReview);
        when(reviewMapper.toDTO(firstMockReview)).thenReturn(firstMockReviewDTO);

        ReviewDTO result = reviewService.addReview(inputReviewDTO, 1L, "user");

        assertNotNull(result);
        assertEquals("Great event", result.description());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    public void deleteReviewTest() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(firstMockReview));
        when(reviewMapper.toDTO(firstMockReview)).thenReturn(firstMockReviewDTO);

        ReviewDTO result = reviewService.deleteReview(1L, "user");
        assertNotNull(result);
        verify(reviewRepository, times(1)).deleteById(1L);
    }

    @Test
    public void updateReviewTest() {
        ReviewDTO inputReviewDTO = new ReviewDTO(null, "Updated review", 3.0, 1L, 1L, null);
        Review updatedReview = new Review("Updated review", 3.0, firstMockUser, firstMockEvent);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(firstMockReview));
        when(reviewMapper.toDTO(any(Review.class))).thenReturn(inputReviewDTO);
        when(reviewRepository.save(any(Review.class))).thenReturn(updatedReview);

        ReviewDTO result = reviewService.updateReview(1L, inputReviewDTO, "user");

        assertNotNull(result);
        assertEquals("Updated review", result.description());
        assertEquals(3.0, result.rating());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

}
