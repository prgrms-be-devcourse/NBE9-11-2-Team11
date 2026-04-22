'use client';

interface Props {
    currentPage: number;   // 현재 페이지 번호
    totalPages: number;    // 전체 페이지 수
    onPageChange: (page: number) => void; // 페이지 변경 시 호출
}

export default function PaginationButtons({ currentPage, totalPages, onPageChange }: Props) {
    return (
        <div className="flex items-center justify-center gap-2 mt-6">
            {/* 이전 버튼 */}
            <button
                onClick={() => onPageChange(currentPage - 1)}
                disabled={currentPage === 1}
                className="px-3 py-1 border rounded disabled:opacity-30 hover:bg-gray-100"
            >
                이전
            </button>

            {/* 페이지 번호들 */}
            {Array.from({ length: totalPages }, (_, i) => (
                <button
                    key={i + 1}
                    onClick={() => onPageChange(i + 1)}
                    className={`px-3 py-1 border rounded hover:bg-gray-100 ${currentPage === i + 1 ? 'bg-black text-white' : ''}`}
                >
                    {i + 1}
                </button>
            ))}

            {/* 다음 버튼 */}
            <button
                onClick={() => onPageChange(currentPage + 1)}
                disabled={currentPage >= totalPages}
                className="px-3 py-1 border rounded disabled:opacity-30 hover:bg-gray-100"
            >
                다음
            </button>
        </div>
    );
}