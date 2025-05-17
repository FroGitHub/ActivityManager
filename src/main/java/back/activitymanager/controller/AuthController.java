package back.activitymanager.controller;

import back.activitymanager.dto.user.UserLoginDto;
import back.activitymanager.dto.user.UserRegistrationRequestDto;
import back.activitymanager.dto.user.UserResponseDto;
import back.activitymanager.dto.user.UserResponseLoginDto;
import back.activitymanager.security.UserService;
import back.activitymanager.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @GetMapping("/login")
    public UserResponseLoginDto login(@RequestBody @Valid UserLoginDto userLoginDto) {
        return authenticationService.authenticate(userLoginDto);
    }

    @GetMapping("/registration")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request) {
        return userService.register(request);
    }

}
