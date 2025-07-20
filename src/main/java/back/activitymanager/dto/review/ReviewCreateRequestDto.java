package back.activitymanager.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewCreateRequestDto {
    @NotBlank
    private String title;
    @Min(1)
    @Max(5)
    private int rate;
    @NotBlank
    private String comment;
}
