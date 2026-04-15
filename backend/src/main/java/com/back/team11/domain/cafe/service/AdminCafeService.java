package com.back.team11.domain.cafe.service;

import com.back.team11.domain.cafe.dto.AdminCafeResponse;
import com.back.team11.domain.cafe.dto.AdminCafeSearchCondition;
import com.back.team11.domain.cafe.dto.CafeCreateRequest;
import com.back.team11.domain.cafe.dto.PageResponse;
import com.back.team11.domain.cafe.entity.Cafe;
import com.back.team11.domain.cafe.entity.CafeType;
import com.back.team11.domain.cafe.entity.Franchise;
import com.back.team11.domain.cafe.repository.CafeRepository;
import com.back.team11.domain.global.exception.CustomException;
import com.back.team11.domain.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminCafeService {

    private final CafeRepository cafeRepository;

    @Transactional
    public AdminCafeResponse createCafe(CafeCreateRequest request) {

        // type과 franchise 일관성 검증
        validateFranchiseConsistency(request.getType(), request.getFranchise());

        Cafe cafe = Cafe.createByAdmin(
                request.getName(),
                request.getAddress(),
                request.getLatitude(),
                request.getLongitude(),
                request.getPhone(),
                request.getDescription(),
                request.getType(),
                request.getFranchise(),
                request.getHasToilet(),
                request.getHasOutlet(),
                request.getHasWifi(),
                request.getFloorCount(),
                request.getHasSeparateSpace(),
                request.getCongestionLevel(),
                request.getImageUrl()
        );

        return AdminCafeResponse.from(cafeRepository.save(cafe));
    }

    // INDIVIDUAL 타입 ↔ Franchise 브랜드 일관성 검증
    private void validateFranchiseConsistency(CafeType type, Franchise franchise) {
        boolean hasBrand = franchise != null && franchise != Franchise.NONE;

        if (type == CafeType.INDIVIDUAL && hasBrand) {
            // 개인 카페인데 브랜드가 선택된 경우
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        if (type == CafeType.FRANCHISE && !hasBrand) {
            // 프랜차이즈인데 브랜드가 NONE이거나 없는 경우
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }


    /**
     관리자 - 카페 목록 조회 (페이 징 및 필터링)
     **/
    @Transactional(readOnly = true)
    public PageResponse<AdminCafeResponse> getCafes(AdminCafeSearchCondition condition, int page) {
        // page는 클라이언트에서 1부터 들어오므로, JPA의 0-based index에 맞춰 1을 빼줌. 사이즈는 15.
        Pageable pageable = PageRequest.of(page - 1, 15); // 성능차 X

        // QueryDSL을 통해 조건에 맞는 Cafe Page 조회
        Page<Cafe> cafePage = cafeRepository.searchAdminCafes(condition, pageable);

        // Page<Cafe> 를 Page<AdminCafeResponse> 로 변환 후 PageResponse 객체로 매핑
        Page<AdminCafeResponse> dtoPage = cafePage.map(AdminCafeResponse::from);

        return PageResponse.of(dtoPage);
    }


    /**
     * 관리자 - 카페 상세 조회
     */
    @Transactional(readOnly = true)
    public AdminCafeResponse getCafe(Long cafeId) {
        // cafeId로 카페 조회, 존재하지 않으면 예외 발생
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new CustomException(ErrorCode.CAFE_NOT_FOUND));

        return AdminCafeResponse.from(cafe);
    }


}