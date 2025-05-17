package back.activitymanager.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
//@FieldMatches(fields = {"password", "repeatPassword"})
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8, message = "Password should be longer than 8")
    private String password;
    @NotBlank
    @Size(min = 8, message = "Password should be longer than 8")
    private String repeatPassword;
    @NotBlank
    @Size(max = 25, message = "FirstName should be longer than 25")
    private String firstName;
    @NotBlank
    @Size(max = 25, message = "LastName should be longer than 25")
    private String lastName;
    @NotBlank
    @Pattern(regexp = "^\\+380\\d{9}$",
            message = "Invalid phone number format (expected +380XXXXXXXXX)")
    private String phoneNumber;

}
