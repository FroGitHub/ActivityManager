package back.activitymanager.service.impl;

import back.activitymanager.dto.user.UserRegistrationRequestDto;
import back.activitymanager.dto.user.UserResponseDto;
import back.activitymanager.dto.user.UserUpdateRequestDto;
import back.activitymanager.dto.user.UserUpdateRoleRequestDto;
import back.activitymanager.dto.user.UserWithRoleDto;
import back.activitymanager.exception.EntityNotFoundException;
import back.activitymanager.exception.RegistrationException;
import back.activitymanager.mapper.UserMapper;
import back.activitymanager.model.Role;
import back.activitymanager.model.User;
import back.activitymanager.repository.RoleRepository;
import back.activitymanager.repository.UserRepository;
import back.activitymanager.service.DropboxService;
import back.activitymanager.service.UserService;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String DEFAULT_PHOTO_PATH = "default";
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final DropboxService dropboxService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("User already exists with email: "
                    + request.getEmail());
        }

        MultipartFile photo = request.getFile();
        return buildAndSaveUserWithPhoto(request, photo);
    }

    @Override
    public UserResponseDto getMyUserInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return mapUserToDtoWithPhoto(user);
    }

    @Override
    public UserResponseDto updateUser(Authentication authentication,
                                      UserUpdateRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();

        userMapper.updateUser(user, requestDto);
        if (!Objects.equals(user.getPassword(), requestDto.getPassword())) {
            user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        }

        user = userRepository.save(user);
        return mapUserToDtoWithPhoto(user);
    }

    @Override
    public UserWithRoleDto updateUserRole(Long id,
                                          UserUpdateRoleRequestDto requestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "There is no user with id: " + id));

        Set<Role> roles = requestDto.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "No role: " + roleName)))
                .collect(Collectors.toSet());
        user.setRoles(roles);

        UserWithRoleDto dto = userMapper.toDtoWithRole(user);
        if (user.getPhotoPath() != null) {
            dto.setPhotoPath(dropboxService.getPhotoLink(user.getPhotoPath()));
        }

        return dto;
    }

    @Override
    public void deleteCurrentUser(Authentication authentication) {
        userRepository.deleteByEmail(authentication.getName());
    }

    private UserResponseDto buildAndSaveUserWithPhoto(UserRegistrationRequestDto request,
                                                      MultipartFile photo) {
        User user = userMapper.toModel(request);

        if (photo != null && !photo.isEmpty()) {
            String photoPath = dropboxService.uploadPhoto(photo);
            user.setPhotoPath(photoPath);
        } else {
            user.setPhotoPath(DEFAULT_PHOTO_PATH);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findByName(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Role is not found: ROLE_USER"));
        user.setRoles(Set.of(role));

        return mapUserToDtoWithPhoto(userRepository.save(user));
    }

    private UserResponseDto mapUserToDtoWithPhoto(User user) {
        UserResponseDto dto = userMapper.toDto(user);
        if (user.getPhotoPath() != null
                && !user.getPhotoPath().equals(DEFAULT_PHOTO_PATH)) {
            dto.setPhotoPath(dropboxService.getPhotoLink(user.getPhotoPath()));
        } else {
            dto.setPhotoPath("");
        }
        return dto;
    }
}
