'use client';

import { useState } from 'react';
import { AdminCafe } from '@/types/admin';
import PendingList from '@/components/admin/PendingList';

export default function AdminPendingPage() {

    // 승인 대기 카페 목록
    const [cafes, setCafes] = useState<AdminCafe[]>([]);
    // 나중에 API 연결하면 status=PENDING 으로 불러올 예정


    // 승인 처리
    const handleApprove = (cafeId: number) => {
        // TODO: API 연결할 부분
        if (!confirm('승인하시겠습니까?')) return;
        console.log('승인 카페 ID:', cafeId);
    };


    // 거절 처리
    const handleReject = (cafeId: number) => {
        // TODO: API 연결할 부분
        if (!confirm('거절하시겠습니까?')) return;
        console.log('거절 카페 ID:', cafeId);
    };


    return (
        <div className="flex min-h-screen">

            {/* 왼쪽 사이드바 */}
            <div className="w-40 border-r p-4 flex flex-col gap-4">
                <span className="text-sm text-gray-500">관리자 페이지</span>
                <a href="/main/admin/cafe" className="text-gray-500">카페 목록</a>
                <a href="/main/admin/pending" className="font-medium">승인 대기</a>
                <a href="/main/admin/rejected" className="text-gray-500">승인 거절</a>
            </div>

            {/* 오른쪽 메인 영역 */}
            <div className="flex-1 p-6">

                {/* 헤더 */}
                <h1 className="text-xl font-bold mb-6">승인 대기</h1>

                {/* 승인 대기 목록 컴포넌트 */}
                <PendingList
                    cafes={cafes}
                    onApprove={handleApprove}
                    onReject={handleReject}
                />

            </div>

        </div>
    );
}