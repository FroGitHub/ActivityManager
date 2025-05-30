package back.activitymanager.dto.user;

import lombok.Data;

@Data
public class UserWithoutRolesDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;

}
