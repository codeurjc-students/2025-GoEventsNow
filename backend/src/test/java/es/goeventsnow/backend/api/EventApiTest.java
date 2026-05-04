package es.goeventsnow.backend.api;

import java.io.File;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class EventApiTest extends BaseApiTest {

    private static final String API_EVENTS = "/api/v1/events/";
    private static final String EVENT_IMAGE_PATH = "src/main/resources/static/images/events/latinMusicFestival_event.jpg";

    @BeforeEach
    public void setUp() {
        setUpRestAssured();
    }

    @Test
    public void testGetAllEvents_return200() {

        given()
                .contentType("application/json")
                .when()
                .get(API_EVENTS)
                .then()
                .statusCode(200)
                .log().all()
                .body("content[0].title", is("Global Latin Music Festival"));
    }

    @Test
    public void testGetEventById_return200() {

        given()
                .contentType("application/json")
                .when()
                .get(API_EVENTS + "1")
                .then()
                .statusCode(200)
                .log().all()
                .body("title", is("Global Latin Music Festival"))
                .body("category", is("Music"))
                .body("location", is("Madrid, WiZink Center"))
                .body("participants[0].name", is("Bad Bunny"));
    }

    @Test
    public void testGetEventById_return404() {

        given()
                .contentType("application/json")
                .when()
                .get(API_EVENTS + "999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetEventById_return400() {

        given()
                .contentType("application/json")
                .when()
                .get(API_EVENTS + "notNumber")
                .then()
                .statusCode(400)
                .log().all();
    }

    @Test
    public void testGetEventsByParticipantId_return200() {

        given()
                .contentType("application/json")
                .when()
                .get(API_EVENTS + "?participantId=1")
                .then()
                .statusCode(200)
                .log().all()
                .body("content[0].title", is("Global Latin Music Festival"))
                .body("content[0].participants[0].id", is(1));
    }

    @Test
    public void testGetEventsByParticipantId_return404() {

        given()
                .contentType("application/json")
                .when()
                .get(API_EVENTS + "?participantId=999")
                .then()
                .statusCode(404)
                .log().all();
    }

    @Test
    public void testGetEventsByParticipantId_return400() {

        given()
                .contentType("application/json")
                .when()
                .get(API_EVENTS + "?participantId=notNumber")
                .then()
                .statusCode(400)
                .log().all();
    }

    @Test
    public void testCreateEventNoAuth_return401() {

        String newEvent = """
                {
                    "title": "New No Auth Java Event",
                    "description": "No auth Java backend conference",
                    "category": "Technology",
                    "location": "Madrid",
                    "date": "2026-06-20",
                    "time": "18:00",
                    "basicPrice": 30.0,
                    "vipPrice": 80.0,
                    "availableBasicTickets": 150,
                    "availableVipTickets": 30,
                    "image": false,
                    "participants": [
                        {
                            "id": 1,
                            "name": "Bad Bunny",
                            "type": "Music Artist",
                            "biography": "Puerto Rican global superstar known for redefining reggaeton and Latin trap, headlining major international festivals and sold-out world tours.",
                            "participantImage": true
                        }
                    ],
                    "tickets": []
                }
                """;

        given()
                .contentType("application/json")
                .body(newEvent)
                .when()
                .post(API_EVENTS)
                .then()
                .statusCode(401);
    }

    @Test
    public void testCreateEventUserRole_return403() {

        String newEvent = """
                {
                    "title": "New USER Java Event",
                    "description": "USER role should not create events",
                    "category": "Technology",
                    "location": "Madrid",
                    "date": "2026-06-20",
                    "time": "18:00",
                    "basicPrice": 30.0,
                    "vipPrice": 80.0,
                    "availableBasicTickets": 150,
                    "availableVipTickets": 30,
                    "image": false,
                    "participants": [
                        {
                            "id": 1,
                            "name": "Bad Bunny",
                            "type": "Music Artist",
                            "biography": "Puerto Rican global superstar known for redefining reggaeton and Latin trap, headlining major international festivals and sold-out world tours.",
                            "participantImage": true
                        }
                    ],
                    "tickets": []
                }
                """;

        given()
                .header("Cookie", getUserCookie())
                .contentType("application/json")
                .body(newEvent)
                .when()
                .post(API_EVENTS)
                .then()
                .statusCode(403);
    }

    @Test
    public void testCreateEvent_return201() {

        String newEvent = """
                {
                    "title": "New Java Event",
                    "description": "Java backend conference",
                    "category": "Technology",
                    "location": "Madrid",
                    "date": "2026-06-20",
                    "time": "18:00",
                    "basicPrice": 30.0,
                    "vipPrice": 80.0,
                    "availableBasicTickets": 150,
                    "availableVipTickets": 30,
                    "image": false,
                    "participants": [
                        {
                            "id": 1,
                            "name": "Bad Bunny",
                            "type": "Music Artist",
                            "biography": "Puerto Rican global superstar known for redefining reggaeton and Latin trap, headlining major international festivals and sold-out world tours.",
                            "participantImage": true
                        }
                    ],
                    "tickets": []
                }
                """;

        given()
                .header("Cookie", getAdminCookie())
                .contentType("application/json")
                .body(newEvent)
                .when()
                .post(API_EVENTS)
                .then()
                .statusCode(201)
                .header("Location", containsString(API_EVENTS))
                .body("id", notNullValue())
                .body("title", is("New Java Event"))
                .body("location", is("Madrid"));
    }

    @Test
    public void testCreateEventWithInvalidParticipant_return400() {

        String newEvent = """
                {
                    "title": "Invalid Participant Event",
                    "description": "Event with invalid participant",
                    "category": "Technology",
                    "location": "Madrid",
                    "date": "2026-06-20",
                    "time": "18:00",
                    "basicPrice": 30.0,
                    "vipPrice": 80.0,
                    "availableBasicTickets": 150,
                    "availableVipTickets": 30,
                    "image": false,
                    "participants": [
                        {
                            "id": 999,
                            "name": "Unknown",
                            "type": "Music",
                            "biography": "Unknown",
                            "participantImage": false
                        }
                    ],
                    "tickets": []
                }
                """;

        given()
                .header("Cookie", getAdminCookie())
                .contentType("application/json")
                .body(newEvent)
                .when()
                .post(API_EVENTS)
                .then()
                .statusCode(400);
    }

    @Test
    public void testReplaceEvent_return200() {

        String updatedEvent = """
                {
                    "title": "Updated Spring Boot Workshop",
                    "description": "Updated description",
                    "category": "Technology",
                    "location": "Madrid Centro",
                    "date": "2026-04-10",
                    "time": "12:00",
                    "basicPrice": 60.0,
                    "vipPrice": 140.0,
                    "availableBasicTickets": 80,
                    "availableVipTickets": 15,
                    "image": true,
                    "participants": [
                        {
                            "id": 1,
                            "name": "Bad Bunny",
                            "type": "Music Artist",
                            "biography": "Puerto Rican global superstar known for redefining reggaeton and Latin trap, headlining major international festivals and sold-out world tours.",
                            "participantImage": true
                        }
                    ],
                    "tickets": []
                }
                """;

        given()
                .header("Cookie", getAdminCookie())
                .contentType("application/json")
                .body(updatedEvent)
                .when()
                .put(API_EVENTS + "1")
                .then()
                .statusCode(200)
                .body("title", is("Updated Spring Boot Workshop"))
                .body("location", is("Madrid Centro"))
                .body("basicPrice", is(60.0f));
    }

    @Test
    public void testReplaceEventNoAuth_return401() {

        String updatedEvent = """
                {
                    "title": "Updated No Auth Spring Boot Workshop",
                    "description": "Updated No Auth description",
                    "category": "Technology",
                    "location": "Madrid Centro",
                    "date": "2026-04-10",
                    "time": "12:00",
                    "basicPrice": 60.0,
                    "vipPrice": 140.0,
                    "availableBasicTickets": 80,
                    "availableVipTickets": 15,
                    "image": true,
                    "participants": [
                        {
                            "id": 1,
                            "name": "Bad Bunny",
                            "type": "Music Artist",
                            "biography": "Puerto Rican global superstar known for redefining reggaeton and Latin trap, headlining major international festivals and sold-out world tours.",
                            "participantImage": true
                        }
                    ],
                    "tickets": []
                }
                """;

        given()
                .contentType("application/json")
                .body(updatedEvent)
                .when()
                .put(API_EVENTS + "1")
                .then()
                .statusCode(401);
    }

    @Test
    public void testReplaceEventUserRole_return403() {

        String updatedEvent = """
                {
                    "title": "Updated USER Spring Boot Workshop",
                    "description": "USER role should not update events",
                    "category": "Technology",
                    "location": "Madrid Centro",
                    "date": "2026-04-10",
                    "time": "12:00",
                    "basicPrice": 60.0,
                    "vipPrice": 140.0,
                    "availableBasicTickets": 80,
                    "availableVipTickets": 15,
                    "image": true,
                    "participants": [
                        {
                            "id": 1,
                            "name": "Bad Bunny",
                            "type": "Music Artist",
                            "biography": "Puerto Rican global superstar known for redefining reggaeton and Latin trap, headlining major international festivals and sold-out world tours.",
                            "participantImage": true
                        }
                    ],
                    "tickets": []
                }
                """;

        given()
                .header("Cookie", getUserCookie())
                .contentType("application/json")
                .body(updatedEvent)
                .when()
                .put(API_EVENTS + "1")
                .then()
                .statusCode(403);
    }

    @Test
    public void testReplaceEvent_return404() {

        String updatedEvent = """
                {
                    "title": "Updated Event",
                    "description": "Updated description",
                    "category": "Technology",
                    "location": "Madrid",
                    "date": "2026-04-10",
                    "time": "12:00",
                    "basicPrice": 60.0,
                    "vipPrice": 140.0,
                    "availableBasicTickets": 80,
                    "availableVipTickets": 15,
                    "image": false,
                    "participants": [],
                    "tickets": []
                }
                """;

        given()
                .header("Cookie", getAdminCookie())
                .contentType("application/json")
                .body(updatedEvent)
                .when()
                .put(API_EVENTS + "999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testReplaceEvent_return400() {

        String updatedEvent = """
                {
                    "title": "Updated Spring Boot Workshop",
                    "description": "Updated description",
                    "category": "Technology",
                    "location": "Madrid Centro",
                    "date": "2026-04-10",
                    "time": "12:00",
                    "basicPrice": 60.0,
                    "vipPrice": 140.0,
                    "availableBasicTickets": 80,
                    "availableVipTickets": 15,
                    "image": true,
                    "participants": [
                        {
                            "id": 1111,
                            "name": "Bad Bunny",
                            "type": "Music Artist",
                            "biography": "Puerto Rican global superstar known for redefining reggaeton and Latin trap, headlining major international festivals and sold-out world tours.",
                            "participantImage": true
                        }
                    ],
                    "tickets": []
                }
                """;

        given()
                .header("Cookie", getAdminCookie())
                .contentType("application/json")
                .body(updatedEvent)
                .when()
                .put(API_EVENTS + "1")
                .then()
                .statusCode(400);
    }

    @Test
    public void testDeleteEvent_return200() {

        String newEvent = """
                {
                    "title": "Event To Delete",
                    "description": "Temporary event",
                    "category": "Technology",
                    "location": "Madrid",
                    "date": "2026-07-01",
                    "time": "19:00",
                    "basicPrice": 20.0,
                    "vipPrice": 50.0,
                    "availableBasicTickets": 100,
                    "availableVipTickets": 10,
                    "image": false,
                    "participants": [
                        {
                            "id": 1,
                            "name": "Bad Bunny",
                            "type": "Music Artist",
                            "biography": "Puerto Rican global superstar known for redefining reggaeton and Latin trap, headlining major international festivals and sold-out world tours.",
                            "participantImage": true
                        }
                    ],
                    "tickets": []
                }
                """;

        int eventId = given()
                .header("Cookie", getAdminCookie())
                .contentType("application/json")
                .body(newEvent)
                .when()
                .post(API_EVENTS)
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .header("Cookie", getAdminCookie())
                .contentType("application/json")
                .when()
                .delete(API_EVENTS + eventId)
                .then()
                .statusCode(200)
                .body("title", is("Event To Delete"));

        given()
                .contentType("application/json")
                .when()
                .get(API_EVENTS + eventId)
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteEvent_return404() {
        given()
                .header("Cookie", getAdminCookie())
                .contentType("application/json")
                .when()
                .delete(API_EVENTS + "999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteEventUserRole_return403() {
        given()
                .header("Cookie", getUserCookie())
                .contentType("application/json")
                .when()
                .delete(API_EVENTS + "1")
                .then()
                .statusCode(403);
    }

    @Test
    public void testDeleteEventNoAuth_return401() {
        given()
                .contentType("application/json")
                .when()
                .delete(API_EVENTS + "1")
                .then()
                .statusCode(401);
    }

    @Test
    public void testGetEventImage_return200() {
        given()
                .when()
                .get(API_EVENTS + "1/image")
                .then()
                .statusCode(200)
                .header("Content-Type", containsString("image/jpeg"));
    }

    @Test
    public void testGetEventImage_return404() {
        given()
                .when()
                .get(API_EVENTS + "999/image")
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteEventImage_return204() {
        given()
                .header("Cookie", getAdminCookie())
                .multiPart("imageFile", new File(EVENT_IMAGE_PATH))
                .when()
                .post(API_EVENTS + "1/image")
                .then()
                .statusCode(201);
        given()
                .header("Cookie", getAdminCookie())
                .when()
                .delete(API_EVENTS + "1/image")
                .then()
                .statusCode(204);
    }

    @Test
    public void testDeleteEventImageUserRole_return403() {
        given()
                .header("Cookie", getUserCookie())
                .when()
                .delete(API_EVENTS + "1/image")
                .then()
                .statusCode(403);
    }

    @Test
    public void testDeleteEventImageNoAuth_return401() {
        given()
                .when()
                .delete(API_EVENTS + "1/image")
                .then()
                .statusCode(401);
    }

    @Test
    public void testDeleteEventImage_return404() {
        given()
                .header("Cookie", getAdminCookie())
                .when()
                .delete(API_EVENTS + "999/image")
                .then()
                .statusCode(404);
    }

    @Test
    public void testCreateEventImage_return201() {

        given()
                .header("Cookie", getAdminCookie())
                .multiPart("imageFile", new File(EVENT_IMAGE_PATH))
                .when()
                .post(API_EVENTS + "1/image")
                .then()
                .statusCode(201);
    }

    @Test
    public void testCreateEventImageUserRole_return403() {

        given()
                .header("Cookie", getUserCookie())
                .multiPart("imageFile", new File(EVENT_IMAGE_PATH))
                .when()
                .post(API_EVENTS + "1/image")
                .then()
                .statusCode(403);
    }

    @Test
    public void testCreateEventImageNoAuth_return401() {

        given()
                .multiPart("imageFile", new File(EVENT_IMAGE_PATH))
                .when()
                .post(API_EVENTS + "1/image")
                .then()
                .statusCode(401);
    }

    @Test
    public void testCreateEventImage_return404() {

        given()
                .header("Cookie", getAdminCookie())
                .multiPart("imageFile", new File(EVENT_IMAGE_PATH))
                .when()
                .post(API_EVENTS + "999/image")
                .then()
                .statusCode(404);
    }

    @Test
    public void testReplaceEventImage_return201() {
        given()
                .header("Cookie", getAdminCookie())
                .multiPart("imageFile", new File(EVENT_IMAGE_PATH))
                .when()
                .post(API_EVENTS + "1/image")
                .then()
                .statusCode(201);

        given()
                .header("Cookie", getAdminCookie())
                .multiPart("imageFile", new File(EVENT_IMAGE_PATH))
                .when()
                .put(API_EVENTS + "1/image")
                .then()
                .statusCode(204);
    }

    @Test
    public void testReplaceEventImageUserRole_return403() {

        given()
                .header("Cookie", getUserCookie())
                .multiPart("imageFile", new File(EVENT_IMAGE_PATH))
                .when()
                .put(API_EVENTS + "1/image")
                .then()
                .statusCode(403);
    }

    @Test
    public void testReplaceEventImageNoAuth_return401() {

        given()
                .multiPart("imageFile", new File(EVENT_IMAGE_PATH))
                .when()
                .put(API_EVENTS + "1/image")
                .then()
                .statusCode(401);
    }

    @Test
    public void testReplaceEventImage_return404() {

        given()
                .header("Cookie", getAdminCookie())
                .multiPart("imageFile", new File(EVENT_IMAGE_PATH))
                .when()
                .put(API_EVENTS + "999/image")
                .then()
                .statusCode(404);
    }

}
