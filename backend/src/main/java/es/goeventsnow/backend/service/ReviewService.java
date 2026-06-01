package es.goeventsnow.backend.service;


import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import es.goeventsnow.backend.dto.review.ReviewDTO;
import es.goeventsnow.backend.dto.review.ReviewMapper;
import es.goeventsnow.backend.model.Event;
import es.goeventsnow.backend.model.Review;
import es.goeventsnow.backend.model.User;
import es.goeventsnow.backend.repository.EventRepository;
import es.goeventsnow.backend.repository.ReviewRepository;
import es.goeventsnow.backend.repository.UserRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ReviewMapper reviewMapper;

    public Page<ReviewDTO> getAllReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable).map(this::toDTO);
    }

    public Page<ReviewDTO> getReviewsByEventId(Long eventId, Pageable pageable) {
        return reviewRepository.findAllByEventAssociatedId(eventId, pageable).map(this::toDTO);
    }

    public Page<ReviewDTO> getReviewsByUsername(String username, Pageable pageable) {
        return reviewRepository.findAllByUserOwnerUsername(username, pageable).map(this::toDTO);
    }

    public ReviewDTO getReviewById(Long reviewId) {
        return toDTO(getReview(reviewId));
    }

    public ReviewDTO addReview(ReviewDTO reviewDTO, Long eventId, String username) {

        User user = getUser(username);
        Event event = getEvent(eventId);

        Review review = toDomain(reviewDTO);
        review.setId(null);
        review.setEventAssociated(event);
        review.setUserOwner(user);
        review.setCreatedAt(LocalDateTime.now());


        Review savedReview = reviewRepository.save(review);
        return toDTO(savedReview);
    }

    public ReviewDTO deleteReview(Long reviewId, String username) {
        Review review = getReview(reviewId);
        if (!review.getUserOwner().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own reviews");
        }
        ReviewDTO reviewDTO = toDTO(review);
        reviewRepository.deleteById(reviewId);
        return reviewDTO;
    }

    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO, String username ) {
        Review review = getReview(reviewId);
        if (!review.getUserOwner().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own reviews");
        }
        review.setRating(reviewDTO.rating());
        review.setDescription(reviewDTO.description());
        review.setCreatedAt(LocalDateTime.now());
        return toDTO(reviewRepository.save(review));
    }

    private Review getReview(long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
    }

    private ReviewDTO toDTO(Review review) {
        return reviewMapper.toDTO(review);
    }

    private Review toDomain(ReviewDTO reviewDTO) {
        return reviewMapper.toDomain(reviewDTO);
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }


    
}
