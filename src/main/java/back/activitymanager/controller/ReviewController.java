package back.activitymanager.controller;

import back.activitymanager.dto.review.ReviewCreateRequestDto;
import back.activitymanager.dto.review.ReviewDto;
import back.activitymanager.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Review", description =
        "User can see a reviews and add a new reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Create a review")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Bearer")
    public ReviewDto createReview(Authentication authentication,
                                  @Valid @RequestBody
                                  ReviewCreateRequestDto requestDto) {
        return reviewService.createReview(authentication, requestDto);
    }

    @Operation(summary = "Get all reviews")
    @GetMapping
    public Page<ReviewDto> getReviews(Pageable pageable) {
        return reviewService.getReviews(pageable);
    }

}
