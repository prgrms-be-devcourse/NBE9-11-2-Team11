package com.back.team11.domain.cafe.controller;

import com.back.team11.domain.cafe.dto.*;
import com.back.team11.domain.cafe.service.AdminCafeService;
import com.back.team11.domain.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/V1/admin")
@RequiredArgsConstructor
public class AdminCafeController {

    private final AdminCafeService adminCafeService;

    /**
     관리자 - 카페 정보 생성 (POST /api/V1/admin/cafe/post)
     **/
    @PostMapping("/cafe/post")
    public ResponseEntity<RsData<AdminCafeResponse>> createCafe(
            @RequestBody @Valid CafeCreateRequest request
    ) {
        AdminCafeResponse response = adminCafeService.createCafe(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RsData<>("카페 등록 성공", "201", response));
    }

    /**
     관리자 - 카페 전체 정보 조회 (GET /api/V1/admin/cafes)
     **/
    @GetMapping("/cafes") // 클래스 레벨이 /api/V1/admin/cafe 이므로 's'를 붙여 /cafes 로 매핑
    public ResponseEntity<RsData<PageResponse<AdminCafeResponse>>> getCafes(
            @ModelAttribute AdminCafeSearchCondition condition,
            @RequestParam(defaultValue = "1") int page
    ) {
        PageResponse<AdminCafeResponse> response = adminCafeService.getCafes(condition, page);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new RsData<>("카페 목록 조회 성공", "200", response));
    }

    // 관리자 - 카페 상세 조회
    // GET /api/V1/admin/cafe/{cafeId}
    @GetMapping("/cafe/{cafeId}")
    public ResponseEntity<RsData<AdminCafeResponse>> getCafe(
            @PathVariable Long cafeId
    ) {
        AdminCafeResponse response = adminCafeService.getCafe(cafeId);
        return ResponseEntity
                .ok(new RsData<>("카페 상세 조회 성공", "200", response));
    }

    /**
     * 관리자 - 카페 정보 수정 (PATCH /api/V1/admin/cafe/{cafeId})
     * 전송된 필드만 수정, null인 필드는 기존값 유지
     */
    @PatchMapping("/cafe/{cafeId}")
    public ResponseEntity<RsData<AdminCafeResponse>> updateCafe(
            @PathVariable Long cafeId,
            @RequestBody CafeUpdateRequest request
    ) {
        AdminCafeResponse response = adminCafeService.updateCafe(cafeId, request);
        return ResponseEntity
                .ok(new RsData<>("카페 정보 수정 성공", "200", response));
    }

    /**
     * 관리자 - 카페 정보 삭제 (DELETE /api/V1/admin/cafe/{cafeId})
     */
    @DeleteMapping("/cafe/{cafeId}")
    public ResponseEntity<RsData<Void>> deleteCafe(
            @PathVariable Long cafeId
    ) {
        adminCafeService.deleteCafe(cafeId);
        return ResponseEntity
                .ok(new RsData<>("카페 삭제 성공", "200"));
    }


    /**
     * 관리자 - 카페 승인 (PATCH /api/V1/admin/cafe/{cafeId}/approve)
     */
    @PatchMapping("/cafe/{cafeId}/approve")
    public ResponseEntity<RsData<AdminCafeResponse>> approveCafe(
            @PathVariable Long cafeId
    ) {
        AdminCafeResponse response = adminCafeService.approveCafe(cafeId);
        return ResponseEntity
                .ok(new RsData<>("카페 승인 성공", "200", response));
    }

    /**
     * 관리자 - 카페 승인 거부 (PATCH /api/V1/admin/cafe/{cafeId}/reject)
     */
    @PatchMapping("/cafe/{cafeId}/reject")
    public ResponseEntity<RsData<AdminCafeResponse>> rejectCafe(
            @PathVariable Long cafeId
    ) {
        AdminCafeResponse response = adminCafeService.rejectCafe(cafeId);
        return ResponseEntity
                .ok(new RsData<>("카페 승인 거부 성공", "200", response));
    }


}