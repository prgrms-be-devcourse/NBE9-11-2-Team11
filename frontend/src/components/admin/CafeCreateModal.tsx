'use client';

import { useState } from 'react'; // useState = 값이 바뀌는 데이터를 저장하는 도구
// 카페 생성 요청에 사용하는 타입들 import
import { CafeCreateRequest, CafeType, FranchiseType, FloorCount, CongestionLevel } from '@/types/admin';

interface Props {
    onClose: () => void;                        // 모달 닫기 버튼 눌렀을 때
    onSubmit: (data: CafeCreateRequest) => void; // 등록하기 버튼 눌렀을 때
}

export default function CafeCreateModal({ onClose, onSubmit }: Props) {

    const [name, setName] = useState('');           // 카페 이름
    const [address, setAddress] = useState('');     // 주소
    const [latitude, setLatitude] = useState(0);   // 위도
    const [longitude, setLongitude] = useState(0); // 경도
    const [phone, setPhone] = useState('');         // 전화번호
    const [description, setDescription] = useState(''); // 설명
    const [type, setType] = useState<CafeType>('INDIVIDUAL');           // 카페 종류
    const [franchise, setFranchise] = useState<FranchiseType>('NONE');  // 프랜차이즈
    const [hasToilet, setHasToilet] = useState(false);        // 화장실 여부
    const [hasOutlet, setHasOutlet] = useState(false);        // 콘센트 여부
    const [hasWifi, setHasWifi] = useState(false);            // 와이파이 여부
    const [hasSeparateSpace, setHasSeparateSpace] = useState(false); // 독립공간 여부
    const [floorCount, setFloorCount] = useState<FloorCount>('ONE');           // 층수
    const [congestionLevel, setCongestionLevel] = useState<CongestionLevel>('LOW'); // 혼잡도
    const [imageUrl, setImageUrl] = useState('');   // 이미지 주소


    // 주소 검색 버튼 눌렀을 때 실행되는 함수
    // ReportModal.tsx 참고
    const handleAddressSearch = () => {
        new window.daum.Postcode({
            oncomplete: async (data: any) => {
                const selectedAddress = data.roadAddress || data.jibunAddress;
                // roadAddress = 도로명 주소, jibunAddress = 지번 주소

                // /api/search 로 주소 → 위도/경도 변환
                const res = await fetch(`/api/search?query=${encodeURIComponent(selectedAddress)}`);
                const result = await res.json();
                const doc = result.documents?.[0];

                setAddress(selectedAddress);
                setLatitude(doc ? parseFloat(doc.y) : 0);   // y = 위도
                setLongitude(doc ? parseFloat(doc.x) : 0);  // x = 경도
            },
        }).open();
    };

    // 등록하기 버튼 눌렀을 때 실행되는 함수
    const handleSubmit = () => {
        // 필수값 체크 (백엔드 @NotBlank, @NotNull 에 해당)
        if (!name || !address || !phone) {
            alert('카페 이름과,주소, 전화번호는 필수입니다!');
            return;
        }

        // 부모한테 입력값 전달
        onSubmit({
            name,
            address,
            phone: phone,
            description: description || undefined,   // 빈 문자열이면 undefined 로 보내기 (선택값이라)
            type,
            franchise,
            hasToilet,
            hasOutlet,
            hasWifi,
            hasSeparateSpace,
            floorCount,
            congestionLevel,
            imageUrl: imageUrl || undefined,
            latitude,
            longitude,
        });
    };

    return (
        // 모달 배경 (클릭하면 닫힘)
        <div className="fixed inset-0 flex items-center justify-center z-50">
            {/* 모달 본체 */}
            <div className="bg-white rounded-lg p-6 w-full max-w-md max-h-[90vh] overflow-y-auto border-2 border-gray-300 shadow-xl">

                {/* 모달 헤더 */}
                <div className="flex items-center justify-between mb-4">
                    <h2 className="text-lg font-bold">카페 등록</h2>
                    <button onClick={onClose} className="text-gray-500 hover:text-black">✕</button>
                </div>

                {/* 입력 폼 */}
                <div className="flex flex-col gap-3">

                    {/* 카페 이름 - 필수 */}
                    <div>
                        <label className="text-sm font-medium">카페 이름 *</label>
                        <input
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            placeholder="OO 카페"
                            className="w-full mt-1 p-2 border rounded"
                        />
                    </div>

                    {/* 주소 - 필수 */}
                    <div>
                        <label className="text-sm font-medium">주소 *</label>
                        <div className="flex gap-2 mt-1">
                            <input
                                type="text"
                                value={address}
                                readOnly
                                placeholder="주소를 검색하세요"
                                className="flex-1 p-2 border rounded bg-gray-50"
                            />
                            <button
                                onClick={handleAddressSearch}
                                className="px-3 py-2 bg-gray-800 text-white text-sm rounded hover:bg-gray-900"
                            >
                                검색
                            </button>
                        </div>
                    </div>

                    {/* 전화번호 - 필수 */}
                    <div>
                        <label className="text-sm font-medium">전화번호 *</label>
                        <input
                            type="text"
                            value={phone}
                            onChange={(e) => setPhone(e.target.value)}
                            placeholder="02-0000-0000"
                            className="w-full mt-1 p-2 border rounded"
                        />
                    </div>

                    {/* 설명 - 선택 */}
                    <div>
                        <label className="text-sm font-medium">설명</label>
                        <input
                            type="text"
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                            placeholder="조용한 카페"
                            className="w-full mt-1 p-2 border rounded"
                        />
                    </div>

                    {/* 카페 종류 - 필수 */}
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

                    {/* 카페 종류가 FRANCHISE(프랜차이즈) 일 때만 보여줌 */}
                    {type === 'FRANCHISE' && (
                        <div>
                            <label className="text-sm font-medium">프랜차이즈 *</label>
                            <select
                                value={franchise}
                                //선택값이 바뀌면 franchise 상태 업데이트
                                onChange={(e) => setFranchise(e.target.value as FranchiseType)}
                                className="w-full mt-1 p-2 border rounded"
                            >
                                <option value="NONE">해당 없음</option>
                                <option value="STARBUCKS">스타벅스</option>
                                <option value="MEGA_COFFEE">메가커피</option>
                            </select>
                        </div>
                    )}

                    {/* 층수 - 필수 */}
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

                    {/* 혼잡도 - 필수 */}
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

                    {/* 화장실 여부 - 필수 */}
                    <div className="flex items-center justify-between">
                        <label className="text-sm font-medium">화장실 유무 *</label>
                        <input
                            type="checkbox"
                            checked={hasToilet}
                            onChange={(e) => setHasToilet(e.target.checked)}
                            // e.target.checked = 체크박스 체크 여부 (true/false)
                            className="w-4 h-4"
                        />
                    </div>

                    {/* 콘센트 여부 - 필수 */}
                    <div className="flex items-center justify-between">
                        <label className="text-sm font-medium">콘센트 유무 *</label>
                        <input
                            type="checkbox"
                            checked={hasOutlet}
                            onChange={(e) => setHasOutlet(e.target.checked)}
                            className="w-4 h-4"
                        />
                    </div>

                    {/* 와이파이 여부 - 필수 */}
                    <div className="flex items-center justify-between">
                        <label className="text-sm font-medium">와이파이 유무 *</label>
                        <input
                            type="checkbox"
                            checked={hasWifi}
                            onChange={(e) => setHasWifi(e.target.checked)}
                            className="w-4 h-4"
                        />
                    </div>

                    {/* 독립 공간 여부 - 필수 */}
                    <div className="flex items-center justify-between">
                        <label className="text-sm font-medium">공부 공간 유무 *</label>
                        <input
                            type="checkbox"
                            checked={hasSeparateSpace}
                            onChange={(e) => setHasSeparateSpace(e.target.checked)}
                            className="w-4 h-4"
                        />
                    </div>

                    {/* 이미지 주소 - 선택 */}
                    <div>
                        <label className="text-sm font-medium">이미지 주소</label>
                        <input
                            type="text"
                            value={imageUrl}
                            onChange={(e) => setImageUrl(e.target.value)}
                            className="w-full mt-1 p-2 border rounded"
                        />
                        {/* imageUrl 이 있을 때만 미리보기 이미지 보여줌 */}
                        {imageUrl && (
                            <img
                                src={imageUrl}
                                alt="이미지 미리보기"
                                className="mt-2 w-full rounded border"
                                onError={(e) => {
                                    e.currentTarget.style.display = 'none';
                                }}
                            />
                        )}
                    </div>


                </div>

                {/* 등록하기 버튼 */}
                <button
                    onClick={handleSubmit}
                    className="w-full mt-6 p-3 bg-black text-white rounded hover:bg-gray-800"
                >
                    등록하기
                </button>

            </div>
        </div>
    );
}