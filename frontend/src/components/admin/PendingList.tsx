'use client';

import { AdminCafe } from '@/types/admin';

// PendingList 가 받는 데이터
interface Props {
    cafes: AdminCafe[];                    // 승인 대기 카페 목록
    onApprove: (cafeId: number) => void;   // 승인 버튼 눌렀을 때
    onReject: (cafeId: number) => void;    // 거절 버튼 눌렀을 때
}

export default function PendingList({ cafes, onApprove, onReject }: Props) {
    return (
        <div className="w-full">

            {/* 카페 총 개수 */}
            <p className="mb-4 text-sm text-gray-500">
                총 {cafes.length}곳
            </p>

            {/* 승인 대기 목록 */}
            <div className="flex flex-col gap-2">
                {cafes.map((cafe) => (
                    <div
                        key={cafe.cafeId}
                        className="flex items-center justify-between p-4 border rounded-lg"
                    >
                        {/* 카페 이름 */}
                        <span className="font-medium">{cafe.name}</span>

                        {/* 승인 / 거절 버튼 */}
                        <div className="flex gap-2">
                            <button
                                onClick={() => onApprove(cafe.cafeId)}
                                className="px-3 py-1 text-sm border rounded text-blue-500 hover:bg-blue-50"
                            >
                                승인
                            </button>
                            <button
                                onClick={() => onReject(cafe.cafeId)}
                                className="px-3 py-1 text-sm border rounded text-red-500 hover:bg-red-50"
                            >
                                거절
                            </button>
                        </div>
                    </div>
                ))}
            </div>

        </div>
    );
}