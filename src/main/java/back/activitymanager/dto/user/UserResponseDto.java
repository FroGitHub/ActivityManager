package back.activitymanager.dto.user;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String photoPath;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
