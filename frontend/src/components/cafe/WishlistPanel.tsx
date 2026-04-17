"use client";

import { useState, useEffect } from "react";
import { Heart, X } from "lucide-react";
import { WishlistResponse } from "@/types/cafe";
import { fetchWishlist } from "@/lib/api/cafe";

interface WishlistPanelProps {
    onClose: () => void;
    onCafeSelect: (cafeId: number) => void;
}

export default function WishlistPanel({ onClose, onCafeSelect }: WishlistPanelProps) {
    const [wishlists, setWishlists] = useState<WishlistResponse[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadWishlist = async () => {
            try {
                const data = await fetchWishlist();
                setWishlists(data ?? []);
            } catch (e) {
                console.error(e);
                setWishlists([]);
            } finally {
                setLoading(false);
            }
        };
        loadWishlist();
    }, []);

    return (
        <div className="fixed top-[20%] left-4 bottom-4 z-50 w-[25%] bg-white/80 backdrop-blur-sm rounded-3xl shadow-xl overflow-hidden flex flex-col border border-gray-100">

            {/* 헤더 */}
            <div className="px-5 py-4 border-b flex items-center justify-between bg-white/80 flex-shrink-0">
                <div className="flex items-center gap-2">
                    <Heart className="text-red-500" size={18} fill="#ef4444" />
                    <h2 className="font-semibold text-gray-900">찜 목록</h2>
                    <span className="text-sm text-gray-400">({wishlists.length})</span>
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
                {loading ? (
                    <div className="flex items-center justify-center h-full">
                        <p className="text-sm text-gray-400">로딩 중...</p>
                    </div>
                ) : wishlists.length === 0 ? (
                    <div className="flex flex-col items-center justify-center h-full gap-3 text-center">
                        <Heart size={40} className="text-gray-200" />
                        <p className="text-sm text-gray-400">찜한 카페가 없습니다</p>
                    </div>
                ) : (
                    wishlists.map((wishlist) => (
                        <button
                            key={wishlist.wishlistId}
                            onClick={() => {
                                onCafeSelect(wishlist.cafeId);
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
                                <p className="font-semibold text-gray-900 text-sm truncate">{wishlist.cafeName}</p>
                                <p className="text-xs text-gray-500 truncate mt-0.5">
                                    {new Date(wishlist.createAt).toLocaleDateString()}
                                </p>
                            </div>
                        </button>
                    ))
                )}
            </div>
        </div>
    );
}