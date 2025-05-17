package back.activitymanager.dto.user;

import java.util.Set;
import lombok.Data;

@Data
public class UserWithRoleDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Set<String> roleNames;

}
