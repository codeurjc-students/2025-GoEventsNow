package es.goeventsnow.backend.api;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventApiTest {

    private static final String API_EVENTS = "/api/v1/events/";

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void eventsGetAllTest() {

        given()
            .contentType("application/json")
        .when()
            .get(API_EVENTS)
        .then()
            .statusCode(200)
            .log().all()
            .body("size()", is(3))
            .body("title", hasItems(
                "Taller de Spring Boot 4.0",
                "Exposición Arte",
                "Torneo de Baloncesto"
            ));
    }
}
