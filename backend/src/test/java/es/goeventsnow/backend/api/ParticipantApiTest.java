package es.goeventsnow.backend.api;

import java.io.File;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class ParticipantApiTest extends BaseApiTest {

    private static final String API_PARTICIPANTS = "/api/v1/participants/";
    private static final String PARTICIPANT_IMAGE_PATH = "src/main/resources/static/images/events/event1.jpg";

    @BeforeEach
    public void setUp() {
        setUpRestAssured();
    }

    @Test
    public void testGetAllParticipants_return200() {
        given()
                .contentType("application/json")
                .when()
                .get(API_PARTICIPANTS)
                .then()
                .statusCode(200)
                .body("content[0].name", is("Bad Bunny"));
    }

    @Test
    public void testGetParticipantById_return200() {
        given()
                .contentType("application/json")
                .when()
                .get(API_PARTICIPANTS + "1")
                .then()
                .statusCode(200)
                .body("name", is("Bad Bunny"))
                .body("type", is("Music"))
                .body("biography", is("Great Artist"))
                .body("participantImage", is(true));
    }

    @Test
    public void testGetParticipantById_return404() {
        given()
                .contentType("application/json")
                .when()
                .get(API_PARTICIPANTS + "999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetParticipantById_return400() {
        given()
                .contentType("application/json")
                .when()
                .get(API_PARTICIPANTS + "notNumber")
                .then()
                .statusCode(400);
    }

    @Test
    public void testCreateParticipantNoAuth_return401() {
        String newParticipant = """
                {
                    "name": "Rosalia",
                    "type": "Music",
                    "biography": "Spanish artist",
                    "participantImage": false
                }
                """;

        given()
                .contentType("application/json")
                .body(newParticipant)
                .when()
                .post(API_PARTICIPANTS)
                .then()
                .statusCode(401);
    }

    @Test
    public void testCreateParticipantUserRole_return403() {
        String newParticipant = """
                {
                    "name": "Rosalia",
                    "type": "Music",
                    "biography": "Spanish artist",
                    "participantImage": false
                }
                """;

        given()
                .header("Cookie", getUserCookie())
                .contentType("application/json")
                .body(newParticipant)
                .when()
                .post(API_PARTICIPANTS)
                .then()
                .statusCode(403);
    }

    @Test
    public void testCreateParticipant_return201() {
        String newParticipant = """
                {
                    "name": "Rosalia",
                    "type": "Music",
                    "biography": "Spanish artist",
                    "participantImage": false
                }
                """;

        given()
                .header("Cookie", getAdminCookie())
                .contentType("application/json")
                .body(newParticipant)
                .when()
                .post(API_PARTICIPANTS)
                .then()
                .statusCode(201)
                .header("Location", containsString(API_PARTICIPANTS))
                .body("id", notNullValue())
                .body("name", is("Rosalia"))
                .body("type", is("Music"))
                .body("biography", is("Spanish artist"));
    }

    @Test
    public void testReplaceParticipant_return200() {
        String updatedParticipant = """
                {
                    "name": "Updated Bad Bunny",
                    "type": "Urban Music",
                    "biography": "Updated biography",
                    "participantImage": true
                }
                """;

        given()
                .header("Cookie", getAdminCookie())
                .contentType("application/json")
                .body(updatedParticipant)
                .when()
                .put(API_PARTICIPANTS + "1")
                .then()
                .statusCode(200)
                .body("name", is("Updated Bad Bunny"))
                .body("type", is("Urban Music"))
                .body("biography", is("Updated biography"));
    }

    @Test
    public void testReplaceParticipantNoAuth_return401() {
        String updatedParticipant = """
                {
                    "name": "Updated No Auth",
                    "type": "Music",
                    "biography": "No auth update",
                    "participantImage": true
                }
                """;

        given()
                .contentType("application/json")
                .body(updatedParticipant)
                .when()
                .put(API_PARTICIPANTS + "1")
                .then()
                .statusCode(401);
    }

    @Test
    public void testReplaceParticipantUserRole_return403() {
        String updatedParticipant = """
                {
                    "name": "Updated User Role",
                    "type": "Music",
                    "biography": "User role should not update",
                    "participantImage": true
                }
                """;

        given()
                .header("Cookie", getUserCookie())
                .contentType("application/json")
                .body(updatedParticipant)
                .when()
                .put(API_PARTICIPANTS + "1")
                .then()
                .statusCode(403);
    }

    @Test
    public void testReplaceParticipant_return404() {
        String updatedParticipant = """
                {
                    "name": "Unknown",
                    "type": "Music",
                    "biography": "Unknown",
                    "participantImage": false
                }
                """;

        given()
                .header("Cookie", getAdminCookie())
                .contentType("application/json")
                .body(updatedParticipant)
                .when()
                .put(API_PARTICIPANTS + "999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteParticipant_return200() {
        String newParticipant = """
                {
                    "name": "Participant To Delete",
                    "type": "Speaker",
                    "biography": "Temporary participant",
                    "participantImage": false
                }
                """;

        int participantId = given()
                .header("Cookie", getAdminCookie())
                .contentType("application/json")
                .body(newParticipant)
                .when()
                .post(API_PARTICIPANTS)
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .header("Cookie", getAdminCookie())
                .contentType("application/json")
                .when()
                .delete(API_PARTICIPANTS + participantId)
                .then()
                .statusCode(200)
                .body("name", is("Participant To Delete"));

        given()
                .contentType("application/json")
                .when()
                .get(API_PARTICIPANTS + participantId)
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteParticipant_return404() {
        given()
                .header("Cookie", getAdminCookie())
                .contentType("application/json")
                .when()
                .delete(API_PARTICIPANTS + "999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteParticipant_return409() {
        given()
                .header("Cookie", getAdminCookie())
                .contentType("application/json")
                .when()
                .delete(API_PARTICIPANTS + "1")
                .then()
                .statusCode(409);
    }

    @Test
    public void testDeleteParticipantUserRole_return403() {
        given()
                .header("Cookie", getUserCookie())
                .contentType("application/json")
                .when()
                .delete(API_PARTICIPANTS + "1")
                .then()
                .statusCode(403);
    }

    @Test
    public void testDeleteParticipantNoAuth_return401() {
        given()
                .contentType("application/json")
                .when()
                .delete(API_PARTICIPANTS + "1")
                .then()
                .statusCode(401);
    }

    @Test
    public void testGetParticipantImage_return200() {
        given()
                .when()
                .get(API_PARTICIPANTS + "1/image")
                .then()
                .statusCode(200)
                .header("Content-Type", containsString("image/jpeg"));
    }

    @Test
    public void testGetParticipantImage_return404() {
        given()
                .when()
                .get(API_PARTICIPANTS + "999/image")
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteParticipantImage_return204() {
        given()
                .header("Cookie", getAdminCookie())
                .when()
                .delete(API_PARTICIPANTS + "1/image")
                .then()
                .statusCode(204);
    }

    @Test
    public void testDeleteParticipantImageUserRole_return403() {
        given()
                .header("Cookie", getUserCookie())
                .when()
                .delete(API_PARTICIPANTS + "1/image")
                .then()
                .statusCode(403);
    }

    @Test
    public void testDeleteParticipantImageNoAuth_return401() {
        given()
                .when()
                .delete(API_PARTICIPANTS + "1/image")
                .then()
                .statusCode(401);
    }

    @Test
    public void testDeleteParticipantImage_return404() {
        given()
                .header("Cookie", getAdminCookie())
                .when()
                .delete(API_PARTICIPANTS + "999/image")
                .then()
                .statusCode(404);
    }

    @Test
    public void testCreateParticipantImage_return201() {
        given()
                .header("Cookie", getAdminCookie())
                .multiPart("imageFile", new File(PARTICIPANT_IMAGE_PATH))
                .when()
                .post(API_PARTICIPANTS + "1/image")
                .then()
                .statusCode(201);
    }

    @Test
    public void testCreateParticipantImageUserRole_return403() {
        given()
                .header("Cookie", getUserCookie())
                .multiPart("imageFile", new File(PARTICIPANT_IMAGE_PATH))
                .when()
                .post(API_PARTICIPANTS + "1/image")
                .then()
                .statusCode(403);
    }

    @Test
    public void testCreateParticipantImageNoAuth_return401() {
        given()
                .multiPart("imageFile", new File(PARTICIPANT_IMAGE_PATH))
                .when()
                .post(API_PARTICIPANTS + "1/image")
                .then()
                .statusCode(401);
    }

    @Test
    public void testCreateParticipantImage_return404() {
        given()
                .header("Cookie", getAdminCookie())
                .multiPart("imageFile", new File(PARTICIPANT_IMAGE_PATH))
                .when()
                .post(API_PARTICIPANTS + "999/image")
                .then()
                .statusCode(404);
    }

    @Test
    public void testReplaceParticipantImage_return204() {
        given()
                .header("Cookie", getAdminCookie())
                .multiPart("imageFile", new File(PARTICIPANT_IMAGE_PATH))
                .when()
                .put(API_PARTICIPANTS + "1/image")
                .then()
                .statusCode(204);
    }

    @Test
    public void testReplaceParticipantImageUserRole_return403() {
        given()
                .header("Cookie", getUserCookie())
                .multiPart("imageFile", new File(PARTICIPANT_IMAGE_PATH))
                .when()
                .put(API_PARTICIPANTS + "1/image")
                .then()
                .statusCode(403);
    }

    @Test
    public void testReplaceParticipantImageNoAuth_return401() {
        given()
                .multiPart("imageFile", new File(PARTICIPANT_IMAGE_PATH))
                .when()
                .put(API_PARTICIPANTS + "1/image")
                .then()
                .statusCode(401);
    }

    @Test
    public void testReplaceParticipantImage_return404() {
        given()
                .header("Cookie", getAdminCookie())
                .multiPart("imageFile", new File(PARTICIPANT_IMAGE_PATH))
                .when()
                .put(API_PARTICIPANTS + "999/image")
                .then()
                .statusCode(404);
    }
}
