package com.back.team11.domain.cafe.repository;

import com.back.team11.domain.cafe.entity.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.back.team11.domain.cafe.entity.QCafe.cafe;

@Repository
@RequiredArgsConstructor
public class CafeRepositoryImpl implements CafeRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Cafe> searchCafes(CafeSearchCondition condition) {
        return queryFactory
                .selectFrom(cafe)
                .where(
                        isApproved(),//AND status = 'APPROVED'
                        latBetween(condition.getSwLat(), condition.getNeLat()),//AND latitude BETWEEN swLat AND neLat
                        lngBetween(condition.getSwLng(), condition.getNeLng()),// AND longitude BETWEEN swLng AND neLng
                        cafeTypeEq(condition.getType()),// AND type = ?
                        franchiseEq(condition.getFranchise()),// AND franchise = ?
                        hasToiletEq(condition.getHasToilet()),// AND has_toilet = ?
                        hasOutletEq(condition.getHasOutlet()),// AND has_outlet = ?
                        hasWifiEq(condition.getHasWifi()),// AND has_wifi = ?
                        floorCountEq(condition.getFloorCount()),// AND floor_count = ?
                        hasSeparateSpaceEq(condition.getHasSeparateSpace()),// AND has_separate_space = ?
                        congestionEq(condition.getCongestionLevel())// AND congestion_level = ?
                )
                .fetch();// List<Cafe> 반환
    }

    private BooleanExpression isApproved(){
        return cafe.status.eq(CafeStatus.APPROVED);
    }

    private BooleanExpression latBetween(Double swLat, Double neLat) {
        return (swLat != null && neLat != null)
                ? cafe.latitude.between(swLat, neLat) : null;
    }

    private BooleanExpression lngBetween(Double swLng, Double neLng) {
        return (swLng != null && neLng != null)
                ? cafe.longitude.between(swLng, neLng) : null;
    }

    private BooleanExpression cafeTypeEq(CafeType type) {
        return type != null ? cafe.type.eq(type) : null;
    }

    private BooleanExpression franchiseEq(Franchise franchise) {
        return franchise != null ? cafe.franchise.eq(franchise) : null;
    }

    private BooleanExpression hasToiletEq(Boolean hasToilet) {
        return hasToilet != null ? cafe.hasToilet.eq(hasToilet) : null;
    }

    private BooleanExpression hasOutletEq(Boolean hasOutlet) {
        return hasOutlet != null ? cafe.hasOutlet.eq(hasOutlet) : null;
    }

    private BooleanExpression hasWifiEq(Boolean hasWifi) {
        return hasWifi != null ? cafe.hasWifi.eq(hasWifi) : null;
    }

    private BooleanExpression floorCountEq(FloorCount floorCount) {
        return floorCount != null ? cafe.floorCount.eq(floorCount) : null;
    }

    private BooleanExpression hasSeparateSpaceEq(Boolean hasSeparateSpace) {
        return hasSeparateSpace != null ? cafe.hasSeparateSpace.eq(hasSeparateSpace) : null;
    }

    private BooleanExpression congestionEq(CongestionLevel congestionLevel) {
        return congestionLevel != null ? cafe.congestionLevel.eq(congestionLevel) : null;
    }
}
