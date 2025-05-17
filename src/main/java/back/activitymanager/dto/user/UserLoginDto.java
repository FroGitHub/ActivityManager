package back.activitymanager.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDto {
    @Email
    private String email;
    @NotBlank
    private String password;
}
