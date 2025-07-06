package back.activitymanager.controller;

import back.activitymanager.dto.user.UserLoginDto;
import back.activitymanager.dto.user.UserRegistrationRequestDto;
import back.activitymanager.dto.user.UserResponseDto;
import back.activitymanager.dto.user.UserResponseLoginDto;
import back.activitymanager.security.AuthenticationService;
import back.activitymanager.service.UserService;
import jakarta.validation.Valid;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<UserResponseLoginDto> login(
            @RequestBody @Valid UserLoginDto userLoginDto
    ) {
        UserResponseLoginDto loginResponse = authenticationService.authenticate(userLoginDto);

        ResponseCookie cookie = ResponseCookie.from("jwt", loginResponse.token())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request) {
        return userService.register(request);
    }

}
