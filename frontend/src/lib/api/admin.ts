// 관리자 API 함수 추가

import { AdminCafe, CafeCreateRequest, CafeUpdateRequest, CafeStatus, PageResponse } from '@/types/admin';
import { apiFetch } from '@/app/api/client'; // apiFetch: 팀원이 만든 공통 fetch 함수 (baseURL + credentials: include 처리)

// 카페 목록 조회
// GET /api/V1/admin/cafes
// status 있으면 → 필터링 (PENDING, REJECTED)
// status 없으면 → 전체 목록
export const fetchCafes = async (status?: CafeStatus, page: number = 0): Promise<PageResponse<AdminCafe>> => {
    // page = 0 → 기본값은 0페이지 (첫 번째 페이지)
    const url = status
        ? `/api/V1/admin/cafes?status=${status}&page=${page}`
        : `/api/V1/admin/cafes?page=${page}`;
    const data = await apiFetch(url);
    return data.data;
};

// 카페 등록
// POST /api/V1/admin/cafe/post
export const createCafe = async (request: CafeCreateRequest): Promise<AdminCafe> => {
    const data = await apiFetch('/api/V1/admin/cafe/post', {
        method: 'POST',
        body: JSON.stringify(request),
    });
    return data.data;
};

// 카페 수정
// PATCH /api/V1/admin/cafe/{cafeId}
export const updateCafe = async (cafeId: number, request: CafeUpdateRequest): Promise<AdminCafe> => {
    const data = await apiFetch(`/api/V1/admin/cafe/${cafeId}`, {
        method: 'PATCH',
        body: JSON.stringify(request),
    });
    return data.data;
};

// 카페 삭제
// DELETE /api/V1/admin/cafe/{cafeId}
export const deleteCafe = async (cafeId: number): Promise<void> => {
    await apiFetch(`/api/V1/admin/cafe/${cafeId}`, {
        method: 'DELETE',
    });
};

// 카페 승인
// PATCH /api/V1/admin/cafe/{cafeId}/approve
export const approveCafe = async (cafeId: number): Promise<AdminCafe> => {
    const data = await apiFetch(`/api/V1/admin/cafe/${cafeId}/approve`, {
        method: 'PATCH',
    });
    return data.data;
};

// 카페 거절
// PATCH /api/V1/admin/cafe/{cafeId}/reject
export const rejectCafe = async (cafeId: number): Promise<AdminCafe> => {
    const data = await apiFetch(`/api/V1/admin/cafe/${cafeId}/reject`, {
        method: 'PATCH',
    });
    return data.data;
};