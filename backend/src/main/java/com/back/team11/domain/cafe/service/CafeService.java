package com.back.team11.domain.cafe.service;

import com.back.team11.domain.cafe.dto.CafeDetailResponse;
import com.back.team11.domain.cafe.dto.CafeResponse;
import com.back.team11.domain.cafe.entity.Cafe;
import com.back.team11.domain.cafe.repository.CafeRepository;
import com.back.team11.domain.cafe.repository.CafeSearchCondition;
import com.back.team11.domain.global.exception.CustomException;
import com.back.team11.domain.global.exception.ErrorCode;
import com.back.team11.domain.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {
    private final CafeRepository cafeRepository;
    private final WishlistRepository wishlistRepository;

    // 카페 목록 조회(지역 검색 + 필터링)
    public List<CafeResponse> searchCafes(CafeSearchCondition condition) {
        List<Cafe> cafes = cafeRepository.searchCafes(condition);
        return cafes.stream()
                .map(CafeResponse::from)
                .toList();
    }

    // 카페 상세보기
    public CafeDetailResponse getCafe(Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(()-> new CustomException(ErrorCode.CAFE_NOT_FOUND));
        long wishlistCount = wishlistRepository.countByCafeId(cafeId);

        return CafeDetailResponse.from(cafe,wishlistCount);
    }
}
