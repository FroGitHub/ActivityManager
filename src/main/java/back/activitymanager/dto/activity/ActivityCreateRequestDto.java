package back.activitymanager.dto.activity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ActivityCreateRequestDto {

    @NotBlank
    private String name;

    @Min(1)
    private int maxNumberOfPeople;

    @Min(0)
    private int currentNumberOfPeople;

    @NotNull
    private LocalDateTime localDateTime;

    @NotNull
    private String category;

    @NotBlank
    private String location;
}
