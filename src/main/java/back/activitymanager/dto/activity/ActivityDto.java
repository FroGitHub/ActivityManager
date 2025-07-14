package back.activitymanager.dto.activity;

import back.activitymanager.dto.user.UserWithoutRolesDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class ActivityDto {
    private Long id;

    private String name;

    private String forWho;

    private int numberOfPeople;

    private int currentNumberOfPeople;

    private String category;

    private String format;

    private double lat;

    private double lng;

    private int imgId;

    private LocalDateTime localDateTime;

    private UserWithoutRolesDto author;

    private List<UserWithoutRolesDto> participants;
}
