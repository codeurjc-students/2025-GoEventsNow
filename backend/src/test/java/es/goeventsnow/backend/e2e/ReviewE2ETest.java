package es.goeventsnow.backend.e2e;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ReviewE2ETest extends E2eTestBase {

    @Test
    void writeReviewShowsModal() {
        createReview(1, "Great event", "2.5");
        waitForId("reviews-section");
        assertTrue(driver.getPageSource().contains("Great event"));

    }

    @Test
    void updateReviewShowsInfo() {

        createReview(1, "Great event", "2.5");
        waitForId("reviews-section");

        openUserMenu();
        clickId("my-profile-link");

        waitForId("user-profile-username");
        clickId("review-section");
        waitForId("event-bought-title-1");
        clickId("btn-open-update-review-modal");
        type("review-description", "Updated review");
        clickId("btn-save-review");
        waitForIdToDisappear("review-description");
        assertTrue(driver.getPageSource().contains("Updated review"));

    }

    @Test
    void deleteReviewShowsInfo() {
        createReview(3, "Bad event", "2.5");
        waitForId("reviews-section");

        openUserMenu();
        clickId("my-profile-link");

        waitForId("user-profile-username");
        clickId("review-section");
        waitForId("event-bought-title-3");
        clickId("btn-delete-review");
        waitForIdToDisappear("event-bought-title-3");
        assertFalse(driver.getPageSource().contains("Bad event"));
    }

    private void createReview(int id,String description, String rating) {
        loginAsAdmin();

        navigateToPath("/event/"+id);
        waitForId("event-detail-page");
        clickId("btn-open-review-modal");

        fillReviewForm(description, rating);
        submitReviewForm();
    }
}
