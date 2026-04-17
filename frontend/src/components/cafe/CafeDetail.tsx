"use client";

import { CafeResponse } from "@/types/cafe";
import { X, MapPin, Phone, Heart, MessageCircle } from "lucide-react";

interface CafeDetailProps {
    cafe: CafeResponse;
    onClose: () => void;
}

export default function CafeDetail({ cafe, onClose }: CafeDetailProps) {
    return (
        <div className="fixed top-[10%] left-4 bottom-4 z-50 w-[25%] bg-white/70 rounded-3xl shadow-xl overflow-hidden flex flex-col border border-gray-100">

            {/* 1. 헤더 영역 */}
            <div className="px-5 py-4 border-b flex items-center justify-between bg-white/50 flex-shrink-0">
                <div className="flex items-center gap-3">
                    <div>
                        <h2 className="font-semibold text-lg leading-tight text-gray-900">
                            {cafe?.name || "이름 없는 카페"}
                        </h2>
                        <p className="text-xs text-gray-600 flex items-center gap-1 mt-0.5">
                            <MapPin size={13} />
                            {/* 주소가 없을 경우를 대비한 Null Safe 로직 */}
                            {cafe?.address ? cafe.address.split(" ").slice(0, 2).join(" ") : "주소 정보 없음"}
                        </p>
                    </div>
                </div>
                <button
                    onClick={onClose}
                    className="w-8 h-8 flex items-center justify-center text-gray-500 hover:text-gray-800 hover:bg-gray-100 rounded-xl transition-colors"
                >
                    <X size={20} />
                </button>
            </div>

            {/* 3. 스크롤 가능 상세 정보 영역 */}
            <div className="flex-1 overflow-y-auto p-5 space-y-5 scrollbar-hide">
                {/* 2. 이미지 영역 */}
                <div className="h-40 bg-gray-100 relative">
                    {cafe?.imageUrl ? (
                        <img
                            src={cafe.imageUrl}
                            alt={cafe.name}
                            className="w-full h-full object-cover"
                        />
                    ) : (
                        <div className="w-full h-full flex items-center justify-center bg-gradient-to-br from-amber-50 to-orange-50">
                            <span className="text-6xl opacity-30">☕</span>
                        </div>
                    )}
                </div>

                {/* 설명 및 찜 카운트 */}
                <div className="flex justify-between items-start ">
                    <div className="flex-1 pr-3 ">
                        <h3 className="text-sm font-semibold text-gray-700 mb-1">설명</h3>
                        <p className="text-gray-800 text-sm font-medium leading-relaxed">
                            {cafe?.description || "조용하고 편안한 작업하기 좋은 카페입니다."}
                        </p>
                    </div>
                    <div className="flex flex-col items-center flex-shrink-0">
                        <Heart className="text-red-500" size={24} fill="#ef4444" />
                        <span className="text-sm font-bold text-gray-800 mt-1">
                            {(cafe?.wishlistCount || 0).toLocaleString()}
                        </span>
                        <span className="text-[10px] text-gray-500 -mt-0.5">찜</span>
                    </div>
                </div>

                {/* 편의시설 섹션 */}
                <div>
                    <h3 className="text-sm font-semibold text-gray-700 mb-3">편의시설</h3>
                    <div className="grid grid-cols-2 gap-3">
                        {cafe?.hasWifi && (
                            <div className="flex items-center gap-2.5 bg-gray-100/60 rounded-2xl px-4 py-3 text-sm font-medium text-gray-800">
                                📶 <span>와이파이</span>
                            </div>
                        )}
                        {cafe?.hasOutlet && (
                            <div className="flex items-center gap-2.5 bg-gray-100/60 rounded-2xl px-4 py-3 text-sm font-medium text-gray-800">
                                🔌 <span>콘센트</span>
                            </div>
                        )}
                        {cafe?.hasToilet && (
                            <div className="flex items-center gap-2.5 bg-gray-100/60 rounded-2xl px-4 py-3 text-sm font-medium text-gray-800">
                                🚻 <span>화장실</span>
                            </div>
                        )}
                        {cafe?.hasSeparateSpace && (
                            <div className="flex items-center gap-2.5 bg-gray-100/60 rounded-2xl px-4 py-3 text-sm font-medium text-gray-800">
                                🪑 <span>분리된 공간</span>
                            </div>
                        )}
                    </div>
                </div>

                {/* 층수 및 혼잡도 (그리드 레이아웃) */}
                <div className="grid grid-cols-2 gap-4 text-sm bg-gray-50/60 rounded-2xl p-4 border border-gray-100">
                    <div>
                        <p className="text-gray-500 text-xs">층수</p>
                        <p className="font-bold text-gray-900 mt-0.5">
                            {cafe?.floorCount === "ONE" && "1층"}
                            {cafe?.floorCount === "TWO" && "2층"}
                            {cafe?.floorCount === "THREE_OR_MORE" && "3층 이상"}
                            {!cafe?.floorCount && "정보 없음"}
                        </p>
                    </div>
                    <div>
                        <p className="text-gray-500 text-xs">혼잡도</p>
                        <p className={`font-bold mt-0.5 ${cafe?.congestionLevel === "LOW" ? "text-green-600" :
                            cafe?.congestionLevel === "MEDIUM" ? "text-amber-600" : "text-red-600"
                            }`}>
                            {cafe?.congestionLevel === "LOW" && "한산"}
                            {cafe?.congestionLevel === "MEDIUM" && "보통"}
                            {cafe?.congestionLevel === "HIGH" && "혼잡"}
                            {!cafe?.congestionLevel && "정보 없음"}
                        </p>
                    </div>
                </div>

                {/* 전화번호 섹션 (수정 완료) */}
                {cafe?.phone && (
                    <a
                        href={`tel:${cafe.phone}`}
                        className="flex items-center gap-3 text-gray-800 hover:text-gray-900 transition-colors p-1"
                    >
                        <div className="w-10 h-10 rounded-2xl bg-gray-100 flex items-center justify-center">
                            <Phone size={20} />
                        </div>
                        <div>
                            <p className="text-xs text-gray-600">전화번호</p>
                            <p className="font-semibold">{cafe.phone}</p>
                        </div>
                    </a>
                )}

                {/* 리뷰 요약 섹션 */}
                <div className="border-t pt-4">
                    <div className="flex items-center gap-2 mb-3">
                        <MessageCircle size={16} className="text-gray-700" />
                        <h3 className="text-sm font-semibold text-gray-700">리뷰</h3>
                        <span className="text-sm font-bold text-gray-900">
                            {(cafe?.reviewCount || 0).toLocaleString()}
                        </span>
                    </div>
                    <div className="bg-gray-50 rounded-2xl px-4 py-3 text-sm text-gray-500 text-center italic">
                        아직 리뷰가 없어요. 첫 번째 리뷰를 남겨보세요!
                    </div>
                </div>

            </div>
        </div>
    );
}