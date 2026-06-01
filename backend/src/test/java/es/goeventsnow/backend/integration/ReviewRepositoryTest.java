package es.goeventsnow.backend.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import es.goeventsnow.backend.dto.review.ReviewDTO;
import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.model.User;
import es.goeventsnow.backend.model.Review;
import es.goeventsnow.backend.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
@Transactional
public class ReviewRepositoryTest extends IntegrationTestBase {

    @Autowired
    private ReviewService reviewService;

    @Test
    public void shouldReturnAllSavedReviewsThroughService() {

        Event firstEvent = createAndSaveEvent("Database Testing Event 1", "Description 1", "Testing 1", "USA",
            "2025-10-05", "10:00", 20.0, 50.0, 100, 20, null);
        User firstUser = createAndSaveUser("user1", "User One", 123456789, "password1", "user1@example.com");

        Event secondEvent = createAndSaveEvent("Database Testing Event 2", "Description 2", "Testing 2", "USA",
            "2025-11-05", "12:00", 25.0, 60.0, 80, 30, null);
        User secondUser = createAndSaveUser("user2", "User Two", 987654321, "password2", "user2@example.com");

        Review firstReview = createAndSaveReview("Great event", 5.0, firstUser, firstEvent);
        Review secondReview = createAndSaveReview("Good event", 4.0, secondUser, secondEvent);

        Page<ReviewDTO> reviews = reviewService.getAllReviews(PageRequest.of(0, 20));

        assertNotNull(reviews);
        assertTrue(reviews.getContent().stream().anyMatch(r -> r.description().equals(firstReview.getDescription())));
        assertTrue(reviews.getContent().stream().anyMatch(r -> r.description().equals(secondReview.getDescription())));
    }

    @Test
    public void shouldReturnReviewByIdThroughService() {
        
        Event firstEvent = createAndSaveEvent("Database Testing Event 1", "Description 1", "Testing 1", "USA",
            "2025-10-05", "10:00", 20.0, 50.0, 100, 20, null);
        User firstUser = createAndSaveUser("user1", "User One", 123456789, "password1", "user1@example.com");

        Review firstReview = createAndSaveReview("Great event", 5.0, firstUser, firstEvent);

        ReviewDTO reviewDTO = reviewService.getReviewById(firstReview.getId());

        assertNotNull(reviewDTO);
        assertEquals(firstReview.getDescription(), reviewDTO.description());
    }

    @Test
    public void shouldReturnReviewByEventIdThroughService() {
        
        Event firstEvent = createAndSaveEvent("Database Testing Event 1", "Description 1", "Testing 1", "USA",
            "2025-10-05", "10:00", 20.0, 50.0, 100, 20, null);
        User firstUser = createAndSaveUser("user1", "User One", 123456789, "password1", "user1@example.com");

        Review firstReview = createAndSaveReview("Great event", 5.0, firstUser, firstEvent);

        Page<ReviewDTO> reviews = reviewService.getReviewsByEventId(firstEvent.getId(), PageRequest.of(0, 20));

        assertNotNull(reviews);
        assertTrue(reviews.getContent().stream().anyMatch(r -> r.description().equals(firstReview.getDescription())));
    }

    @Test
    public void shouldReturnReviewByUsernameThroughService() {
        
        Event firstEvent = createAndSaveEvent("Database Testing Event 1", "Description 1", "Testing 1", "USA",
            "2025-10-05", "10:00", 20.0, 50.0, 100, 20, null);
        User firstUser = createAndSaveUser("user1", "User One", 123456789, "password1", "user1@example.com");

        Review firstReview = createAndSaveReview("Great event", 5.0, firstUser, firstEvent);

        Page<ReviewDTO> reviews = reviewService.getReviewsByUsername(firstUser.getUsername(), PageRequest.of(0, 20));

        assertNotNull(reviews);
        assertTrue(reviews.getContent().stream().anyMatch(r -> r.description().equals(firstReview.getDescription())));
    }

    @Test
    public void shouldAddReviewThroughService() {

        Event event = createAndSaveEvent("Add Review Event", "Desc", "Cat", "Loc", "2025-12-01", "18:00", 10.0, 20.0, 50, 10, null);
        User user = createAndSaveUser("adduser", "Add User", 111222333, "pass", "adduser@example.com");

        ReviewDTO reviewToAdd = createReviewDTO(null, "Amazing event", 5.0, event.getId(), user.getId());

        ReviewDTO savedReviewDTO = reviewService.addReview(reviewToAdd, event.getId(), user.getUsername());
        Review reviewInRepository = reviewRepository.findById(savedReviewDTO.id()).orElseThrow();

        assertNotNull(savedReviewDTO.id());
        assertEquals(reviewToAdd.description(), reviewInRepository.getDescription());
        assertEquals(reviewToAdd.rating(), reviewInRepository.getRating());
   
    }

    @Test
    public void shouldDeleteReviewThroughService() {
        Event event = createAndSaveEvent("Delete Event", "Desc", "Cat", "Loc", "2025-12-02", "19:00", 10.0, 20.0, 50, 10, null);
        User user = createAndSaveUser("deluser", "Del User", 444555666, "pass", "deluser@example.com");

        Review savedReview = createAndSaveReview("Review to Delete", 4.0, user, event);

        ReviewDTO deletedReview = reviewService.deleteReview(savedReview.getId(), user.getUsername());

        assertEquals(savedReview.getId(), deletedReview.id());
        assertTrue(reviewRepository.findById(savedReview.getId()).isEmpty());
    }

    @Test
    public void shouldUpdateReviewThroughService() {
        Event event = createAndSaveEvent("Update Event", "Desc", "Cat", "Loc", "2025-12-03", "20:00", 15.0, 25.0, 60, 15, null);
        User user = createAndSaveUser("upduser", "Upd User", 777888999, "pass", "upduser@example.com");

        Review savedReview = createAndSaveReview("Original Review", 3.0, user, event);
        ReviewDTO updatedReview = createReviewDTO(savedReview.getId(), "Updated Review", 4.0, event.getId(), user.getId());

        ReviewDTO returnedReview = reviewService.updateReview(savedReview.getId(), updatedReview, user.getUsername());
        Review reviewInRepository = reviewRepository.findById(returnedReview.id()).orElseThrow();

        assertEquals(updatedReview.description(), reviewInRepository.getDescription());
        assertEquals(updatedReview.rating(), reviewInRepository.getRating());
    }
    
}
