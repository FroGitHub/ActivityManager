package back.activitymanager.service;

import back.activitymanager.dto.activity.ActivityCreateRequestDto;
import back.activitymanager.dto.activity.ActivityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface ActivityService {
    ActivityDto createActivity(Authentication authentication,
                               ActivityCreateRequestDto createRequestDto);

    Page<ActivityDto> getAll(Pageable pageable);

    ActivityDto getById(Long id);

    void deleteById(Authentication authentication, Long id);

    ActivityDto participateInActivity(Authentication authentication, Long id);

    ActivityDto refuseInActivity(Authentication authentication, Long id);

    Page<ActivityDto> getMyActivities(Pageable pageable, 
                                      Authentication authentication);

    Page<ActivityDto> getMeParticipating(Pageable pageable, Authentication authentication);
}
