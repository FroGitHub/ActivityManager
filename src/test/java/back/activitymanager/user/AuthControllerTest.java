package back.activitymanager.user;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringStartsWith.startsWith;

import back.activitymanager.dto.user.UserLoginDto;
import back.activitymanager.security.AuthenticationService;
import back.activitymanager.service.UserService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Should register user and return UserResponseDto")
    @Sql(scripts = "classpath:database/user/delete-users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/user/delete-user-with-id-1.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void register_validRequest_returnsUserResponseDto() {

        RestAssured
                .given()
                    .log().all()
                    .multiPart("email", "test@example.com")
                    .multiPart("phoneNumber", "+380968396281")
                    .multiPart("password", "securePassword123")
                    .multiPart("repeatPassword", "securePassword123")
                    .multiPart("firstName", "user")
                    .multiPart("lastName", "user")
                .when()
                    .post("/auth/registration")
                .then()
                    .log().all()
                    .statusCode(200)
                    .body("id", equalTo(1))
                    .body("email", equalTo("test@example.com"))
                    .body("photo_path", equalTo(null));
    }

    @Test
    @DisplayName("Should login user and return JWT token")
    @Sql(scripts = "classpath:database/user/create-user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/user/delete-user-with-id-1.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void login_validCredentials_returnsJwtToken() {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setEmail("user@gmail.com");
        loginDto.setPassword("securePassword123");

        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(loginDto)
                .when()
                    .post("/auth/login")
                .then()
                    .statusCode(200)
                    .body("token", startsWith("eyJ"));

    }
}
