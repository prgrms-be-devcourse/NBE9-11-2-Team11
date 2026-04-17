package com.back.team11.domain.cafe.controller;

import com.back.team11.domain.cafe.dto.CafeReportRequest;
import com.back.team11.domain.cafe.dto.CafeResponse;
import com.back.team11.domain.cafe.service.CafeService;
import com.back.team11.domain.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/V1/cafe")
@RequiredArgsConstructor
public class CafeController {

    private final CafeService cafeService;

    /**
     * 사용자 - 카페 정보 생성 요청(제보) (POST /api/V1/cafe/report)
     * JWT 쿠키에서 로그인한 사용자 ID를 자동으로 추출하여 제보자로 연결
     */
    @PostMapping("/report")
    public ResponseEntity<RsData<CafeResponse>> reportCafe(
            @AuthenticationPrincipal Long memberId,
            @RequestBody @Valid CafeReportRequest request
    ) {
        CafeResponse response = cafeService.reportCafe(memberId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RsData<>("카페 제보가 접수되었습니다.", "201", response));
    }
}