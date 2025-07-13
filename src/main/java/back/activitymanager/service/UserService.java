package back.activitymanager.service;

import back.activitymanager.dto.user.UserRegistrationRequestDto;
import back.activitymanager.dto.user.UserResponseDto;
import back.activitymanager.dto.user.UserUpdateRequestDto;
import back.activitymanager.dto.user.UserUpdateRoleRequestDto;
import back.activitymanager.dto.user.UserWithRoleDto;
import back.activitymanager.exception.RegistrationException;
import org.springframework.security.core.Authentication;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException;

    UserResponseDto getMyUserInfo(Authentication authentication);

    UserResponseDto updateUser(
            Authentication authentication,
            UserUpdateRequestDto requestDto);

    UserWithRoleDto updateUserRole(Long id, UserUpdateRoleRequestDto requestDto);

    void deleteCurrentUser(Authentication authentication);
}
