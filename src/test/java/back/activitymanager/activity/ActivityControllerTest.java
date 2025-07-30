package back.activitymanager.activity;

import back.activitymanager.Util;
import back.activitymanager.dto.activity.ActivityCreateRequestDto;
import back.activitymanager.dto.activity.ActivityDto;
import back.activitymanager.dto.activity.ActivitySearchDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActivityControllerTest {
    private static final String POST_ID = "01028";
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Should create activity successfully")
    @Sql(scripts = "classpath:database/user/create-user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/activity/delete-activities.sql",
            "classpath:database/user/delete-users.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createActivity_successCreation_ReturnsActivityDto() {
        ActivityCreateRequestDto dto = new ActivityCreateRequestDto();
        dto.setName("Yoga Session");
        dto.setForWho("Adults");
        dto.setNumberOfPeople(10);
        dto.setCategory("Health");
        dto.setFormat("Offline");
        dto.setLat(50.4501);
        dto.setLng(30.5234);
        dto.setImgId(1);
        dto.setLocalDateTime(LocalDateTime.of(2025, 8, 15, 10, 0));

        RestAssured
                .given()
                    .header("Authorization", "Bearer " + Util.getToken())
                    .contentType(ContentType.JSON)
                    .body(dto)
                .when()
                    .post("/activity/create")
                .then()
                    .statusCode(201)
                    .body("local", endsWith(POST_ID));
    }

    @Test
    @DisplayName("Should get activity successfully")
    @Sql(scripts = {"classpath:database/user/create-user.sql",
            "classpath:database/activity/create-activity.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/activity/delete-activities.sql",
            "classpath:database/user/delete-users.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getActivities_successGetting_ReturnsPageActivityDto() {
        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                .when()
                    .post("/activity")
                .then()
                    .statusCode(200)
                    .body("content[0].name",
                            equalTo("football"))
                    .body("content[0].local",
                            equalTo("Not found"));
    }

    @Test
    @DisplayName("Should get activity by id successfully")
    @Sql(scripts = {"classpath:database/user/create-user.sql",
            "classpath:database/activity/create-activity.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/activity/delete-activities.sql",
            "classpath:database/user/delete-users.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getActivities_successGetting_ReturnsActivityDto() {
        RestAssured
                .given()
                    .header("Authorization", "Bearer " + Util.getToken())
                    .contentType(ContentType.JSON)
                .when()
                    .get("/activity/123")
                .then()
                .statusCode(200)
                    .body("name", equalTo("football"))
                    .body("local", equalTo("Not found"));
    }

    @Test
    @DisplayName("Should get activity a user created successfully")
    @Sql(scripts = {"classpath:database/user/create-user.sql",
            "classpath:database/activity/create-activity.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/activity/delete-activities.sql",
            "classpath:database/user/delete-users.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getMyActivities_successGetting_ReturnsPageActivityDto() {
        RestAssured
                .given()
                    .header("Authorization", "Bearer " + Util.getToken())
                    .contentType(ContentType.JSON)
                .when()
                    .post("/activity/myActivities")
                .then()
                    .statusCode(200)
                    .body("content[0].name", equalTo("football"))
                    .body("content[0].local", equalTo("Not found"));
    }

    @Test
    @DisplayName("Should get activity user participating successfully")
    @Sql(scripts = {"classpath:database/user/create-user.sql",
            "classpath:database/activity/create-activity.sql",
            "classpath:database/activity/create-activity-user.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/activity/delete-activity-user.sql",
            "classpath:database/activity/delete-activities.sql",
            "classpath:database/user/delete-users.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getMeParticipating_successGetting_ReturnsPageActivityDto() {
        RestAssured
                .given()
                    .header("Authorization", "Bearer " + Util.getToken())
                    .contentType(ContentType.JSON)
                .when()
                    .post("/activity/myActivities")
                .then()
                    .statusCode(200)
                    .body("content[0].name", equalTo("football"))
                    .body("content[0].local", equalTo("Not found"));
    }

    @Test
    @DisplayName("Should get activity user participating successfully")
    @Sql(scripts = {"classpath:database/user/create-user.sql",
            "classpath:database/activity/create-activity.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/activity/delete-activities.sql",
            "classpath:database/user/delete-users.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void searchActivity_successGetting_ReturnsPageActivityDto() {
        ActivitySearchDto activitySearchDto = new ActivitySearchDto();
        activitySearchDto.setCategory("sport");
        activitySearchDto.setFormat("format");
        activitySearchDto.setForWho("men");

        List<ActivityDto> activityDtos = RestAssured
                .given()
                    .header("Authorization", "Bearer " + Util.getToken())
                    .contentType(ContentType.JSON)
                    .body(activitySearchDto)
                .when()
                    .post("/activity/search")
                .then()
                    .statusCode(200)
                    .extract()
                .body()
                    .jsonPath()
                    .getList(".", ActivityDto.class);

        assertEquals(activityDtos.size(), 1);
        assertEquals(activityDtos.get(0).getId(), 123);
        assertEquals(activityDtos.get(0).getImgId(), 1);
        assertEquals(
                activityDtos.get(0)
                        .getLocalDateTime()
                        .toString(),
                "2025-08-15T10:00");
    }
}
