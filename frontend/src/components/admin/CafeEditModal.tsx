'use client';

import { useState } from 'react';
import { AdminCafe, CafeUpdateRequest, CafeType, FranchiseType, FloorCount, CongestionLevel } from '@/types/admin';

interface Props {
    cafe: AdminCafe;                              // 수정할 카페 기존 데이터
    onClose: () => void;                          // 모달 닫기
    onSubmit: (cafeId: number, data: CafeUpdateRequest) => void; // 수정하기 버튼 눌렀을 때
}

export default function CafeEditModal({ cafe, onClose, onSubmit }: Props) {

    // 입력값 상태 관리
    const [name, setName] = useState(cafe.name);
    const [address, setAddress] = useState(cafe.address);
    const [phone, setPhone] = useState(cafe.phone ?? '');
    // ?? '' 는 null 이면 빈 문자열로 대체한다는 것
    const [description, setDescription] = useState(cafe.description ?? '');
    const [type, setType] = useState<CafeType>(cafe.type);
    const [franchise, setFranchise] = useState<FranchiseType>(cafe.franchise);
    const [hasToilet, setHasToilet] = useState(cafe.hasToilet);
    const [hasOutlet, setHasOutlet] = useState(cafe.hasOutlet);
    const [hasWifi, setHasWifi] = useState(cafe.hasWifi);
    const [hasSeparateSpace, setHasSeparateSpace] = useState(cafe.hasSeparateSpace);
    const [floorCount, setFloorCount] = useState<FloorCount>(cafe.floorCount);
    const [congestionLevel, setCongestionLevel] = useState<CongestionLevel>(cafe.congestionLevel);
    const [imageUrl, setImageUrl] = useState(cafe.imageUrl ?? '');


    // 수정하기 버튼 눌렀을 때 실행되는 함수
    const handleSubmit = () => {
        if (!name || !address) {
            alert('카페 이름과 주소는 필수입니다!');
            return;
        }

        // 부모한테 cafeId 랑 수정된 데이터 전달
        onSubmit(cafe.cafeId, {
            name,
            address,
            phone: phone || undefined,
            description: description || undefined,
            type,
            franchise,
            hasToilet,
            hasOutlet,
            hasWifi,
            hasSeparateSpace,
            floorCount,
            congestionLevel,
            imageUrl: imageUrl || undefined,
        });
    };

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white rounded-lg p-6 w-full max-w-md max-h-[90vh] overflow-y-auto">

                {/* 모달 헤더 */}
                <div className="flex items-center justify-between mb-4">
                    <h2 className="text-lg font-bold">카페 수정</h2>
                    <button onClick={onClose} className="text-gray-500 hover:text-black">✕</button>
                </div>

                {/* 입력 폼 */}
                <div className="flex flex-col gap-3">

                    <div>
                        <label className="text-sm font-medium">카페 이름 *</label>
                        <input
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            className="w-full mt-1 p-2 border rounded"
                        />
                    </div>

                    <div>
                        <label className="text-sm font-medium">주소 *</label>
                        <input
                            type="text"
                            value={address}
                            onChange={(e) => setAddress(e.target.value)}
                            className="w-full mt-1 p-2 border rounded"
                        />
                    </div>

                    <div>
                        <label className="text-sm font-medium">전화번호</label>
                        <input
                            type="text"
                            value={phone}
                            onChange={(e) => setPhone(e.target.value)}
                            className="w-full mt-1 p-2 border rounded"
                        />
                    </div>

                    <div>
                        <label className="text-sm font-medium">설명</label>
                        <input
                            type="text"
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                            className="w-full mt-1 p-2 border rounded"
                        />
                    </div>

                    <div>
                        <label className="text-sm font-medium">카페 종류 *</label>
                        <select
                            value={type}
                            onChange={(e) => setType(e.target.value as CafeType)}
                            className="w-full mt-1 p-2 border rounded"
                        >
                            <option value="INDIVIDUAL">개인 카페</option>
                            <option value="FRANCHISE">프랜차이즈</option>
                        </select>
                    </div>

                    <div>
                        <label className="text-sm font-medium">프랜차이즈 *</label>
                        <select
                            value={franchise}
                            onChange={(e) => setFranchise(e.target.value as FranchiseType)}
                            className="w-full mt-1 p-2 border rounded"
                        >
                            <option value="NONE">해당 없음</option>
                            <option value="STARBUCKS">스타벅스</option>
                            <option value="MEGA_COFFEE">메가커피</option>
                        </select>
                    </div>

                    <div>
                        <label className="text-sm font-medium">층수 *</label>
                        <select
                            value={floorCount}
                            onChange={(e) => setFloorCount(e.target.value as FloorCount)}
                            className="w-full mt-1 p-2 border rounded"
                        >
                            <option value="ONE">1층</option>
                            <option value="TWO">2층</option>
                            <option value="THREE_OR_MORE">3층 이상</option>
                        </select>
                    </div>

                    <div>
                        <label className="text-sm font-medium">혼잡도 *</label>
                        <select
                            value={congestionLevel}
                            onChange={(e) => setCongestionLevel(e.target.value as CongestionLevel)}
                            className="w-full mt-1 p-2 border rounded"
                        >
                            <option value="LOW">여유</option>
                            <option value="MEDIUM">보통</option>
                            <option value="HIGH">혼잡</option>
                        </select>
                    </div>

                    <div className="flex items-center justify-between">
                        <label className="text-sm font-medium">화장실 유무 *</label>
                        <input
                            type="checkbox"
                            checked={hasToilet}
                            onChange={(e) => setHasToilet(e.target.checked)}
                            className="w-4 h-4"
                        />
                    </div>

                    <div className="flex items-center justify-between">
                        <label className="text-sm font-medium">콘센트 유무 *</label>
                        <input
                            type="checkbox"
                            checked={hasOutlet}
                            onChange={(e) => setHasOutlet(e.target.checked)}
                            className="w-4 h-4"
                        />
                    </div>

                    <div className="flex items-center justify-between">
                        <label className="text-sm font-medium">와이파이 유무 *</label>
                        <input
                            type="checkbox"
                            checked={hasWifi}
                            onChange={(e) => setHasWifi(e.target.checked)}
                            className="w-4 h-4"
                        />
                    </div>

                    <div className="flex items-center justify-between">
                        <label className="text-sm font-medium">공부 공간 유무 *</label>
                        <input
                            type="checkbox"
                            checked={hasSeparateSpace}
                            onChange={(e) => setHasSeparateSpace(e.target.checked)}
                            className="w-4 h-4"
                        />
                    </div>

                    <div>
                        <label className="text-sm font-medium">이미지 주소</label>
                        <input
                            type="text"
                            value={imageUrl}
                            onChange={(e) => setImageUrl(e.target.value)}
                            className="w-full mt-1 p-2 border rounded"
                        />
                    </div>

                </div>

                {/* 수정하기 버튼 */}
                <button
                    onClick={handleSubmit}
                    className="w-full mt-6 p-3 bg-black text-white rounded hover:bg-gray-800"
                >
                    수정하기
                </button>

            </div>
        </div>
    );
}