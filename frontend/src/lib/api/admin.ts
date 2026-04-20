// 관리자 API 함수 추가

import { AdminCafe, CafeCreateRequest, CafeUpdateRequest, CafeStatus, PageResponse } from '@/types/admin';


// 카페 목록 조회
// GET /api/V1/admin/cafes
// status 있으면 → 필터링 (PENDING, REJECTED)
// status 없으면 → 전체 목록
export const fetchCafes = async (status?: CafeStatus): Promise<PageResponse<AdminCafe>> => {
    const url = status ? `/api/V1/admin/cafes?status=${status}` : `/api/V1/admin/cafes`;
    const res = await fetch(url, {
        credentials: 'include',
    });
    if (!res.ok) throw new Error('카페 목록 조회 실패');
    const data = await res.json();
    // 백엔드 응답 구조: { msg, resultCode, data }
    // 실제 데이터는 data.data로 반환
    return data.data;
};


// 카페 등록
// POST /api/V1/admin/cafe/post
export const createCafe = async (request: CafeCreateRequest): Promise<AdminCafe> => {
    const res = await fetch('/api/V1/admin/cafe/post', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(request),
    });
    if (!res.ok) throw new Error('카페 등록 실패');
    const data = await res.json();
    return data.data;
};


// 카페 수정
// PATCH /api/V1/admin/cafe/{cafeId}
export const updateCafe = async (cafeId: number, request: CafeUpdateRequest): Promise<AdminCafe> => {
    const res = await fetch(`/api/V1/admin/cafe/${cafeId}`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(request),
    });
    if (!res.ok) throw new Error('카페 수정 실패');
    const data = await res.json();
    return data.data;
};


// 카페 삭제
// DELETE /api/V1/admin/cafe/{cafeId}
export const deleteCafe = async (cafeId: number): Promise<void> => {
    const res = await fetch(`/api/V1/admin/cafe/${cafeId}`, {
        method: 'DELETE',
        credentials: 'include',
    });
    if (!res.ok) throw new Error('카페 삭제 실패');
};


// 카페 승인
// PATCH /api/V1/admin/cafe/{cafeId}/approve
export const approveCafe = async (cafeId: number): Promise<AdminCafe> => {
    const res = await fetch(`/api/V1/admin/cafe/${cafeId}/approve`, {
        method: 'PATCH',
        credentials: 'include',
    });
    if (!res.ok) throw new Error('카페 승인 실패');
    const data = await res.json();
    return data.data;
};


// 카페 거절
// PATCH /api/V1/admin/cafe/{cafeId}/reject
export const rejectCafe = async (cafeId: number): Promise<AdminCafe> => {
    const res = await fetch(`/api/V1/admin/cafe/${cafeId}/reject`, {
        method: 'PATCH',
        credentials: 'include',
    });
    if (!res.ok) throw new Error('카페 거절 실패');
    const data = await res.json();
    return data.data;
};