'use client';

import { useState, useEffect} from 'react';
import { AdminCafe, CafeCreateRequest, CafeUpdateRequest } from '@/types/admin';
import { fetchCafes, createCafe, updateCafe, deleteCafe } from '@/lib/api/admin'; // API 함수 가져오기
import CafeList from '@/components/admin/CafeList';
import CafeCreateModal from '@/components/admin/CafeCreateModal';
import CafeEditModal from '@/components/admin/CafeEditModal';

export default function AdminCafePage() {

    // 상태 관리
    // 카페 목록 데이터
    const [cafes, setCafes] = useState<AdminCafe[]>([]);

    // 등록 모달 열림/닫힘 여부 (true = 열림, false = 닫힘)
    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);

    // 수정 모달에서 쓸 선택된 카페 (null = 선택 안 됨)
    const [selectedCafe, setSelectedCafe] = useState<AdminCafe | null>(null);

    // 로딩 상태
    const [isLoading, setIsLoading] = useState(false);


    // 카페 목록 조회
    const loadCafes = async () => {
        setIsLoading(true);
        try {
            const data = await fetchCafes();
            setCafes(data.content);
        } catch (error) {
            console.log('카페 목록 조회 실패:', error);
        } finally {
            setIsLoading(false);
        }
    };

    // 카페 등록 처리
    const handleCreate = async (data: CafeCreateRequest) => {
        try {
            await createCafe(data); // API 호출
            alert('카페 등록 성공');
            setIsCreateModalOpen(false);
            loadCafes(); // 목록 새로고침
        } catch (error) {
            console.log('카페 등록 실패:', error);
            alert('카페 등록 실패');
        }
    };

    // 카페 수정 처리
    const handleEdit = async (cafeId: number, data: CafeUpdateRequest) => {
        try {
            await updateCafe(cafeId, data); // API 호출
            alert('카페 정보 수정 성공');
            setSelectedCafe(null);
            loadCafes(); // 목록 새로고침
        } catch (error) {
            console.log('카페 수정 실패:', error);
            alert('카페 정보 수정 실패');
        }
    };


    // 카페 삭제 처리
    const handleDelete = async (cafeId: number) => {
        if (!confirm('정말 삭제할까요?')) return;
        try {
            await deleteCafe(cafeId); // 추가! API 호출
            alert('카페 삭제 성공');
            loadCafes(); // 목록 새로고침
        } catch (error) {
            console.log('카페 삭제 실패:', error);
            alert('카페 삭제 실패');
        }
    };


    // 페이지 처음 열릴 때 목록 자동으로 불러오기
    useEffect(() => {
        loadCafes();
    }, []);



    return (
        <div className="flex min-h-screen">

            {/* 왼쪽 사이드바 */}
            <div className="w-40 border-r p-4 flex flex-col gap-4">
                <span className="text-sm text-gray-500">관리자 페이지</span>
                <a href="/main/admin/cafe" className="font-medium">카페 목록</a>
                <a href="/main/admin/pending" className="text-gray-500">승인 대기</a>
                <a href="/main/admin/rejected" className="text-gray-500">승인 거절</a>
            </div>

            {/* 오른쪽 메인 영역 */}
            <div className="flex-1 p-6">

                {/* 헤더 */}
                <div className="flex items-center justify-between mb-6">
                    <h1 className="text-xl font-bold">카페 목록</h1>
                    {/* 등록하기 버튼 */}
                    <button
                        onClick={() => setIsCreateModalOpen(true)}
                        // setIsCreateModalOpen(true) = 등록 모달 열기
                        className="px-4 py-2 bg-black text-white rounded hover:bg-gray-800"
                    >
                        등록하기
                    </button>
                </div>

                {/* 카페 목록 컴포넌트 */}
                <CafeList
                    cafes={cafes}
                    onEdit={(cafe) => setSelectedCafe(cafe)}
                    // 수정 버튼 누르면 selectedCafe 에 카페 데이터 저장 → 수정 모달 열림
                    onDelete={handleDelete}
                />

            </div>

            {/* 등록 모달 - isCreateModalOpen 이 true 일 때만 보임 */}
            {isCreateModalOpen && (
                <CafeCreateModal
                    onClose={() => setIsCreateModalOpen(false)}
                    onSubmit={handleCreate}
                />
            )}

            {/* 수정 모달 - selectedCafe 가 있을 때만 보임 */}
            {selectedCafe && (
                <CafeEditModal
                    cafe={selectedCafe}
                    onClose={() => setSelectedCafe(null)}
                    onSubmit={handleEdit}
                />
            )}

        </div>
    );
}