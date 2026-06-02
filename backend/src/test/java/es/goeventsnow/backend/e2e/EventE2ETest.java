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

        assertEquals("Global Latin Music Festival", springBootWorkshopTitle);
        assertEquals("Stand-Up Comedy Night: Juan Dávila Live", basketballTournamentTitle);
    }

    @Test
    void eventDetailShowsInfo() {
        navigateToHome();

        clickId("event-detail-1");

        waitForId("event-detail-page");
        assertTrue(driver.getPageSource().contains("Global Latin Music Festival"));
    }

    @Test
    void eventSearchShowsInfo() {
        navigateToHome();

        clickId("btn-all-events");

        waitForId("event-title-list-1");        
        assertTrue(driver.getPageSource().contains("Global Latin Music Festival"));
    }

    @Test
    void manageEventsShowsInfo() {
        loginAsAdmin();

        openManageEventsFromMenu();

        assertTrue(driver.getPageSource().contains("Global Latin Music Festival"));
    }

    @Test
    void manageEventsDeleteShowsInfo() {
        loginAsAdmin();
        openManageEventsFromMenu();

        clickId("delete-event-8");

        waitForIdToDisappear("event-manage-title-8");
        assertFalse(driver.getPageSource().contains("Top Dessert Masterclass"));
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
    void filterEventsNameShowsInfo() {
        navigateToPath("/events");

        waitForId("event-title-list-1");
        type("search-name-event", "Comedy");
        clickId("apply-filters-bar");

        waitForPageTextToDisappear("Global Latin Music Festival");
        waitForPageText("Stand-Up Comedy Night: Juan Dávila Live");

        assertTrue(driver.getPageSource().contains("Stand-Up Comedy Night: Juan Dávila Live"));
        assertFalse(driver.getPageSource().contains("Global Latin Music Festival"));
    }

    @Test
    void filterEventsCategoryShowsInfo() {
        navigateToPath("/events");

        waitForId("event-title-list-1");
        setInputValue("search-category-event", "Comedy");
        clickId("apply-filters-bar");

        waitForPageTextToDisappear("Global Latin Music Festival");
        waitForPageText("Stand-Up Comedy Night: Juan Dávila Live");
        assertTrue(driver.getPageSource().contains("Stand-Up Comedy Night: Juan Dávila Live"));
        assertFalse(driver.getPageSource().contains("Global Latin Music Festival"));
    }

    @Test
    void filterEventsSortShowsInfo() {
        navigateToPath("/events");

        waitForId("event-title-list-1");
        waitForSelectOption("sort-select","Recent");
        clickCss("#sort-select option[value='recent']");

        waitForId("event-title-list-11");
        assertTrue(driver.getPageSource().contains("Music, Culture and Social Impact Summit"));
        assertFalse(driver.getPageSource().contains("Global Latin Music Festival"));
    }

}
