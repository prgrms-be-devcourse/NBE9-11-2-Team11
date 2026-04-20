package com.back.team11.domain.wishlist.controller;

import com.back.team11.domain.global.rsData.RsData;
import com.back.team11.domain.wishlist.dto.WishlistResponse;
import com.back.team11.domain.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/V1")
public class WishlistController {

    private final WishlistService wishlistService;

    //찜 추가
    @PostMapping("/cafe/{cafeId}/wishlist")
    public ResponseEntity<RsData<WishlistResponse>> addWishList(
            @PathVariable Long cafeId
    ){
        WishlistResponse wishlist = wishlistService.addWishlist(cafeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RsData<>("찜이 추가되었습니다.", "201",wishlist));
    }

    //찜 취소
    @DeleteMapping("/cafe/{cafeId}/wishlist")
    public ResponseEntity<RsData<Void>> deleteWishlist(
            @PathVariable Long cafeId
    ){
        wishlistService.deleteWishlist(cafeId);
        return ResponseEntity.ok(new RsData<>( "찜이 취소되었습니다.", "200"));
    }

    // 내 찜 목록 조회, 필요시 페이지 기능 추가
    @GetMapping("/member/me/wishlist")
    public ResponseEntity<RsData<Page<WishlistResponse>>> getWishlists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<WishlistResponse> wishlists = wishlistService.getWishlists(pageable);
        return ResponseEntity.ok(new RsData<>("찜 목록 조회 성공", "200", wishlists));
    }
}
