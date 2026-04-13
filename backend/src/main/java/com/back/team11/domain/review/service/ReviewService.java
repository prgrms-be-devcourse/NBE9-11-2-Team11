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


}
