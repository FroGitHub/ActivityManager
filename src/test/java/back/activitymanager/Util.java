package back.activitymanager;

import back.activitymanager.dto.user.UserLoginDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class Util {
    public static String getToken() {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setEmail("user@gmail.com");
        loginDto.setPassword("securePassword123");

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(loginDto)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }
}
