package back.activitymanager.user;

import back.activitymanager.Util;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import static org.hamcrest.core.IsEqual.equalTo;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Should login user and return information about current user")
    @Sql(scripts = "classpath:database/user/create-user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/user/delete-user-with-id-1.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getMe_validRequest_returnsUserResponseDto() {

        RestAssured
                .given()
                    .header("Authorization", "Bearer " + Util.getToken())
                    .accept(ContentType.JSON)
                .when()
                    .get("/users/me")
                .then()
                    .statusCode(200)
                    .body("email", equalTo("user@gmail.com"));
    }

    @Test
    @DisplayName("Should login user and return information about current user")
    @Sql(scripts = "classpath:database/user/create-user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteMe_validRequest_returnsStatusNoContent() {
        RestAssured
                .given()
                    .header("Authorization", "Bearer " + Util.getToken())
                    .accept(ContentType.JSON)
                .when()
                    .delete("/users/deleteMe")
                .then()
                    .statusCode(204);
    }
}
