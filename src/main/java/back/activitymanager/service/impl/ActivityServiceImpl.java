package back.activitymanager.service.impl;

import back.activitymanager.api.LocalAdaptorApi;
import back.activitymanager.dto.activity.ActivityCreateRequestDto;
import back.activitymanager.dto.activity.ActivityDto;
import back.activitymanager.dto.activity.ActivitySearchDto;
import back.activitymanager.exception.EntityNotFoundException;
import back.activitymanager.exception.LocalNotFoundException;
import back.activitymanager.exception.NoAccessException;
import back.activitymanager.mapper.ActivityMapper;
import back.activitymanager.model.Activity;
import back.activitymanager.model.User;
import back.activitymanager.repository.ActivityRepository;
import back.activitymanager.repository.ActivitySpecification;
import back.activitymanager.repository.UserRepository;
import back.activitymanager.service.ActivityService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final UserRepository userRepository;

    @Override
    public ActivityDto createActivity(Authentication authentication,
                                      ActivityCreateRequestDto createRequestDto) {
        Activity activity = activityMapper.toObjFromCreateRequest(createRequestDto);

        User user = (User) authentication.getPrincipal();

        activity.setAuthor(user);
        activity.getParticipants().add(user);

        return activityMapper.toDto(activityRepository.save(activity));
    }

    @Override
    public Page<ActivityDto> getAll(Pageable pageable) {
        return activityRepository.findAll(pageable).map(activityMapper::toDto);
    }

    @Override
    public ActivityDto getById(Long id) {
        return activityMapper.toDto(activityRepository
                .findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "There is no activity with id: " + id)));
    }

    @Override
    public void deleteById(Authentication authentication,
                           Long id) {
        if (activityRepository.existsByAuthor(
                (User) authentication.getPrincipal())) {
            activityRepository.deleteById(id);
        } else {
            throw new NoAccessException(
                    "You don`t have access to delete the activity with id: " + id);
        }
    }

    @Override
    public ActivityDto participateInActivity(Authentication authentication,
                                             Long id) {
        Activity activity = activityRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("There is no activity with id: " + id));

        if (activity.getNumberOfPeople() == activity.getParticipants().size()) {
            throw new NoAccessException("The room is full in the activity with id: " + id);
        }

        User principal = (User) authentication.getPrincipal();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found with email: " + principal.getEmail()));

        activity.addParticipant(user);

        activityRepository.flush();

        return activityMapper.toDto(activity);
    }

    @Override
    public ActivityDto refuseInActivity(Authentication authentication, Long id) {
        Activity activity = activityRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("There is no activity with id: " + id));

        User principal = (User) authentication.getPrincipal();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found with email: " + principal.getEmail()));

        activity.removeParticipant(user);

        activityRepository.flush();

        return activityMapper.toDto(activity);
    }

    @Override
    public Page<ActivityDto> getMyActivities(Pageable pageable,
                                          Authentication authentication) {
        return activityRepository
                .findByAuthorEmail(pageable, authentication.getName())
                .map(activityMapper::toDto);
    }

    @Override
    public Page<ActivityDto> getMeParticipating(Pageable pageable,
                                                Authentication authentication) {
        return activityRepository
                .findByParticipantsEmail(pageable, authentication.getName())
                .map(activityMapper::toDto);
    }

    private static boolean searchActivityHelper(double lat, double lng, String local)
            throws LocalNotFoundException {
        try {
            return LocalAdaptorApi.isEtLocal(lat, lng, local);
        } catch (Exception e) {
            throw new LocalNotFoundException("Local is not found: " + local);
        }
    }

    @Override
    public List<ActivityDto> searchActivity(ActivitySearchDto activitySearchDto) {

        Specification<Activity> spec = Specification
                .where(ActivitySpecification.hasForWho(activitySearchDto.getForWho()))
                .and(ActivitySpecification.hasCategory(activitySearchDto.getCategory()))
                .and(ActivitySpecification.hasFormat(activitySearchDto.getFormat()))
                .and(ActivitySpecification.afterDate(activitySearchDto.getDateTime()));

        List<Activity> all = activityRepository.findAll(spec)
                .stream()
                .filter(a -> searchActivityHelper(a.getLat(),
                        a.getLng(), activitySearchDto.getLocal()))
                .toList();

        return all.stream().map(activityMapper::toDto).toList();
    }

}
