package com.back.team11.domain.cafe.controller;

import com.back.team11.domain.cafe.dto.CafeDetailResponse;
import com.back.team11.domain.cafe.dto.CafeListResponse;
import com.back.team11.domain.cafe.dto.CafeResponse;
import com.back.team11.domain.cafe.repository.CafeSearchCondition;
import com.back.team11.domain.cafe.service.CafeService;
import com.back.team11.domain.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/V1/cafe")
public class CafeSearchController {

    private final CafeService cafeService;

    @GetMapping
    public ResponseEntity<RsData<List<CafeListResponse>>> searchCafes(
            @Valid @ModelAttribute CafeSearchCondition condition
    ){
        List<CafeListResponse> cafes = cafeService.searchCafes(condition);
        return ResponseEntity.ok(new RsData<>("카페 목록 조회 성공","200", cafes));
    }


    @GetMapping("/{cafeId}")
    public ResponseEntity<RsData<CafeDetailResponse>> getCafe(
            @PathVariable Long cafeId
    ){
        CafeDetailResponse cafe = cafeService.getCafe(cafeId);
        return ResponseEntity.ok(new RsData<>("카페 조회 성공", "200", cafe));
    }
}
