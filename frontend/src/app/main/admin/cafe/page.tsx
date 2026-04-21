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

    // 상세 보기에서 쓸 선택된 카페 (null = 선택 안 됨)
    const [detailCafe, setDetailCafe] = useState<AdminCafe | null>(null);

    // 현재 페이지 번호 (1부터 시작)
    const [currentPage, setCurrentPage] = useState(1);
    // 전체 페이지 수
    const [totalPages, setTotalPages] = useState(0);

    // 검색어 상태
    const [searchName, setSearchName] = useState('');


    // 카페 목록 조회
    const loadCafes = async (page: number = 1, name: string = '') => {
        setIsLoading(true);
        try {
            const data = await fetchCafes(undefined, page, name);
            setCafes(data.content);
            setTotalPages(data.totalPages); // 전체 페이지 수 저장
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
            await updateCafe(cafeId, data);
            alert('카페 정보 수정 성공');
            setSelectedCafe(null);
            loadCafes();
        } catch (error) {
            console.log('카페 수정 실패:', error);
            alert('카페 정보 수정 실패');
        }
    };


    // 카페 삭제 처리
    const handleDelete = async (cafeId: number) => {
        if (!confirm('정말 삭제할까요?')) return;
        try {
            await deleteCafe(cafeId);
            alert('카페 삭제 성공');
            loadCafes();
        } catch (error) {
            console.log('카페 삭제 실패:', error);
            alert('카페 삭제 실패');
        }
    };


    // 페이지 처음 열릴 때 목록 자동으로 불러오기 + 페이징
    useEffect(() => {
        loadCafes(currentPage, searchName);
    }, [currentPage]);


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

                {/* 검색창 */}
                <div className="flex gap-2 mb-4">
                    <input
                        type="text"
                        value={searchName}
                        onChange={(e) => setSearchName(e.target.value)}
                        placeholder="카페 이름 검색"
                        className="flex-1 p-2 border rounded"
                    />
                    <button
                        onClick={() => {
                            setCurrentPage(1);
                            loadCafes(1, searchName);
                        }}
                        className="px-4 py-2 bg-black text-white rounded hover:bg-gray-800"
                    >
                        검색
                    </button>
                </div>

                {/* 카페 목록 컴포넌트 */}
                <CafeList
                    cafes={cafes}
                    onEdit={(cafe) => setSelectedCafe(cafe)}
                    onDelete={handleDelete}
                    onDetail={(cafe) => setDetailCafe(cafe)}
                />
                {/* 페이징 버튼 */}
                <div className="flex items-center justify-center gap-2 mt-6">
                    {/* 이전 버튼 */}
                    <button
                        onClick={() => setCurrentPage(currentPage - 1)}
                        disabled={currentPage === 1}
                        // disabled = 첫 페이지면 버튼 비활성화
                        className="px-3 py-1 border rounded disabled:opacity-30 hover:bg-gray-100"
                    >
                        이전
                    </button>

                    {/* 페이지 번호들 */}
                    {Array.from({ length: totalPages }, (_, i) => (
                        <button
                            key={i+1}
                            onClick={() => setCurrentPage(i+1)}
                            className={`px-3 py-1 border rounded hover:bg-gray-100 ${currentPage === i ? 'bg-black text-white' : ''}`}
                            // 현재 페이지면 검은색으로 표시
                        >
                            {i + 1}
                        </button>
                    ))}

                    {/* 다음 버튼 */}
                    <button
                        onClick={() => setCurrentPage(currentPage + 1)}
                        disabled={currentPage >= totalPages}
                        // disabled = 마지막 페이지면 버튼 비활성화
                        className="px-3 py-1 border rounded disabled:opacity-30 hover:bg-gray-100"
                    >
                        다음
                    </button>
                </div>

            </div>

            {/* 등록 모달 - isCreateModalOpen 이 true 일 때만 보임 */}
            {isCreateModalOpen && (
                <CafeCreateModal
                    onClose={() => setIsCreateModalOpen(false)}
                    onSubmit={handleCreate}
                />
            )}

            {/* 상세 모달 - detailCafe 가 있을 때만 보임 */}
            {detailCafe && (
                <div className="fixed inset-0 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg p-6 w-full max-w-md max-h-[90vh] overflow-y-auto border-2 border-gray-300 shadow-xl">

                        {/* 헤더 */}
                        <div className="flex items-center justify-between mb-4">
                            <h2 className="text-lg font-bold">카페 상세 정보</h2>
                            <button onClick={() => setDetailCafe(null)} className="text-gray-500 hover:text-black">✕</button>
                        </div>

                        {/* 이미지 */}
                        {detailCafe.imageUrl && (
                            <img src={detailCafe.imageUrl} alt="카페 이미지" className="w-full rounded mb-4" />
                        )}

                        {/* 상세 정보 */}
                        <div className="flex flex-col gap-3 text-sm">

                            <div className="flex justify-between border-b pb-2">
                                <span className="text-gray-500">카페 이름</span>
                                <span className="font-medium">{detailCafe.name}</span>
                            </div>
                            <div className="flex justify-between border-b pb-2">
                                <span className="text-gray-500">주소</span>
                                <span className="text-right">{detailCafe.address}</span>
                            </div>
                            <div className="flex justify-between border-b pb-2">
                                <span className="text-gray-500">전화번호</span>
                                <span>{detailCafe.phone ?? '-'}</span>
                            </div>
                            <div className="flex justify-between border-b pb-2">
                                <span className="text-gray-500">설명</span>
                                <span>{detailCafe.description ?? '-'}</span>
                            </div>
                            <div className="flex justify-between border-b pb-2">
                                <span className="text-gray-500">카페 종류</span>
                                <span>{detailCafe.type === 'FRANCHISE' ? '프랜차이즈' : '개인 카페'}</span>
                            </div>
                            {detailCafe.type === 'FRANCHISE' && (
                                <div className="flex justify-between border-b pb-2">
                                    <span className="text-gray-500">프랜차이즈</span>
                                    <span>
                            {detailCafe.franchise === 'STARBUCKS' ? '스타벅스' :
                                detailCafe.franchise === 'MEGA_COFFEE' ? '메가커피' : '-'}
                        </span>
                                </div>
                            )}
                            <div className="flex justify-between border-b pb-2">
                                <span className="text-gray-500">층수</span>
                                <span>
                        {detailCafe.floorCount === 'ONE' ? '1층' :
                            detailCafe.floorCount === 'TWO' ? '2층' : '3층 이상'}
                    </span>
                            </div>
                            <div className="flex justify-between border-b pb-2">
                                <span className="text-gray-500">혼잡도</span>
                                <span>
                        {detailCafe.congestionLevel === 'LOW' ? '여유' :
                            detailCafe.congestionLevel === 'MEDIUM' ? '보통' : '혼잡'}
                    </span>
                            </div>

                            {/* 편의시설 */}
                            <div className="mt-1">
                                <p className="text-gray-500 mb-2">편의시설</p>
                                <div className="flex gap-2 flex-wrap">
                                    {detailCafe.hasToilet && <span className="px-2 py-1 bg-gray-100 rounded">🚻 화장실</span>}
                                    {detailCafe.hasOutlet && <span className="px-2 py-1 bg-gray-100 rounded">🔌 콘센트</span>}
                                    {detailCafe.hasWifi && <span className="px-2 py-1 bg-gray-100 rounded">📶 와이파이</span>}
                                    {detailCafe.hasSeparateSpace && <span className="px-2 py-1 bg-gray-100 rounded">📚 공부 공간</span>}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
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