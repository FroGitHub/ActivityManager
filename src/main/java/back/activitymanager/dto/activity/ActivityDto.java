package back.activitymanager.dto.activity;

import back.activitymanager.dto.user.UserWithoutRolesDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class ActivityDto {

    private Long id;

    private String name;

    private int maxNumberOfPeople;

    private int currentNumberOfPeople;

    private LocalDateTime localDateTime;

    private String category;

    private String location;

    private UserWithoutRolesDto author;

    private List<UserWithoutRolesDto> participants;
}
