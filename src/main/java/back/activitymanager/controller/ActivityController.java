package back.activitymanager.controller;

import back.activitymanager.dto.activity.ActivityCreateRequestDto;
import back.activitymanager.dto.activity.ActivityDto;
import back.activitymanager.dto.activity.ActivitySearchDto;
import back.activitymanager.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activity")
@Tag(name = "Activities", description = "Add/Remove/Get activities")
public class ActivityController {

    private final ActivityService activityService;

    @Operation(summary = "Create activity", description = "User can create activity")
    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Bearer")
    @ResponseStatus(HttpStatus.CREATED)
    public ActivityDto createActivity(
            Authentication authentication,
            @RequestBody @Valid ActivityCreateRequestDto createRequestDto) {
        return activityService.createActivity(
                authentication,
                createRequestDto);
    }

    @Operation(summary = "Get all activities")
    @PostMapping
    public Page<ActivityDto> getAll(Pageable pageable) {
        return activityService.getAll(pageable);
    }

    @Operation(summary = "Get activity by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Bearer")
    public ActivityDto getById(@PathVariable Long id) {
        return activityService.getById(id);
    }

    @Operation(summary = "Delete activity by id",
            description = "If the activity does`nt belong to user, "
                    + "user will not able to delete it")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Bearer")
    public void deleteById(Authentication authentication,
                           @PathVariable Long id) {
        activityService.deleteById(authentication, id);
    }

    @Operation(summary = "Take part in an event")
    @PostMapping("/participate/{id}")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Bearer")
    public ActivityDto participate(Authentication authentication,
                                   @PathVariable Long id) {
        return activityService.participateInActivity(authentication, id);
    }

    @Operation(summary = "Refuse to participate")
    @PostMapping("/refuse/{id}")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Bearer")
    public ActivityDto refuse(Authentication authentication,
                                   @PathVariable Long id) {
        return activityService.refuseInActivity(authentication, id);
    }

    @Operation(summary = "Get the activities created by current user")
    @PostMapping("/myActivities")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Bearer")
    public Page<ActivityDto> getMyActivity(Pageable pageable,
                                           Authentication authentication) {
        return activityService.getMyActivities(pageable, authentication);
    }

    @Operation(summary = "Get the activities current user participates")
    @PostMapping("/myParticipating")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Bearer")
    public Page<ActivityDto> getMeParticipating(Pageable pageable,
                                           Authentication authentication) {
        return activityService.getMeParticipating(pageable, authentication);
    }

    @Operation(summary = "Search activity")
    @PostMapping("/search")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Bearer")
    public List<ActivityDto> searchActivity(ActivitySearchDto activitySearchDto) {
        return activityService.searchActivity(activitySearchDto);
    }

}
