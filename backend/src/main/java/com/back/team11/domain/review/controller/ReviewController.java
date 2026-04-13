package com.back.team11.domain.review.controller;


import com.back.team11.domain.review.dto.ReviewRequestDto;
import com.back.team11.domain.review.dto.ReviewResponseDto;
import com.back.team11.domain.review.service.ReviewService;
import com.back.team11.domain.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
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


}
