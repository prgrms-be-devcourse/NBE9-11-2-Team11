"use client";

import { Heart } from "lucide-react";
import { CafeResponse } from "@/types/cafe";

interface PopularCafeListProps {
    onCafeSelect: (cafe: CafeResponse) => void;
}

const dummyCafes: CafeResponse[] = [
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
        cafeId: 2,
        name: "메가커피 역삼점",
        address: "서울시 강남구 역삼동 123",
        latitude: 37.4965,
        longitude: 127.0283,
        phone: "02-2345-6789",
        description: "가성비 좋은 카페",
        type: "FRANCHISE",
        franchise: "MEGA_COFFEE",
        hasToilet: true,
        hasOutlet: true,
        hasWifi: true,
        floorCount: "ONE",
        hasSeparateSpace: false,
        congestionLevel: "MEDIUM",
        imageUrl: null,
        wishlistCount: 875,
        reviewCount: 189,
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

const sortedCafes = [...dummyCafes].sort((a, b) => b.wishlistCount - a.wishlistCount).slice(0, 3);

export default function PopularCafeList({ onCafeSelect }: PopularCafeListProps) {
    return (
        <div className="fixed top-30 left-4 z-40 w-64 bg-white/50 backdrop-blur-sm rounded-3xl shadow-xl overflow-hidden border border-gray-100">

            {/* 헤더 */}
            <div className="px-5 py-3 border-b">
                <h2 className="font-semibold text-gray-900 text-sm">인기 카페</h2>
                <p className="text-xs text-gray-400 mt-0.5">현재 지도 기준 찜 많은 순</p>
            </div>

            {/* 목록 */}
            <div className="p-3 space-y-2">
                {sortedCafes.map((cafe, index) => (
                    <button
                        key={cafe.cafeId}
                        onClick={() => onCafeSelect(cafe)}
                        className="w-full flex items-center gap-3 bg-white rounded-2xl p-3 shadow-sm hover:shadow-md transition-shadow text-left"
                    >
                        {/* 순위 */}
                        <div className={`w-6 h-6 rounded-lg flex items-center justify-center text-xs font-bold flex-shrink-0
                            ${index === 0 ? "bg-amber-100 text-amber-600" :
                                index === 1 ? "bg-gray-100 text-gray-500" :
                                    "bg-orange-100 text-orange-500"}`}
                        >
                            {index + 1}
                        </div>

                        {/* 정보 */}
                        <div className="flex-1 min-w-0">
                            <p className="font-semibold text-gray-900 text-xs truncate">{cafe.name}</p>
                            <p className="text-xs text-gray-400 truncate mt-0.5">{cafe.address.split(" ").slice(0, 3).join(" ")}</p>
                        </div>

                        {/* 찜 수 */}
                        <div className="flex items-center gap-1 flex-shrink-0">
                            <Heart size={10} className="text-red-400" fill="#f87171" />
                            <span className="text-xs text-gray-400">{cafe.wishlistCount.toLocaleString()}</span>
                        </div>
                    </button>
                ))}
            </div>
        </div>
    );
}