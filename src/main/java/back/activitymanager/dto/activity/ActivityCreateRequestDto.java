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

    @NotBlank
    private String forWho;

    @Min(0)
    private int numberOfPeople;

    @NotBlank
    private String category;

    @NotBlank
    private String format;

    @NotNull
    private double lat;

    @NotNull
    private double lng;

    @NotNull
    private int imgId;

    @NotNull
    private LocalDateTime localDateTime;
}
