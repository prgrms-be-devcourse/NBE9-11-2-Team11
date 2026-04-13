package com.back.team11.domain.cafe.repository;

import com.back.team11.domain.cafe.entity.CafeType;
import com.back.team11.domain.cafe.entity.CongestionLevel;
import com.back.team11.domain.cafe.entity.FloorCount;
import com.back.team11.domain.cafe.entity.Franchise;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CafeSearchCondition {
    // 좌표 범위
    @DecimalMin(value = "-90.0", message = "위도는 -90 ~ 90 사이여야 합니다.")
    @DecimalMax(value = "90.0", message = "위도는 -90 ~ 90 사이여야 합니다.")
    private Double swLat;

    @DecimalMin(value = "-180.0", message = "경도는 -180 ~ 180 사이여야 합니다.")
    @DecimalMax(value = "180.0", message = "경도는 -180 ~ 180 사이여야 합니다.")
    private Double swLng;

    @DecimalMin(value = "-90.0", message = "위도는 -90 ~ 90 사이여야 합니다.")
    @DecimalMax(value = "90.0", message = "위도는 -90 ~ 90 사이여야 합니다.")
    private Double neLat;

    @DecimalMin(value = "-180.0", message = "경도는 -180 ~ 180 사이여야 합니다.")
    @DecimalMax(value = "180.0", message = "경도는 -180 ~ 180 사이여야 합니다.")
    private Double neLng;

    // 필터링
    private CafeType type;
    private Franchise franchise;
    private Boolean hasToilet;
    private Boolean hasOutlet;
    private Boolean hasWifi;
    private FloorCount floorCount;
    private Boolean hasSeparateSpace;
    private CongestionLevel congestionLevel;
}
