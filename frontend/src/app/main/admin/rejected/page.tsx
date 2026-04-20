'use client';

import { useState, useEffect } from 'react';
import { AdminCafe } from '@/types/admin';
import { fetchCafes } from '@/lib/api/admin';
import RejectedList from '@/components/admin/RejectedList';

export default function AdminRejectedPage() {

    // 승인 거절 카페 목록
    const [cafes, setCafes] = useState<AdminCafe[]>([]);

    // 로딩 상태
    const [isLoading, setIsLoading] = useState(false);


    // 승인 거절 목록 조회
    const loadRejectedCafes = async () => {
        setIsLoading(true);
        try {
            const data = await fetchCafes('REJECTED'); // status=REJECTED 로 필터링
            setCafes(data.content);
        } catch (error) {
            console.log('승인 거절 목록 조회 실패:', error);
        } finally {
            setIsLoading(false);
        }
    };


    // 페이지 처음 열릴 때 목록 자동으로 불러오기
    useEffect(() => {
        loadRejectedCafes();
    }, []);


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

                {/* 로딩 중일 때 표시 */}
                {isLoading ? (
                    <p className="text-gray-500">불러오는 중...</p>
                ) : (
                    <RejectedList cafes={cafes} />
                )}

            </div>

        </div>
    );
}