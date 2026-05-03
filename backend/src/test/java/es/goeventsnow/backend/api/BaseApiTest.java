package es.goeventsnow.backend.api;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class BaseApiTest {

    protected static final String API_AUTH_LOGIN = "/api/v1/auth/login";
    
    @LocalServerPort
    protected int port;

    protected void setUpRestAssured() {
        RestAssured.port = port;
        RestAssured.baseURI = "https://localhost";
        RestAssured.useRelaxedHTTPSValidation();
    }

    protected String getAdminToken() {
        String loginRequest = """
                {
                    "username": "admin",
                    "password": "adminpass"
                }
                """;

        return given()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post(API_AUTH_LOGIN)
                .then()
                .statusCode(200)
                .extract()
                .cookie("AuthToken");
    }

    protected String getUserToken() {
        String loginRequest = """
                {
                    "username": "user",
                    "password": "pass"
                }
                """;

        return given()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post(API_AUTH_LOGIN)
                .then()
                .statusCode(200)
                .extract()
                .cookie("AuthToken");
    }

    protected String getAdminCookie() {
        return "AuthToken=" + getAdminToken();
    }

    protected String getUserCookie() {
        return "AuthToken=" + getUserToken();
    }
}
