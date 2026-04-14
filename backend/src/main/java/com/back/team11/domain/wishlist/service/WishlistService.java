package com.back.team11.domain.wishlist.service;

import com.back.team11.domain.cafe.entity.Cafe;
import com.back.team11.domain.cafe.repository.CafeRepository;
import com.back.team11.domain.global.exception.CustomException;
import com.back.team11.domain.global.exception.ErrorCode;
import com.back.team11.domain.member.entity.Member;
import com.back.team11.domain.member.repository.MemberRepository;
import com.back.team11.domain.wishlist.dto.WishlistResponse;
import com.back.team11.domain.wishlist.entity.Wishlist;
import com.back.team11.domain.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WishlistService {

    private final MemberRepository memberRepository;
    private final CafeRepository cafeRepository;
    private final WishlistRepository wishlistRepository;


    @Transactional
    public WishlistResponse addWishlist(Long cafeId) {

        // 멤버 임시 구현(JWT 도입 후 수정 예정)
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 카페 존재 여부 확인
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new CustomException(ErrorCode.CAFE_NOT_FOUND));

        // Member는 한 카페에 한번만 찜 가능
        if(wishlistRepository.existsByMemberIdAndCafeId(member.getId(), cafeId)){
            throw new CustomException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }

        Wishlist wishlist = Wishlist.create(member, cafe);

        wishlistRepository.save(wishlist);

        return WishlistResponse.from(wishlist);
    }

    @Transactional
    public void deleteWishlist(Long cafeId) {
        // 멤버 임시 구현(JWT 도입 후 수정 예정)
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 카페 존재 여부 확인
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new CustomException(ErrorCode.CAFE_NOT_FOUND));

        // 찜 내역 없으면
        if(!wishlistRepository.existsByMemberIdAndCafeId(member.getId(), cafeId)){
            throw new CustomException(ErrorCode.REVIEW_NOT_FOUND);
        }

        wishlistRepository.deleteByMemberIdAndCafeId(member.getId(), cafe.getId());
    }

    public List<WishlistResponse> getWishlists() {
        // 멤버 임시 구현(JWT 도입 후 수정 예정)
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<Wishlist> wishlists = wishlistRepository.findAllByMemberIdWithCafe(member.getId());

        return wishlists.stream()
                .map(WishlistResponse::from)
                .toList();
    }
}
