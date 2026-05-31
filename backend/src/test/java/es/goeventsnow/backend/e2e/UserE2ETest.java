package es.goeventsnow.backend.e2e;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class UserE2ETest extends E2eTestBase {

    @Test
    void loginShowsUserInfo() {
        loginAsAdmin();

        assertTrue(driver.getPageSource().contains("admin"));
    }

    @Test
    void profileShowsInfo() {
        loginAsAdmin();

        openUserMenu();
        clickId("my-profile-link");

        waitForId("user-profile-username");
        assertTrue(driver.getPageSource().contains("admin"));
    }

    @Test
    void logoutShowsLoginLink() {
        loginAsAdmin();

        openUserMenu();
        clickId("logout-link");

        waitForId("event-list");
        assertTrue(driver.getPageSource().contains("Log In"));
    }

    @Test
    void registerShowsNewUserInfo() {
        String usernameValue = "selenium" + System.currentTimeMillis();

        registerAndLoginUser(usernameValue);

        assertTrue(driver.getPageSource().contains(usernameValue));
    }

    @Test
    void favoriteEventShowsInProfile() {
        loginAsAdmin();

        navigateToPath("/event/1");
        waitForId("event-detail-page");
        clickId("btn-favorite");

        openUserMenu();
        clickId("my-profile-link");

        waitForId("user-profile-username");
        clickId("favoriteEvents-section");

        waitForId("event-favorite-title-1");
        assertTrue(driver.getPageSource().contains("Global Latin Music Festival"));
    }

    @Test
    void unfavoriteEventShows() {

        loginAsAdmin();

        navigateToPath("/event/3");
        waitForId("event-detail-page");
        clickId("btn-favorite");

        openUserMenu();
        clickId("my-profile-link");

        waitForId("user-profile-username");
        clickId("favoriteEvents-section");

        waitForId("event-favorite-title-3");
        clickId("remove-favorite-3");

        waitForIdToDisappear("event-favorite-title-3");
        assertFalse(driver.getPageSource().contains("Stand-Up Comedy Night: Juan Dávila Live"));

    }

    @Test
    void followParticipantShowsInProfile() {
        loginAsAdmin();

        navigateToPath("/participant/1");
        waitForId("participant-detail-name-1");
        clickId("btn-follow");

        openUserMenu();
        clickId("my-profile-link");

        waitForId("user-profile-username");
        clickId("followedParticipants-section");

        waitForId("participant-title-1");
        assertTrue(driver.getPageSource().contains("unfollow-1"));
    }

    @Test
    void unfollowParticipantShows() {
        loginAsAdmin();

        navigateToPath("/participant/3");
        waitForId("participant-detail-name-3");
        clickId("btn-follow");

        openUserMenu();
        clickId("my-profile-link");

        waitForId("user-profile-username");
        clickId("followedParticipants-section");

        waitForId("participant-title-3");
        clickId("unfollow-3");

        waitForIdToDisappear("participant-title-3");
        assertFalse(driver.getPageSource().contains("Juan Dávila"));

    }

    @Test
    void registerInvalidDataShowsValidationInfo() {
        navigateToHome();

        clickId("register-link");
        type("fullname", "");
        type("username", "");
        type("email", "invalid-email");
        type("phone", "invalid-phone");
        type("password", "123");
        type("confirmPassword", "1234");

        assertTrue(driver.getPageSource().contains("Fullname is required"));
        assertTrue(driver.getPageSource().contains("Username must be at least 3 characters long."));
        assertTrue(driver.getPageSource().contains("Email is required"));
        assertTrue(driver.getPageSource().contains("Please enter a valid 9-digit phone number."));
        assertTrue(driver.getPageSource().contains("Password must be at least 5 characters long."));
        assertTrue(driver.getPageSource().contains("Passwords do not match"));
    }

    @Test
    void nonExistingRouteShowsNotFoundInfo() {
        navigateToPath("/non-existing-route");

        waitForId("error-section");
        assertTrue(driver.getPageSource().contains("Error 404: Page Not Found"));
    }

    private void registerAndLoginUser(String usernameValue) {
        navigateToHome();
        clickId("register-link");

        type("fullname", "Test Selenium User");
        type("username", usernameValue);
        type("email", usernameValue + "@email.com");
        type("phone", "123456789");
        type("password", "12345");
        type("confirmPassword", "12345");
        clickId("register-btn");

        waitForId("login-btn");
        type("username", usernameValue);
        type("password", "12345");
        clickId("login-btn");

        waitForId("event-list");
        waitForId("userMenu");
    }
}
