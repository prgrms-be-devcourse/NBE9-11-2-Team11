"use client";

import { Heart, X } from "lucide-react";
import { CafeResponse } from "@/types/cafe";

interface WishlistPanelProps {
    onClose: () => void;
    onCafeSelect: (cafe: CafeResponse) => void;
}

// 더미 찜 목록
const dummyWishlist: CafeResponse[] = [
    {
        cafeId: 1,
        name: "스타벅스 강남점",
        address: "서울시 강남구 강남대로 390",
        latitude: 37.4979,
        longitude: 127.0276,
        phone: "02-1234-5678",
        description: "조용하고 넓은 카페",
        type: "FRANCHISE",
        franchise: "STARBUCKS",
        hasToilet: true,
        hasOutlet: true,
        hasWifi: true,
        floorCount: "TWO",
        hasSeparateSpace: false,
        congestionLevel: "LOW",
        imageUrl: null,
        wishlistCount: 1240,
        reviewCount: 342,
    },
    {
        cafeId: 3,
        name: "카페 온도",
        address: "서울시 강남구 논현동 456",
        latitude: 37.5100,
        longitude: 127.0400,
        phone: "02-3456-7890",
        description: "분위기 좋은 개인 카페",
        type: "INDIVIDUAL",
        franchise: "NONE",
        hasToilet: true,
        hasOutlet: true,
        hasWifi: true,
        floorCount: "ONE",
        hasSeparateSpace: true,
        congestionLevel: "LOW",
        imageUrl: null,
        wishlistCount: 452,
        reviewCount: 98,
    },
];

export default function WishlistPanel({ onClose, onCafeSelect }: WishlistPanelProps) {
    return (
        <div className="fixed top-[20%] left-4 bottom-4 z-50 w-[25%] bg-white/80 backdrop-blur-sm rounded-3xl shadow-xl overflow-hidden flex flex-col border border-gray-100">

            {/* 헤더 */}
            <div className="px-5 py-4 border-b flex items-center justify-between bg-white/80 flex-shrink-0">
                <div className="flex items-center gap-2">
                    <Heart className="text-red-500" size={18} fill="#ef4444" />
                    <h2 className="font-semibold text-gray-900">찜 목록</h2>
                    <span className="text-sm text-gray-400">({dummyWishlist.length})</span>
                </div>
                <button
                    onClick={onClose}
                    className="w-8 h-8 flex items-center justify-center text-gray-400 hover:text-gray-600 hover:bg-gray-100 rounded-xl transition-colors"
                >
                    <X size={18} />
                </button>
            </div>

            {/* 목록 */}
            <div className="flex-1 overflow-y-auto scrollbar-hide p-4 space-y-3">
                {dummyWishlist.length === 0 ? (
                    <div className="flex flex-col items-center justify-center h-full gap-3 text-center">
                        <Heart size={40} className="text-gray-200" />
                        <p className="text-sm text-gray-400">찜한 카페가 없습니다</p>
                    </div>
                ) : (
                    dummyWishlist.map((cafe) => (
                        <button
                            key={cafe.cafeId}
                            onClick={() => {
                                onCafeSelect(cafe);
                                onClose();
                            }}
                            className="w-full flex items-center gap-3 bg-white rounded-2xl p-3 shadow-sm hover:shadow-md transition-shadow text-left"
                        >
                            {/* 이미지 */}
                            <div className="w-14 h-14 rounded-xl bg-gradient-to-br from-amber-50 to-orange-50 flex items-center justify-center flex-shrink-0">
                                <span className="text-2xl">☕</span>
                            </div>

                            {/* 정보 */}
                            <div className="flex-1 min-w-0">
                                <p className="font-semibold text-gray-900 text-sm truncate">{cafe.name}</p>
                                <p className="text-xs text-gray-500 truncate mt-0.5">{cafe.address}</p>
                                <div className="flex items-center gap-1 mt-1">
                                    <Heart size={10} className="text-red-400" fill="#f87171" />
                                    <span className="text-xs text-gray-400">{cafe.wishlistCount.toLocaleString()}</span>
                                </div>
                            </div>
                        </button>
                    ))
                )}
            </div>
        </div>
    );
}