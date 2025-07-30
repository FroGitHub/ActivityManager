package back.activitymanager.review;

import back.activitymanager.Util;
import back.activitymanager.dto.review.ReviewCreateRequestDto;
import back.activitymanager.dto.review.ReviewDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReviewControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Should return all reviews successfully")
    @Sql(scripts = {"classpath:database/user/create-user.sql",
            "classpath:database/review/create-review.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/review/delete-reviews.sql",
            "classpath:database/user/delete-users.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getReviews_successReturn_ReturnsReviewDto() {
        List<ReviewDto> list = given()
                    .contentType(ContentType.JSON)
                .when()
                    .get("/review")
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                    .jsonPath()
                    .getList("content", ReviewDto.class);

        assertEquals(list.size(), 1);
        assertEquals(list.get(0).id(), 1);
        assertEquals(list.get(0).rate(), 4);
        assertEquals(list.get(0).title(), "football");
        assertEquals(list.get(0)
                        .user()
                        .getEmail(),
                "user@gmail.com");
        assertEquals(list.get(0)
                        .dateTime()
                        .toString(),
                "2025-08-15T10:00");
    }

    @Test
    @DisplayName("Should create a new reviews successfully")
    @Sql(scripts = {"classpath:database/user/create-user.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/review/delete-reviews.sql",
            "classpath:database/user/delete-users.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createReview_successCreate_ReturnsReviewDto() {

        ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto();
        requestDto.setComment("good game");
        requestDto.setRate(5);
        requestDto.setTitle("football");

        RestAssured
                .given()
                    .header("Authorization", "Bearer " + Util.getToken())
                    .contentType(ContentType.JSON)
                    .body(requestDto)
                .when()
                    .post("/review")
                .then()
                    .statusCode(200)
                    .body("user.email", equalTo("user@gmail.com"))
                    .body("id", equalTo(1))
                    .body("rate", equalTo(5))
                    .body("title", equalTo("football"))
                    .body("comment", equalTo("good game"));

    }
}
