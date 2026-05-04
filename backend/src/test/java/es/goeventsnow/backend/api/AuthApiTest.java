package es.goeventsnow.backend.api;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class AuthApiTest extends BaseApiTest {

    private static final String API_AUTH = "/api/v1/auth/";

    @BeforeEach
    public void setUp() {
        setUpRestAssured();
    }

    @Test
    public void testLogin_return200() {
        String loginRequest = """
                {
                    "username": "user",
                    "password": "pass"
                }
                """;

        given()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post(API_AUTH + "login")
                .then()
                .statusCode(200)
                .cookie("AuthToken", notNullValue())
                .cookie("RefreshToken", notNullValue())
                .body("status", is("SUCCESS"));
    }

    @Test
    public void testLoginWrongPassword_return401() {
        String loginRequest = """
                {
                    "username": "user",
                    "password": "wrongpass"
                }
                """;

        given()
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post(API_AUTH + "login")
                .then()
                .statusCode(401);
    }

    @Test
    public void testRefreshToken_return200() {
        String loginRequest = """
                {
                    "username": "user",
                    "password": "pass"
                }
                """;

        String refreshToken =
                given()
                        .contentType("application/json")
                        .body(loginRequest)
                        .when()
                        .post(API_AUTH + "login")
                        .then()
                        .statusCode(200)
                        .extract()
                        .cookie("RefreshToken");

        given()
                .cookie("RefreshToken", refreshToken)
                .when()
                .post(API_AUTH + "refresh")
                .then()
                .statusCode(200)
                .cookie("AuthToken", notNullValue())
                .body("status", is("SUCCESS"));
    }

    @Test
    public void testRefreshTokenNoCookie_return401() {
        given()
                .when()
                .post(API_AUTH + "refresh")
                .then()
                .statusCode(401)
                .body("status", is("FAILURE"));
    }

    @Test
    public void testLogout_return200() {
        given()
                .when()
                .post(API_AUTH + "logout")
                .then()
                .statusCode(200)
                .body("status", is("SUCCESS"))
                .body("message", is("logout successfully"));
    }

    @Test
    public void testRegister_return201() {
        given()
                .multiPart("username", "newuser")
                .multiPart("fullname", "New User")
                .multiPart("email", "newuser@email.com")
                .multiPart("password", "newpass")
                .multiPart("phone", "666777888")
                .when()
                .post(API_AUTH + "register")
                .then()
                .statusCode(201)
                .header("Location", containsString("/api/v1/users/"))
                .body("status", is("SUCCESS"))
                .body("message", is("User registered"));
    }

    @Test
    public void testRegisterInvalidPhone_return400() {
        given()
                .multiPart("username", "phoneuser")
                .multiPart("fullname", "Phone User")
                .multiPart("email", "phoneuser@email.com")
                .multiPart("password", "pass")
                .multiPart("phone", "notNumber")
                .when()
                .post(API_AUTH + "register")
                .then()
                .statusCode(400)
                .body("status", is("ERROR"));
    }
}
