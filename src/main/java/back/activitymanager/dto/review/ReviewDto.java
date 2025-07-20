package back.activitymanager.dto.review;

import back.activitymanager.dto.user.UserResponseDto;
import java.time.LocalDateTime;

public record ReviewDto(Long id,
                        String title,
                        int rate,
                        UserResponseDto user,
                        String comment,
                        LocalDateTime dateTime) {
}
