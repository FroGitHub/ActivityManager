package back.activitymanager.controller;

import back.activitymanager.dto.user.UserResponseDto;
import back.activitymanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description =
        "Endpoints for get and update current info about user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get info of current user")
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Bearer")
    public UserResponseDto getUser(Authentication authentication) {
        return userService.getMyUserInfo(authentication);
    }

    @Operation(summary = "Delete current user")
    @DeleteMapping("/deleteMe")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Bearer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCurrentUser(Authentication authentication) {
        userService.deleteCurrentUser(authentication);
    }

}
