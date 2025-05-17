package back.activitymanager.dto.user;

import back.activitymanager.model.Role;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;

@Data
public class UserUpdateRoleRequestDto {
    @NotNull
    private Set<Role.RoleName> roles;
}

