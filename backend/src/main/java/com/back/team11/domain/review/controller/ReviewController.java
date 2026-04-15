package com.back.team11.domain.review.controller;


import com.back.team11.domain.global.rsData.RsData;
import com.back.team11.domain.review.dto.ReviewRequestDto;
import com.back.team11.domain.review.dto.ReviewResponseDto;
import com.back.team11.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/V1/cafe/{cafeId}/reviews")
public class ReviewController {

    private final ReviewService  reviewService;

    /*  ---------------- JWT 구현 후 사용 -----------------------

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

     */


    // JWT 없을 때 임시 코드  JWT 구현 시 삭제

    @PostMapping
    public ResponseEntity<RsData<ReviewResponseDto>> createReview(
            @PathVariable Long cafeId,
            @Valid @RequestBody ReviewRequestDto requestDto,
            @RequestParam Long memberId  // 임시
    ) {
        ReviewResponseDto data = reviewService.createReview(cafeId, requestDto, memberId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RsData<>("리뷰가 작성되었습니다.", "201", data));
    }

    @GetMapping
    public ResponseEntity<RsData<List<ReviewResponseDto>>> getReviews(
            @PathVariable Long cafeId
    ) {
        List<ReviewResponseDto> data = reviewService.getReviews(cafeId);
        return ResponseEntity.ok(new RsData<>("리뷰 목록 조회 성공", "200", data));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<RsData<ReviewResponseDto>> updateReview(
            @PathVariable Long cafeId,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequestDto requestDto,
            @RequestParam Long memberId  // 임시
    ) {
        ReviewResponseDto data = reviewService.updateReview(cafeId, reviewId, requestDto, memberId);
        return ResponseEntity.ok(new RsData<>("리뷰가 수정되었습니다.", "200", data));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<RsData<Void>> deleteReview(
            @PathVariable Long cafeId,
            @PathVariable Long reviewId,
            @RequestParam Long memberId  // 임시
    ) {
        reviewService.deleteReview(cafeId, reviewId, memberId);
        return ResponseEntity.ok(new RsData<>("리뷰가 삭제되었습니다.", "200"));
    }

}
