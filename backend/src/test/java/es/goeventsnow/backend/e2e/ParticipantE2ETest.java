package es.goeventsnow.backend.e2e;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ParticipantE2ETest extends E2eTestBase {

    @Test
    void participantListShowsInfo() {
        navigateToHome();

        clickId("participants-link");

        waitForId("participant-name-list-1");
        assertTrue(driver.getPageSource().contains("Bad Bunny"));
    }

    @Test
    void participantDetailShowsInfo() {
        navigateToHome();

        clickId("participants-link");
        waitForId("participant-name-list-1");
        clickId("participant-detail-1");

        waitForId("participant-detail-name-1");
        assertTrue(driver.getPageSource().contains("Bad Bunny"));
    }

    @Test
    void manageParticipantsShowsInfo() {
        loginAsAdmin();

        openManageParticipantsFromMenu();

        assertTrue(driver.getPageSource().contains("Bad Bunny"));
    }

    @Test
    void createParticipantShowsInEventForm() {
        loginAsAdmin();
        navigateToPath("/manage-participants");

        waitForId("participant-manage-name-1");
        clickId("create-participant-link");
        fillParticipantForm("New Participant Creation");
        submitParticipantForm();

        navigateToPath("/edit-event/1");
        waitForId("participantIds");
        waitForSelectOption("participantIds", "New Participant Creation");
    }

    @Test
    void deleteParticipantShowsInfo() {
        loginAsAdmin();
        navigateToPath("/manage-participants");

        waitForId("participant-manage-name-1");
        loadMoreParticipants();
        waitForId("participant-manage-name-12");
        clickId("delete-participant-12");

        waitForIdToDisappear("participant-manage-name-12");
        assertFalse(driver.getPageSource().contains("Bill Gates"));
    }

    @Test
    void editParticipantShowsInfo() {
        loginAsAdmin();
        navigateToPath("/manage-participants");

        waitForId("participant-manage-name-1");
        clickId("edit-participant-2");
        type("name", "Edited Participant");
        submitParticipantForm();

        navigateToPath("/participant/2");
        waitForId("participant-detail-name-2");
        assertTrue(driver.getPageSource().contains("Edited Participant"));
    }
}
