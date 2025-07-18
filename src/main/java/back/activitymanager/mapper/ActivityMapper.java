package back.activitymanager.mapper;

import back.activitymanager.api.LocalAdaptorApi;
import back.activitymanager.config.MapperConfig;
import back.activitymanager.dto.activity.ActivityCreateRequestDto;
import back.activitymanager.dto.activity.ActivityDto;
import back.activitymanager.model.Activity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = {UserMapper.class})
public interface ActivityMapper {

    @Mapping(target = "currentNumberOfPeople", ignore = true)
    @Mapping(target = "local", ignore = true)
    ActivityDto toDto(Activity activity);

    @AfterMapping
    default void setCurrentNumberOfPeople(
            @MappingTarget ActivityDto activityDto, Activity activity) {
        activityDto.setCurrentNumberOfPeople(activity.getParticipants().size());
    }

    @AfterMapping
    default void setLocal(
            @MappingTarget ActivityDto activityDto, Activity activity) {
        activityDto.setLocal(LocalAdaptorApi
                .getLocalNameByLatLon(
                        activity.getLat(),
                        activity.getLng()));
    }

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "participants", ignore = true)
    Activity toObjFromCreateRequest(
            ActivityCreateRequestDto createRequestDto);

}
