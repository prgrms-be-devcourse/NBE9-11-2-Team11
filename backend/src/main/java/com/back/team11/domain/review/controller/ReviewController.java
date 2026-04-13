package com.back.team11.domain.review.controller;


import com.back.team11.domain.global.rsData.RsData;
import com.back.team11.domain.review.dto.ReviewRequestDto;
import com.back.team11.domain.review.dto.ReviewResponseDto;
import com.back.team11.domain.review.service.ReviewService;
import com.back.team11.domain.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/V1/cafes/{cafeId}/reviews")
public class ReviewController {

    private final ReviewService  reviewService;

    //리뷰 작성
    @PostMapping
    public RsData<ReviewResponseDto> createReview(
            @PathVariable Long cafeId,
            @Valid @RequestBody ReviewRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails // 시큐리티 설정에 맞게 수정
    ) {
        ReviewResponseDto data = reviewService.createReview(cafeId, requestDto, userDetails.getMember().getId());
        return new RsData<>("리뷰가 작성되었습니다.", "201", data);
    }

    //리뷰조회
    @GetMapping
    public RsData<List<ReviewResponseDto>> getReviews(
            @PathVariable Long cafeId
    ) {
        List<ReviewResponseDto> data = reviewService.getReviews(cafeId);
        return new RsData<>("리뷰 목록 조회 성공", "200", data);
    }

    //리뷰수정
    @PutMapping("/{reviewId}")
    public RsData<ReviewResponseDto> updateReview(
            @PathVariable Long cafeId,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails // 시큐리티 설정에 맞게 수정
    ){
        ReviewResponseDto data = reviewService.updateReview(cafeId, reviewId, requestDto, userDetails.getMember().getId());
        return new RsData<>("리뷰가 수정되었습니다.", "200", data);
    }

    //리뷰삭제
    @DeleteMapping("/{reviewId}")
    public RsData<Void> deleteReview(
            @PathVariable Long cafeId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomUserDetails userDetails // 시큐리티 설정에 맞게 수정
    ) {
        reviewService.deleteReview(cafeId, reviewId, userDetails.getMember().getId());
        return new RsData<>("리뷰가 삭제되었습니다.", "200");
    }

}
