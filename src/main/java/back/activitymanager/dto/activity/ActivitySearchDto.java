package back.activitymanager.dto.activity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ActivitySearchDto {
    private String local;
    private String forWho;
    private String category;
    private String format;
    private LocalDateTime dateTime;
}
