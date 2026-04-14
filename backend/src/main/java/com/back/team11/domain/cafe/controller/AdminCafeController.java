package com.back.team11.domain.cafe.controller;

import com.back.team11.domain.cafe.dto.AdminCafeResponse;
import com.back.team11.domain.cafe.dto.CafeCreateRequest;
import com.back.team11.domain.cafe.service.AdminCafeService;
import com.back.team11.domain.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/V1/admin/cafe")
@RequiredArgsConstructor
public class AdminCafeController {

    private final AdminCafeService adminCafeService;

    /**
     관리자 - 카페 정보 생성 (POST /api/V1/admin/cafe/post)
     **/
    @PostMapping("/post")
    public ResponseEntity<RsData<AdminCafeResponse>> createCafe(
            @RequestBody @Valid CafeCreateRequest request
    ) {
        AdminCafeResponse response = adminCafeService.createCafe(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RsData<>("카페 등록 성공", "201", response));
    }

}