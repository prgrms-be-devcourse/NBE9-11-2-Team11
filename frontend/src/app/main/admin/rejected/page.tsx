'use client';

import { useState } from 'react';
import { AdminCafe } from '@/types/admin';
import RejectedList from '@/components/admin/RejectedList';

export default function AdminRejectedPage() {

    // 승인 거절 카페 목록
    const [cafes, setCafes] = useState<AdminCafe[]>([]);
    // 나중에 API 연결하면 status=REJECTED 로 불러올 예정!!!


    return (
        <div className="flex min-h-screen">

            {/* 왼쪽 사이드바 */}
            <div className="w-40 border-r p-4 flex flex-col gap-4">
                <span className="text-sm text-gray-500">관리자 페이지</span>
                <a href="/main/admin/cafe" className="text-gray-500">카페 목록</a>
                <a href="/main/admin/pending" className="text-gray-500">승인 대기</a>
                <a href="/main/admin/rejected" className="font-medium">승인 거절</a>
            </div>

            {/* 오른쪽 메인 영역 */}
            <div className="flex-1 p-6">

                {/* 헤더 */}
                <h1 className="text-xl font-bold mb-6">승인 거절</h1>

                {/* 승인 거절 목록 컴포넌트 */}
                <RejectedList cafes={cafes} />

            </div>

        </div>
    );
}