package com.back.team11.domain.cafe.dto;

import com.back.team11.domain.cafe.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CafeListResponse(
        Long cafeId,
        String name,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        String phone,
        String description,
        CafeType type,
        Franchise franchise,
        Boolean hasToilet,
        Boolean hasOutlet,
        Boolean hasWifi,
        FloorCount floorCount,
        Boolean hasSeparateSpace,
        CongestionLevel congestionLevel,
        String imageUrl,
        long wishlistCount,
        LocalDateTime createdAt   // 추가
) {
    public static CafeListResponse from(Cafe cafe, long wishlistCount) {
        return new CafeListResponse(
                cafe.getId(),
                cafe.getName(),
                cafe.getAddress(),
                cafe.getLatitude(),
                cafe.getLongitude(),
                cafe.getPhone(),
                cafe.getDescription(),
                cafe.getType(),
                cafe.getFranchise(),
                cafe.getHasToilet(),
                cafe.getHasOutlet(),
                cafe.getHasWifi(),
                cafe.getFloorCount(),
                cafe.getHasSeparateSpace(),
                cafe.getCongestionLevel(),
                cafe.getImageUrl(),
                wishlistCount,
                cafe.getCreatedAt()   // 추가
        );
    }
}