package es.goeventsnow.backend.e2e;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class TicketE2ETest extends E2eTestBase {

    @Test
    void purchaseTicketShowsBoughtEventInProfile() {
        payTicket();
        clickId("ticket-section");

        waitForId("event-bought-title-1");
        assertTrue(driver.getPageSource().contains("Global Latin Music Festival"));
    }

    @Test
    void chartTicketShowsBoughtByEvent() {
        payTicket();
        navigateToPath("/graphics");

        waitForId("totalTicketsSold");
        waitForPageText("Global Latin Music Festival");
        assertTrue(driver.getPageSource().contains("Global Latin Music Festival"));
    }

    @Test
    void chartTicketShowsBoughtByCategory() {
        
        payTicket();
        navigateToPath("/graphics");

        waitForId("totalTicketsSold");
        waitForPageText("Music");
        assertTrue(driver.getPageSource().contains("Music"));
    }

    private void payTicket() {

        loginAsAdmin();

        waitForId("event-title-1");
        clickId("event-detail-1");
        waitForId("event-detail-page");
        clickId("buy-ticket-link");
        clickId("increase-ticket-btn");
        clickId("pay-btn");

        waitForId("user-profile-username");
        
    }
}
