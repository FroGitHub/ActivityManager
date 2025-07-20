package back.activitymanager.service.impl;

import back.activitymanager.dto.review.ReviewCreateRequestDto;
import back.activitymanager.dto.review.ReviewDto;
import back.activitymanager.mapper.ReviewMapper;
import back.activitymanager.model.Review;
import back.activitymanager.model.User;
import back.activitymanager.repository.ReviewRepository;
import back.activitymanager.service.ReviewService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewDto createReview(Authentication authentication,
                                  ReviewCreateRequestDto createRequestDto) {

        Review review = reviewMapper.toObjFromCreateRequest(createRequestDto);
        review.setUser((User) authentication.getPrincipal());
        review.setDateTime(LocalDateTime.now());
        return reviewMapper.toDto(reviewRepository.save(review));
    }

    @Override
    public Page<ReviewDto> getReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable)
                .map(reviewMapper::toDto);
    }
}
