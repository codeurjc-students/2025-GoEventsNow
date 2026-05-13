package es.goeventsnow.backend.api;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class TicketApiTest extends BaseApiTest {

    private static final String API_TICKETS = "/api/v1/tickets/";

    @BeforeEach
    public void setUp() {
        setUpRestAssured();
    }

    @Test
    public void testGetAllTicketsNoAuth_return401() {
        given()
                .when()
                .get(API_TICKETS)
                .then()
                .statusCode(401);
    }

    @Test
    public void testGetAllTicketsUser_return200() {
        given()
                .header("Cookie", getUserCookie())
                .when()
                .get(API_TICKETS)
                .then()
                .statusCode(200)
                .body("content", notNullValue());
    }

    @Test
    public void testGetTicketByIdNoAuth_return401() {
        given()
                .when()
                .get(API_TICKETS + "1")
                .then()
                .statusCode(401);
    }

    @Test
    public void testGetTicketById_return404() {
        given()
                .header("Cookie", getUserCookie())
                .when()
                .get(API_TICKETS + "999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetTicketById_return400() {
        given()
                .header("Cookie", getUserCookie())
                .when()
                .get(API_TICKETS + "notNumber")
                .then()
                .statusCode(400);
    }

    @Test
    public void testGetTicketById_return200() {
        String ticket = """
                {
                    "ticketType": "BASIC",
                    "price": 50.0,
                    "numTickets": 1,
                    "eventId": 1,
                    "userOwnerId": 1
                }
                """;

        int createdTicketId = given()
                .header("Cookie", getUserCookie())
                .contentType("application/json")
                .body(ticket)
                .when()
                .post(API_TICKETS)
                .then()
                .statusCode(201)
                .header("Location", containsString(API_TICKETS))
                .body("id", notNullValue())
                .extract()
                .path("id");

        given()
                .header("Cookie", getUserCookie())
                .when()
                .get(API_TICKETS + createdTicketId)
                .then()
                .statusCode(200)
                .body("id", is(createdTicketId))
                .body("ticketType", is("BASIC"));
    }

    @Test
    public void testCreateTicketNoAuth_return401() {
        String ticket = """
                {
                    "ticketType": "BASIC",
                    "price": 50.0,
                    "numTickets": 1,
                    "eventId": 1,
                    "userOwnerId": 1
                }
                """;

        given()
                .contentType("application/json")
                .body(ticket)
                .when()
                .post(API_TICKETS)
                .then()
                .statusCode(401);
    }

    @Test
    public void testCreateBasicTicketUser_return201() {
        String ticket = """
                {
                    "ticketType": "BASIC",
                    "price": 50.0,
                    "numTickets": 1,
                    "eventId": 1,
                    "userOwnerId": 1
                }
                """;

        given()
                .header("Cookie", getUserCookie())
                .contentType("application/json")
                .body(ticket)
                .when()
                .post(API_TICKETS)
                .then()
                .statusCode(201)
                .header("Location", containsString(API_TICKETS))
                .body("id", notNullValue())
                .body("ticketType", is("BASIC"))
                .body("numTickets", is(1));
    }

    @Test
    public void testCreateVipTicketUser_return201() {
        String ticket = """
                {
                    "ticketType": "VIP",
                    "price": 120.0,
                    "numTickets": 1,
                    "eventId": 1,
                    "userOwnerId": 1
                }
                """;

        given()
                .header("Cookie", getUserCookie())
                .contentType("application/json")
                .body(ticket)
                .when()
                .post(API_TICKETS)
                .then()
                .statusCode(201)
                .body("ticketType", is("VIP"));
    }

    @Test
    public void testCreateTicketInvalidEvent_return404() {
        String ticket = """
                {
                    "ticketType": "BASIC",
                    "price": 50.0,
                    "numTickets": 1,
                    "eventId": 999,
                    "userOwnerId": 1
                }
                """;

        given()
                .header("Cookie", getUserCookie())
                .contentType("application/json")
                .body(ticket)
                .when()
                .post(API_TICKETS)
                .then()
                .statusCode(404);
    }

    @Test
    public void testCreateTicketInvalidType_return400() {
        String ticket = """
                {
                    "ticketType": "GOLD",
                    "price": 50.0,
                    "numTickets": 1,
                    "eventId": 1,
                    "userOwnerId": 1
                }
                """;

        given()
                .header("Cookie", getUserCookie())
                .contentType("application/json")
                .body(ticket)
                .when()
                .post(API_TICKETS)
                .then()
                .statusCode(400);
    }

    @Test
    public void testCreateBasicTicketNotEnoughTickets_return400() {
        String ticket = """
                {
                    "ticketType": "BASIC",
                    "price": 50.0,
                    "numTickets": 99999,
                    "eventId": 1,
                    "userOwnerId": 1
                }
                """;

        given()
                .header("Cookie", getUserCookie())
                .contentType("application/json")
                .body(ticket)
                .when()
                .post(API_TICKETS)
                .then()
                .statusCode(400);
    }

    @Test
    public void testCreateVipTicketNotEnoughTickets_return400() {
        String ticket = """
                {
                    "ticketType": "VIP",
                    "price": 120.0,
                    "numTickets": 99999,
                    "eventId": 1,
                    "userOwnerId": 1
                }
                """;

        given()
                .header("Cookie", getUserCookie())
                .contentType("application/json")
                .body(ticket)
                .when()
                .post(API_TICKETS)
                .then()
                .statusCode(400);
    }
}
