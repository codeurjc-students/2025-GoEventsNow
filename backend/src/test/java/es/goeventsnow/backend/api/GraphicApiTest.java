package es.goeventsnow.backend.api;

import static org.hamcrest.Matchers.hasKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class GraphicApiTest extends BaseApiTest {

    private static final String API_GRAPHIC = "/api/v1/graphics/";

    @BeforeEach
    public void setUp() {
        setUpRestAssured();
    }

    @Test
    public void testGetTicketsSoldByEvent() {
        given()
                .header("Cookie", getAdminCookie())
                .when()
                .get(API_GRAPHIC + "bargraph")
                .then()
                .statusCode(200)
                .body("$", hasKey("labels"))
                .body("$", hasKey("data"))
                .body("$", hasKey("backgroundColor"));
    }

    @Test
    public void testGetTicketsSoldByCategory() {
        given()
                .header("Cookie", getAdminCookie())
                .when()
                .get(API_GRAPHIC + "piechart")
                .then()
                .statusCode(200)
                .body("$", hasKey("labels"))
                .body("$", hasKey("data"))
                .body("$", hasKey("backgroundColor"));
    }

    @Test
    public void testGetTicketsNotAuthSoldByEvent() {
        given()
                .when()
                .get(API_GRAPHIC + "bargraph")
                .then()
                .statusCode(401);
    }

    @Test
    public void testGetTicketsNotAuthSoldByCategory() {
        given()
                .when()
                .get(API_GRAPHIC + "piechart")
                .then()
                .statusCode(401);
    }
    
}
