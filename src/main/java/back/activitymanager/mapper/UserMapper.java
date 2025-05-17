package back.activitymanager.mapper;

import back.activitymanager.config.MapperConfig;
import back.activitymanager.dto.user.UserRegistrationRequestDto;
import back.activitymanager.dto.user.UserResponseDto;
import back.activitymanager.dto.user.UserUpdateRequestDto;
import back.activitymanager.dto.user.UserWithRoleDto;
import back.activitymanager.model.User;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto userRegistrationRequestDto);

    @Mapping(target = "roleNames", ignore = true)
    UserWithRoleDto toDtoWithRole(User user);

    @AfterMapping
    default void userRolesToString(@MappingTarget UserWithRoleDto userWithRoleDto,
                                   User user) {
        userWithRoleDto.setRoleNames(user.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet()));
    }

    void updateUser(@MappingTarget User user, UserUpdateRequestDto requestDto);
}
