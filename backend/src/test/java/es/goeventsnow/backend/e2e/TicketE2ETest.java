package es.goeventsnow.backend.e2e;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TicketE2ETest extends E2eTestBase {

    @Test
    void purchaseTicketShowsBoughtEventInProfile() {
        loginAsAdmin();

        waitForId("event-title-1");
        clickId("event-detail-1");
        waitForId("event-detail-page");
        clickId("buy-ticket-link");
        clickId("increase-ticket-btn");
        clickId("pay-btn");

        waitForId("user-profile-username");
        clickId("ticket-section");

        waitForId("event-bought-title-1");
        assertTrue(driver.getPageSource().contains("Spring Boot 4.0 Workshop"));
    }
}
