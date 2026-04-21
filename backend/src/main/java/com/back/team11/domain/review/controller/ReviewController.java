package com.back.team11.domain.review.controller;


import com.back.team11.domain.global.dto.PageResponse;
import com.back.team11.domain.global.rsData.RsData;
import com.back.team11.domain.global.util.AuthUtil;
import com.back.team11.domain.review.dto.ReviewRequestDto;
import com.back.team11.domain.review.dto.ReviewResponseDto;
import com.back.team11.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/V1/cafe/{cafeId}/reviews")
public class ReviewController {

    private final ReviewService  reviewService;
    private final AuthUtil authUtil;

    //리뷰 작성
    @PostMapping
    public ResponseEntity<RsData<ReviewResponseDto>> createReview(
            @PathVariable Long cafeId,
            @Valid @RequestBody ReviewRequestDto requestDto
    ) {
        Long memberId = authUtil.getCurrentMemberId();
        ReviewResponseDto data = reviewService.createReview(cafeId, requestDto,memberId);
        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RsData<>("리뷰가 작성되었습니다.", "201", data));
    }

    //리뷰조회
    @GetMapping
    public ResponseEntity<RsData<List<ReviewResponseDto>>> getReviews(
            @PathVariable Long cafeId
    ) {
        List<ReviewResponseDto> data = reviewService.getReviews(cafeId);
        return ResponseEntity.ok(new RsData<>("리뷰 목록 조회 성공", "200", data));
    }

    //페이징 리뷰조회
    @GetMapping("/page")
    public ResponseEntity<RsData<PageResponse<ReviewResponseDto>>> getReviewsPage(
            @PathVariable Long cafeId,
            @PageableDefault(size = 10)
            Pageable pageable
    ) {
        PageResponse<ReviewResponseDto> data = reviewService.getReviewsPage(cafeId, pageable);
        return ResponseEntity.ok(new RsData<>("리뷰 페이징 조회 성공", "200", data));
    }

    //리뷰수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<RsData<ReviewResponseDto>> updateReview(
            @PathVariable Long cafeId,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequestDto requestDto
    ){
        Long memberId = authUtil.getCurrentMemberId();
        ReviewResponseDto data = reviewService.updateReview(cafeId, reviewId, requestDto, memberId);
        return ResponseEntity.ok(new RsData<>("리뷰가 수정되었습니다.", "200", data));
    }

    //리뷰삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<RsData<Void>> deleteReview(
            @PathVariable Long cafeId,
            @PathVariable Long reviewId
    ) {
        Long memberId = authUtil.getCurrentMemberId();
        reviewService.deleteReview(cafeId, reviewId, memberId);
        return ResponseEntity.ok(new RsData<>("리뷰가 삭제되었습니다.", "200"));
    }

}
