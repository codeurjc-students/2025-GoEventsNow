package es.goeventsnow.backend.controller;

import java.net.URI;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import es.goeventsnow.backend.dto.review.ReviewDTO;
import es.goeventsnow.backend.service.ReviewService;
import org.springframework.web.bind.annotation.PutMapping;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewRestController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/")
    public ResponseEntity<Page<ReviewDTO>> getAllReviews(Pageable pageable) {
        return ResponseEntity.ok(reviewService.getAllReviews(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<Page<ReviewDTO>> getReviewsByUsername(@PathVariable String username, Pageable pageable) {
        return ResponseEntity.ok(reviewService.getReviewsByUsername(username, pageable));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<Page<ReviewDTO>> getReviewsByEventId(@PathVariable Long eventId, Pageable pageable) {
        return ResponseEntity.ok(reviewService.getReviewsByEventId(eventId, pageable));
    }

    @PostMapping("/event/{eventId}")
    public ResponseEntity<ReviewDTO> createReview(@PathVariable Long eventId, @Valid @RequestBody ReviewDTO reviewDTO, Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ReviewDTO savedReviewDTO = reviewService.addReview(reviewDTO, eventId, principal.getName());
        
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedReviewDTO.id())
            .toUri();

        return ResponseEntity.created(location).body(savedReviewDTO);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable long reviewId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(reviewService.deleteReview(reviewId, principal.getName()));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable long reviewId, @Valid @RequestBody ReviewDTO reviewDTO, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(reviewService.updateReview(reviewId, reviewDTO, principal.getName()));
    }

    
}
