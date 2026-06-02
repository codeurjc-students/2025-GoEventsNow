package es.goeventsnow.backend.e2e;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ReviewE2ETest extends E2eTestBase {

    @Test
    void writeReviewShowsModal() {
        createReview(1, "Great event", "2.5");
        waitForPageText("Great event");
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
        clickReviewActionByDescription("Great event", "btn-open-update-review-modal-");
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
        String reviewId = reviewIdByDescription("Bad event");
        clickId("btn-delete-review-" + reviewId);
        waitForIdToDisappear("review-" + reviewId);
        assertFalse(driver.getPageSource().contains("Bad event"));
    }

    private void createReview(int id,String description, String rating) {
        loginAsAdmin();

        navigateToPath("/event/"+id);
        waitForId("event-detail-page");
        clickId("btn-open-review-modal");

        fillReviewForm(description, rating);
        submitReviewForm();
        waitForPageText(description);
    }

    private void clickReviewActionByDescription(String description, String buttonIdPrefix) {
        String reviewId = reviewIdByDescription(description);
        clickId(buttonIdPrefix + reviewId);
    }

    private String reviewIdByDescription(String description) {
        WebElement review = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[starts-with(@id,'review-')][.//*[contains(normalize-space(.), '" + description + "')]]")));

        return review.getDomAttribute("id").replace("review-", "");
    }
}
