package com.back.team11.domain.cafe.service;

import com.back.team11.domain.cafe.dto.CafeResponse;
import com.back.team11.domain.cafe.entity.Cafe;
import com.back.team11.domain.cafe.repository.CafeRepository;
import com.back.team11.domain.cafe.repository.CafeSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {
    private final CafeRepository cafeRepository;

    // 카페 목록 조회(지역 검색 + 필터링)
    public List<CafeResponse> searchCafes(CafeSearchCondition condition) {
        List<Cafe> cafes = cafeRepository.searchCafes(condition);
        return cafes.stream()
                .map(CafeResponse::from)
                .toList();
    }

}
