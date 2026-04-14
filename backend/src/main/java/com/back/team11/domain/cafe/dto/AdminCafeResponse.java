package com.back.team11.domain.cafe.dto;

import com.back.team11.domain.cafe.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// 관리자용 응답 DTO - 사용자용 CafeResponse와 달리 status, createdAt 포함
public record AdminCafeResponse(
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
        CafeStatus status,       // 관리자만 확인 필요
        LocalDateTime createdAt  // 관리자만 확인 필요
) {
    public static AdminCafeResponse from(Cafe cafe) {
        return new AdminCafeResponse(
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
                cafe.getStatus(),
                cafe.getCreatedAt()
        );
    }
}