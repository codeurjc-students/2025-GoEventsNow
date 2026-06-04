package es.goeventsnow.backend.api;

import java.io.File;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class UserApiTest extends BaseApiTest {

    private static final String API_USERS = "/api/v1/users/";
    private static final String USER_IMAGE_PATH = "src/main/resources/static/images/participants/badbunny_participant.jpg";

    @BeforeEach
    public void setUp() {
        setUpRestAssured();
    }

    private int getCurrentUserId(String cookieHeader) {
        return given()
                .header("Cookie", cookieHeader)
                .when()
                .get(API_USERS + "me")
                .then()
                .statusCode(200)
                .extract()
                .path("id");
    }

    @Test
    public void testGetCurrentUser_return200() {
        given()
                .header("Cookie", getUserCookie())
                .when()
                .get(API_USERS + "me")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("username", is("user"));
    }

    @Test
    public void testGetUserById_return200() {
        String cookie = getUserCookie();
        int userId = getCurrentUserId(cookie);

        given()
                .header("Cookie", cookie)
                .when()
                .get(API_USERS + userId)
                .then()
                .statusCode(200)
                .body("id", is(userId))
                .body("username", is("user"));
    }

    @Test
    public void testGetCurrentUserNoAuth_return401() {
        given()
                .when()
                .get(API_USERS + "me")
                .then()
                .statusCode(401);
    }

    @Test
    public void testUserExists_returnTrue() {
        given()
                .when()
                .get(API_USERS + "exists?username=user")
                .then()
                .statusCode(200)
                .body(is("true"));
    }

    @Test
    public void testUserExists_returnFalse() {
        given()
                .when()
                .get(API_USERS + "exists?username=nonexistentuser")
                .then()
                .statusCode(200)
                .body(is("false"));
    }

    @Test
    public void testReplaceUser_return200() {
        String cookie = getUserCookie();
        int userId = getCurrentUserId(cookie);

        String updatedUser = """
                {
                    "fullname": "Updated User",
                    "phone": "123456879",
                    "email": "updated@email.com"
                }
                """;

        given()
                .header("Cookie", cookie)
                .contentType("application/json")
                .body(updatedUser)
                .when()
                .put(API_USERS + userId)
                .then()
                .statusCode(200)
                .body("fullname", is("Updated User"))
                .body("phone", is(123456879))
                .body("email", is("updated@email.com"));
    }

    @Test
    public void testReplaceUserNoAuth_return401() {
        String updatedUser = """
                {
                    "fullname": "No Auth User",
                    "phone": "123456789",
                    "email": "noauth@email.com"
                }
                """;

        given()
                .contentType("application/json")
                .body(updatedUser)
                .when()
                .put(API_USERS + "1")
                .then()
                .statusCode(401);
    }

    @Test
    public void testReplaceOtherUser_return403() {
        String userCookie = getUserCookie();
        String adminCookie = getAdminCookie();

        int adminId = getCurrentUserId(adminCookie);

        String updatedUser = """
                {
                    "fullname": "Forbidden Update",
                    "phone": "123456789",
                    "email": "forbidden@email.com"
                }
                """;

        given()
                .header("Cookie", userCookie)
                .contentType("application/json")
                .body(updatedUser)
                .when()
                .put(API_USERS + adminId)
                .then()
                .statusCode(403);
    }

    @Test
    public void testGetUserImage_return200() {
        String cookie = getUserCookie();
        int userId = getCurrentUserId(cookie);

        given()
                .header("Cookie", cookie)
                .multiPart("imageFile", new File(USER_IMAGE_PATH))
                .when()
                .post(API_USERS + userId + "/image")
                .then()
                .statusCode(201);

        given()
                .header("Cookie", cookie)
                .when()
                .get(API_USERS + userId + "/image")
                .then()
                .statusCode(200)
                .header("Content-Type", containsString("image/jpeg"));
    }

    @Test
    public void testCreateUserImage_return201() {
        String cookie = getUserCookie();
        int userId = getCurrentUserId(cookie);

        given()
                .header("Cookie", cookie)
                .multiPart("imageFile", new File(USER_IMAGE_PATH))
                .when()
                .post(API_USERS + userId + "/image")
                .then()
                .statusCode(201);
    }

    @Test
    public void testCreateUserImageNoAuth_return401() {
        given()
                .multiPart("imageFile", new File(USER_IMAGE_PATH))
                .when()
                .post(API_USERS + "1/image")
                .then()
                .statusCode(401);
    }

    @Test
    public void testCreateOtherUserImage_return403() {
        String userCookie = getUserCookie();
        String adminCookie = getAdminCookie();

        int adminId = getCurrentUserId(adminCookie);

        given()
                .header("Cookie", userCookie)
                .multiPart("imageFile", new File(USER_IMAGE_PATH))
                .when()
                .post(API_USERS + adminId + "/image")
                .then()
                .statusCode(403);
    }

    @Test
    public void testReplaceUserImage_return204() {
        String cookie = getUserCookie();
        int userId = getCurrentUserId(cookie);

        given()
                .header("Cookie", cookie)
                .multiPart("imageFile", new File(USER_IMAGE_PATH))
                .when()
                .post(API_USERS + userId + "/image")
                .then()
                .statusCode(201);

        given()
                .header("Cookie", cookie)
                .multiPart("imageFile", new File(USER_IMAGE_PATH))
                .when()
                .put(API_USERS + userId + "/image")
                .then()
                .statusCode(204);
    }

    @Test
    public void testReplaceUserImageNoAuth_return401() {
        given()
                .multiPart("imageFile", new File(USER_IMAGE_PATH))
                .when()
                .put(API_USERS + "1/image")
                .then()
                .statusCode(401);
    }

    @Test
    public void testReplaceOtherUserImage_return403() {
        String userCookie = getUserCookie();
        String adminCookie = getAdminCookie();

        int adminId = getCurrentUserId(adminCookie);

        given()
                .header("Cookie", userCookie)
                .multiPart("imageFile", new File(USER_IMAGE_PATH))
                .when()
                .put(API_USERS + adminId + "/image")
                .then()
                .statusCode(403);
    }

    @Test
    public void testDeleteUserImage_return204() {
        String cookie = getUserCookie();
        int userId = getCurrentUserId(cookie);

        given()
                .header("Cookie", cookie)
                .multiPart("imageFile", new File(USER_IMAGE_PATH))
                .when()
                .post(API_USERS + userId + "/image")
                .then()
                .statusCode(201);

        given()
                .header("Cookie", cookie)
                .when()
                .delete(API_USERS + userId + "/image")
                .then()
                .statusCode(204);
    }

    @Test
    public void testDeleteUserImageNoAuth_return401() {
        given()
                .when()
                .delete(API_USERS + "1/image")
                .then()
                .statusCode(401);
    }

    @Test
    public void testDeleteOtherUserImage_return403() {
        String userCookie = getUserCookie();
        String adminCookie = getAdminCookie();

        int adminId = getCurrentUserId(adminCookie);

        given()
                .header("Cookie", userCookie)
                .when()
                .delete(API_USERS + adminId + "/image")
                .then()
                .statusCode(403);
    }

    @Test
    public void getUserFollowingParticipantsEmpty_return200() {
        String adminCookie = getAdminCookie();
        int adminId = getCurrentUserId(adminCookie);

        given()
                .header("Cookie", adminCookie)
                .when()
                .get(API_USERS + adminId + "/following")
                .then()
                .statusCode(200)
                .body("content", hasSize(0))
                .body("page.size", is(20))
                .body("page.number", is(0))
                .body("page.totalElements", is(0))
                .body("page.totalPages", is(0));
    }

    @Test
    public void getUserFavoritesEventsEmpty_return200() {
        String adminCookie = getAdminCookie();
        int adminId = getCurrentUserId(adminCookie);

        given()
                .header("Cookie", adminCookie)
                .when()
                .get(API_USERS + adminId + "/favorites")
                .then()
                .statusCode(200)
                .body("content", hasSize(0))
                .body("page.size", is(20))
                .body("page.number", is(0))
                .body("page.totalElements", is(0))
                .body("page.totalPages", is(0));
    }

    @Test
    public void testAddFavoriteEvent_return200() {
        String adminCookie = getAdminCookie();
        int adminId = getCurrentUserId(adminCookie);

        addFavoriteEvent(adminCookie, adminId);
    }

    @Test
    public void testAddFavoriteEventAlreadyAdded_return400() {
        String adminCookie = getAdminCookie();
        int adminId = getCurrentUserId(adminCookie);

        given()
                .header("Cookie", adminCookie)
                .when()
                .post(API_USERS + adminId + "/favorites/1")
                .then()
                .statusCode(400);
    }

    @Test
    public void testAddFollowingParticipant_return200() {
        String adminCookie = getAdminCookie();
        int adminId = getCurrentUserId(adminCookie);

        addFollowingParticipant(adminCookie, adminId);
    }

    @Test
    public void testAddFollowingParticipantAlreadyAdded_return400() {
        String adminCookie = getAdminCookie();
        int adminId = getCurrentUserId(adminCookie);

        given()
                .header("Cookie", adminCookie)
                .when()
                .post(API_USERS + adminId + "/following/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testDeleteFavoriteEvent_return200() {
        String adminCookie = getAdminCookie();
        int adminId = getCurrentUserId(adminCookie);

        addFavoriteEvent(adminCookie, adminId);
        given()
                .header("Cookie", adminCookie)
                .when()
                .delete(API_USERS + adminId + "/favorites/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testDeleteFavoriteEventNotContained_return400() {
        String adminCookie = getAdminCookie();
        int adminId = getCurrentUserId(adminCookie);

        given()
                .header("Cookie", adminCookie)
                .when()
                .delete(API_USERS + adminId + "/favorites/1")
                .then()
                .statusCode(400);
    }

    @Test
    public void testDeleteFollowingParticipant_return200() {
        String adminCookie = getAdminCookie();
        int adminId = getCurrentUserId(adminCookie);

        addFollowingParticipant(adminCookie, adminId);
        given()
                .header("Cookie", adminCookie)
                .when()
                .delete(API_USERS + adminId + "/following/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testDeleteFollowingParticipantNotContained_return400() {
        String adminCookie = getAdminCookie();
        int adminId = getCurrentUserId(adminCookie);

        given()
                .header("Cookie", adminCookie)
                .when()
                .delete(API_USERS + adminId + "/following/1")
                .then()
                .statusCode(400);
    }

    @Test
    public void getUserFavoritesEvents_return200() {
        String adminCookie = getAdminCookie();
        int adminId = getCurrentUserId(adminCookie);

        addFavoriteEvent(adminCookie, adminId);

        given()
                .header("Cookie", adminCookie)
                .when()
                .get(API_USERS + adminId + "/favorites")
                .then()
                .statusCode(200)
                .body("content[0].id", is(1));
    }

    @Test
    public void getUserFollowingParticipants_return200() {
        String adminCookie = getAdminCookie();
        int adminId = getCurrentUserId(adminCookie);

        addFollowingParticipant(adminCookie, adminId);

        given()
                .header("Cookie", adminCookie)
                .when()
                .get(API_USERS + adminId + "/following")
                .then()
                .statusCode(200)
                .body("content[0].id", is(1));
    }

    private void addFollowingParticipant(String adminCookie, int adminId) {
        given()
                .header("Cookie", adminCookie)
                .when()
                .post(API_USERS + adminId + "/following/1")
                .then()
                .statusCode(200);
    }

    private void addFavoriteEvent(String adminCookie, int adminId) {
        given()
                .header("Cookie", adminCookie)
                .when()
                .post(API_USERS + adminId + "/favorites/1")
                .then()
                .statusCode(200);
    }
}
