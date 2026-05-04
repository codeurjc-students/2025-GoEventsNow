package es.goeventsnow.backend.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EventE2ETest extends E2eTestBase {

    @Test
    void eventListShowsEvents() {
        navigateToHome();

        waitForId("event-list");
        String springBootWorkshopTitle = waitForId("event-title-1").getText();
        String basketballTournamentTitle = waitForId("event-title-3").getText();

        assertEquals("Spring Boot 4.0 Workshop", springBootWorkshopTitle);
        assertEquals("Basketball Tournament", basketballTournamentTitle);
    }

    @Test
    void eventDetailShowsInfo() {
        navigateToHome();

        clickId("event-detail-1");

        waitForId("event-detail-page");
        assertTrue(driver.getPageSource().contains("Spring Boot 4.0 Workshop"));
    }

    @Test
    void eventSearchShowsInfo() {
        navigateToHome();

        clickId("btn-all-events");

        waitForId("event-title-list-1");        
        assertTrue(driver.getPageSource().contains("Spring Boot 4.0 Workshop"));
    }

    @Test
    void manageEventsShowsInfo() {
        loginAsAdmin();

        openManageEventsFromMenu();

        assertTrue(driver.getPageSource().contains("Spring Boot 4.0 Workshop"));
    }

    @Test
    void manageEventsDeleteShowsInfo() {
        loginAsAdmin();
        openManageEventsFromMenu();

        clickId("delete-event-8");

        waitForIdToDisappear("event-manage-title-8");
        assertFalse(driver.getPageSource().contains("International Science Congress"));
    }

    @Test
    void manageEventsEditShowsInfo() {
        loginAsAdmin();
        openManageEventsFromMenu();

        clickId("edit-event-7");
        type("title", "New Title Edited");
        submitEventForm();

        waitForId("event-title-1");
        navigateToPath("/event/7");
        waitForId("event-detail-page");
        assertTrue(driver.getPageSource().contains("New Title Edited"));
    }

    @Test
    void createEventShowsItInParticipantDetail() {
        loginAsAdmin();
        openManageEventsFromMenu();

        clickId("create-event-link");
        fillEventForm("New Event Creation 213");
        submitEventForm();

        navigateToPath("/participant/1");
        waitForId("participant-detail-name-1");
        assertTrue(driver.getPageSource().contains("New Event Creation 213"));
    }
}
