package back.activitymanager.service;

import back.activitymanager.dto.review.ReviewCreateRequestDto;
import back.activitymanager.dto.review.ReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface ReviewService {
    ReviewDto createReview(Authentication authentication, ReviewCreateRequestDto createRequestDto);

    Page<ReviewDto> getReviews(Pageable pageable);
}
