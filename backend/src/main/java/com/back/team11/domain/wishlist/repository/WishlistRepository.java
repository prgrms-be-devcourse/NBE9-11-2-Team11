package com.back.team11.domain.wishlist.repository;

import com.back.team11.domain.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    boolean existsByMemberIdAndCafeId(Long id, Long cafeId);

    void deleteByMemberIdAndCafeId(Long id, Long id1);

    //Lazy 로딩으로 인한 N+1 문제를 해결하기 위한 Join fetch 도입
    @Query("SELECT w FROM Wishlist w JOIN FETCH w.cafe WHERE w.member.id = :memberId")
    List<Wishlist> findAllByMemberIdWithCafe(@Param("memberId") Long memberId);

    long countByCafeId(Long cafeId);

    // 카페 삭제 시 연관된 찜 목록 일괄 삭제
    void deleteByCafeId(Long cafeId);

}
