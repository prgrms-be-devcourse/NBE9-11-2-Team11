package com.back.team11.domain.review.repository;

import com.back.team11.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByMemberIdAndCafeId(Long memberId, Long cafeId);  //리뷰를 생성한지 안한지 체크
    List<Review> findAllByCafeId(Long cafeId);
    Optional<Review> findByIdAndCafeId(Long reviewId, Long cafeId);

    // 카페 삭제 시 연관된 리뷰 일괄 삭제
    void deleteByCafeId(Long cafeId);

}
