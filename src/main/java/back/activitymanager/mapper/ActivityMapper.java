package back.activitymanager.mapper;

import back.activitymanager.config.MapperConfig;
import back.activitymanager.dto.activity.ActivityCreateRequestDto;
import back.activitymanager.dto.activity.ActivityDto;
import back.activitymanager.model.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ActivityMapper {

    ActivityDto toDto(Activity activity);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "participants", ignore = true)
    Activity toObjFromCreateRequest(
            ActivityCreateRequestDto createRequestDto);

}
