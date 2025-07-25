package back.activitymanager.controller;

import back.activitymanager.dto.review.ReviewCreateRequestDto;
import back.activitymanager.dto.review.ReviewDto;
import back.activitymanager.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ReviewDto createReview(Authentication authentication,
                                  @Valid @RequestBody
                                  ReviewCreateRequestDto requestDto) {
        return reviewService.createReview(authentication, requestDto);
    }

    @GetMapping
    public Page<ReviewDto> getReviews(Pageable pageable) {
        return reviewService.getReviews(pageable);
    }

}
