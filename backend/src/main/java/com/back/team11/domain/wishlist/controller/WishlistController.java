package com.back.team11.domain.wishlist.controller;

import com.back.team11.domain.global.rsData.RsData;
import com.back.team11.domain.wishlist.dto.WishlistResponse;
import com.back.team11.domain.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        WishlistResponse wishlist = wishlistService.addWishList(cafeId);
        return ResponseEntity.ok(new RsData<>("200", "찜이 추가되었습니다."));
    }
}
