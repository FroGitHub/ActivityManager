package back.activitymanager.mapper;

import back.activitymanager.config.MapperConfig;
import back.activitymanager.dto.review.ReviewCreateRequestDto;
import back.activitymanager.dto.review.ReviewDto;
import back.activitymanager.model.Review;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ReviewMapper {

    ReviewDto toDto(Review review);

    Review toObjFromCreateRequest(ReviewCreateRequestDto requestDto);
}
