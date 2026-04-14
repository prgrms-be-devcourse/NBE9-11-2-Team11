package com.back.team11.domain.wishlist.repository;

import com.back.team11.domain.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    boolean existsByMemberIdAndCafeId(Long id, Long cafeId);
}
