package es.goeventsnow.backend.api;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class ReviewApiTest extends BaseApiTest {

    private static final String API_REVIEWS = "/api/v1/reviews/";

    @BeforeEach
    public void setUp() {
        setUpRestAssured();
    }

    private int createReviewAndGetId(String cookie, long eventId, String description, double rating) {
        String review = """
                {
                    "description": "%s",
                    "rating": %s,
                    "eventAssociatedId": %d,
                    "userOwnerId": 1,
                    "createdAt": null
                }
                """.formatted(description, rating, eventId);

        return given()
                .header("Cookie", cookie)
                .contentType("application/json")
                .body(review)
                .when()
                .post(API_REVIEWS + "event/" + eventId)
                .then()
                .statusCode(201)
                .header("Location", containsString(API_REVIEWS + "event/" + eventId))
                .body("id", notNullValue())
                .body("description", is(description))
                .body("rating", is((float) rating))
                .extract()
                .path("id");
    }

    @Test
    public void testReviewLifecycle_return200() {
        String userCookie = getUserCookie();
        int reviewId = createReviewAndGetId(userCookie, 1L, "Great Event", 5.0);

        given()
                .contentType("application/json")
                .when()
                .get(API_REVIEWS + reviewId)
                .then()
                .statusCode(200)
                .body("id", is(reviewId))
                .body("description", is("Great Event"))
                .body("rating", is(5.0f));

        given()
                .contentType("application/json")
                .when()
                .get(API_REVIEWS)
                .then()
                .statusCode(200)
                .body("content[0].description", is("Great Event"));

        given()
                .contentType("application/json")
                .when()
                .get(API_REVIEWS + "event/1")
                .then()
                .statusCode(200)
                .body("content[0].description", is("Great Event"));

        given()
                .contentType("application/json")
                .when()
                .get(API_REVIEWS + "user/user")
                .then()
                .statusCode(200)
                .body("content[0].description", is("Great Event"));

        given()
                .contentType("application/json")
                .when()
                .put(API_REVIEWS + reviewId)
                .then()
                .statusCode(401);

        String updatedReview = """
                {
                    "description": "Updated review",
                    "rating": 4.0,
                    "eventAssociatedId": 1,
                    "userOwnerId": 1,
                    "createdAt": null
                }
                """;

        given()
                .header("Cookie", userCookie)
                .contentType("application/json")
                .body(updatedReview)
                .when()
                .put(API_REVIEWS + reviewId)
                .then()
                .statusCode(200)
                .body("id", is(reviewId))
                .body("description", is("Updated review"))
                .body("rating", is(4.0f));

        given()
                .header("Cookie", userCookie)
                .contentType("application/json")
                .when()
                .delete(API_REVIEWS + reviewId)
                .then()
                .statusCode(200)
                .body("id", is(reviewId))
                .body("description", is("Updated review"));

        given()
                .contentType("application/json")
                .when()
                .get(API_REVIEWS + reviewId)
                .then()
                .statusCode(404);
    }

    @Test
    public void testCreateReviewNoAuth_return401() {
        String newReview = """
                {
                    "description": "Unauthenticated review",
                    "rating": 5.0,
                    "eventAssociatedId": 1,
                    "userOwnerId": 1,
                    "createdAt": null
                }
                """;

        given()
                .contentType("application/json")
                .body(newReview)
                .when()
                .post(API_REVIEWS + "event/1")
                .then()
                .statusCode(401);
    }

    @Test
    public void testCreateReview_return201() {
        String newReview = """
                {
                    "description": "Created from API",
                    "rating": 5.0,
                    "eventAssociatedId": 1,
                    "userOwnerId": 1,
                    "createdAt": null
                }
                """;

        given()
                .header("Cookie", getUserCookie())
                .contentType("application/json")
                .body(newReview)
                .when()
                .post(API_REVIEWS + "event/1")
                .then()
                .statusCode(201)
                .header("Location", containsString(API_REVIEWS + "event/1"))
                .body("id", notNullValue())
                .body("description", is("Created from API"))
                .body("rating", is(5.0f));
    }

    @Test
    public void testCreateReviewInvalidRating_return400() {
        String newReview = """
                {
                    "description": "Invalid rating review",
                    "rating": 6.0,
                    "eventAssociatedId": 1,
                    "userOwnerId": 1,
                    "createdAt": null
                }
                """;

        given()
                .header("Cookie", getUserCookie())
                .contentType("application/json")
                .body(newReview)
                .when()
                .post(API_REVIEWS + "event/1")
                .then()
                .statusCode(400);
    }

    @Test
    public void testGetReviewById_return404() {
        given()
                .contentType("application/json")
                .when()
                .get(API_REVIEWS + "999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetReviewById_return400() {
        given()
                .contentType("application/json")
                .when()
                .get(API_REVIEWS + "notNumber")
                .then()
                .statusCode(400);
    }

    @Test
    public void deleteOtherUserReview_return403() {
        String userCookie = getUserCookie();
        int reviewId = createReviewAndGetId(userCookie, 1L, "Review to delete", 3.0);

        given()
                .header("Cookie", getAdminCookie())
                .contentType("application/json")
                .when()
                .delete(API_REVIEWS + reviewId)
                .then()
                .statusCode(403);
    }
}
