package com.back.team11.domain.review.service;


import com.back.team11.domain.cafe.entity.Cafe;
import com.back.team11.domain.cafe.repository.CafeRepository;
import com.back.team11.domain.member.entity.Member;
import com.back.team11.domain.member.repository.MemberRepository;
import com.back.team11.domain.review.dto.ReviewRequestDto;
import com.back.team11.domain.review.dto.ReviewResponseDto;
import com.back.team11.domain.review.entity.Review;
import com.back.team11.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final CafeRepository cafeRepository;
    private final MemberRepository memberRepository;

    // 리뷰 작성
    public ReviewResponseDto createReview(Long cafeId, ReviewRequestDto requestDto, Long memberId) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        if (reviewRepository.existsByMemberIdAndCafeId(memberId, cafeId)) {
            throw new IllegalStateException("이미 해당 카페에 리뷰를 작성하셨습니다.");
        }

        Review review = new Review(member, cafe, requestDto.content());
        reviewRepository.save(review);

        return ReviewResponseDto.from(review);
    }

    //리뷰 조회
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviews(Long cafeId) {
        cafeRepository.findById(cafeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페입니다."));

        return reviewRepository.findAllByCafeId(cafeId).stream()
                .map(ReviewResponseDto::from)
                .collect(Collectors.toList());
    }

    //리뷰 수정
    public ReviewResponseDto updateReview(Long cafeId, Long reviewId, ReviewRequestDto requestDto, Long memberId) {
        Review review = reviewRepository.findByIdAndCafeId(reviewId, cafeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));

        if (!review.getMember().getId().equals(memberId)) {
            throw new SecurityException("리뷰 수정 권한이 없습니다.");
        }

        review.update(requestDto.content());

        return ReviewResponseDto.from(review);
    }

}
