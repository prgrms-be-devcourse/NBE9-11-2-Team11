"use client";

import { useState, useEffect } from "react";
import { CafeDetailResponse, ReviewResponse } from "@/types/cafe";
import { X, MapPin, Phone, Heart, MessageCircle } from "lucide-react";
import { addWishlist, removeWishlist, fetchCafeReviews, createReview, deleteReview } from "@/lib/api/cafe";
import { useRouter } from "next/navigation";
import { useAuthStore } from "@/store/authStore";

interface CafeDetailProps {
    cafe: CafeDetailResponse;
    onClose: () => void;
}

export default function CafeDetail({ cafe, onClose }: CafeDetailProps) {
    const [isWishlisted, setIsWishlisted] = useState(cafe.isWishlisted);
    const [wishlistCount, setWishlistCount] = useState(cafe.wishlistCount);
    const [reviews, setReviews] = useState<ReviewResponse[]>([]);
    const [reviewLoading, setReviewLoading] = useState(true);
    const [reviewContent, setReviewContent] = useState("");
    const { isLoggedIn, member } = useAuthStore();
    const router = useRouter();

    // 리뷰 목록 불러오기
    useEffect(() => {
        const loadReviews = async () => {
            try {
                const data = await fetchCafeReviews(cafe.cafeId);
                setReviews(data);
            } catch (e) {
                console.error(e);
            } finally {
                setReviewLoading(false);
            }
        };
        loadReviews();
    }, [cafe.cafeId]);

    useEffect(() => {
        setIsWishlisted(cafe.isWishlisted);
        setWishlistCount(cafe.wishlistCount);
    }, [cafe.cafeId]);

    // 찜 버튼 클릭 (낙관적 업데이트)
    const handleWishlist = async () => {
        if (!isLoggedIn) {
            router.push("/login");
            return;
        }

        const prev = isWishlisted;
        setIsWishlisted(!prev);
        setWishlistCount(prev ? wishlistCount - 1 : wishlistCount + 1);

        try {
            if (prev) {
                await removeWishlist(cafe.cafeId);
            } else {
                await addWishlist(cafe.cafeId);
            }
        } catch (e) {
            setIsWishlisted(prev);
            setWishlistCount(cafe.wishlistCount);
        }
    };

    const handleReviewSubmit = async () => {
        if (!reviewContent.trim()) return;
        try {
            await createReview(cafe.cafeId, reviewContent);
            setReviewContent("");
            const data = await fetchCafeReviews(cafe.cafeId);
            setReviews(data);
        } catch (e) {
            console.error(e);
        }
    };

    const handleReviewDelete = async (reviewId: number) => {
        try {
            await deleteReview(cafe.cafeId, reviewId);
            const data = await fetchCafeReviews(cafe.cafeId);
            setReviews(data);
        } catch (e) {
            console.error(e);
        }
    };

    return (
        <div className="fixed top-[10%] left-4 bottom-4 z-50 w-[25%] bg-white/70 rounded-3xl shadow-xl overflow-hidden flex flex-col border border-gray-100">

            {/* 헤더 */}
            <div className="px-5 py-4 border-b flex items-center justify-between bg-white/50 flex-shrink-0">
                <div className="flex items-center gap-3">
                    <div>
                        <h2 className="font-semibold text-lg leading-tight text-gray-900">
                            {cafe?.name || "이름 없는 카페"}
                        </h2>
                        <p className="text-xs text-gray-600 flex items-center gap-1 mt-0.5">
                            <MapPin size={13} />
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

            {/* 스크롤 영역 */}
            <div className="flex-1 overflow-y-auto p-5 space-y-5 scrollbar-hide">

                {/* 이미지 */}
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

                {/* 설명 및 찜 */}
                <div className="flex justify-between items-start">
                    <div className="flex-1 pr-3">
                        <h3 className="text-sm font-semibold text-gray-700 mb-1">설명</h3>
                        <p className="text-gray-800 text-sm font-medium leading-relaxed">
                            {cafe?.description || "조용하고 편안한 작업하기 좋은 카페입니다."}
                        </p>
                    </div>
                    <button
                        onClick={handleWishlist}
                        className="flex flex-col items-center flex-shrink-0"
                    >
                        <Heart
                            className={isWishlisted ? "text-red-500" : "text-gray-300"}
                            size={24}
                            fill={isWishlisted ? "#ef4444" : "none"}
                        />
                        <span className="text-sm font-bold text-gray-800 mt-1">
                            {wishlistCount.toLocaleString()}
                        </span>
                        <span className="text-[10px] text-gray-500 -mt-0.5">찜</span>
                    </button>
                </div>

                {/* 편의시설 */}
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

                {/* 층수 및 혼잡도 */}
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

                {/* 전화번호 */}
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

                {/* 리뷰 */}
                <div className="border-t pt-4">
                    <div className="flex items-center gap-2 mb-3">
                        <MessageCircle size={16} className="text-gray-700" />
                        <h3 className="text-sm font-semibold text-gray-700">리뷰</h3>
                        <span className="text-sm font-bold text-gray-900">{reviews.length}</span>
                    </div>

                    {/* 리뷰 작성 */}
                    {isLoggedIn ? (
                        <div className="mb-3">
                            <textarea
                                value={reviewContent}
                                onChange={(e) => setReviewContent(e.target.value)}
                                placeholder="리뷰를 작성해주세요"
                                rows={3}
                                className="w-full px-4 py-3 rounded-2xl border border-gray-200 text-sm text-gray-900 placeholder-gray-400 outline-none focus:border-gray-400 transition-colors resize-none bg-white/80"
                            />
                            <button
                                onClick={handleReviewSubmit}
                                disabled={!reviewContent.trim()}
                                className="w-full mt-2 py-2.5 rounded-2xl bg-gray-800 text-white text-sm font-medium hover:bg-gray-900 transition-colors disabled:opacity-40 disabled:cursor-not-allowed"
                            >
                                리뷰 등록
                            </button>
                        </div>
                    ) : (
                        <div className="mb-3 bg-gray-50 rounded-2xl px-4 py-4 text-center">
                            <p className="text-sm text-gray-500 mb-2">로그인 후 리뷰를 작성할 수 있어요</p>
                            <button
                                onClick={() => router.push("/login")}
                                className="px-4 py-2 rounded-xl bg-gray-800 text-white text-sm font-medium hover:bg-gray-900 transition-colors"
                            >
                                로그인하기
                            </button>
                        </div>
                    )}

                    {reviewLoading ? (
                        <div className="text-sm text-gray-400 text-center py-3">로딩 중...</div>
                    ) : reviews.length === 0 ? (
                        <div className="bg-gray-50 rounded-2xl px-4 py-3 text-sm text-gray-500 text-center italic">
                            아직 리뷰가 없어요. 첫 번째 리뷰를 남겨보세요!
                        </div>
                    ) : (
                        <div className="space-y-3">
                            {reviews.map((review) => (
                                <div key={review.id} className="bg-gray-50 rounded-2xl px-4 py-3">
                                    <div className="flex items-center justify-between mb-1">
                                        <span className="text-sm font-semibold text-gray-800">{review.nickname}</span>
                                        <div className="flex items-center gap-2">
                                            <span className="text-xs text-gray-400">{review.createdAt.slice(0, 10)}</span>
                                            {/* 본인 리뷰 또는 관리자면 삭제 버튼 표시 */}
                                            {(review.memberId === member?.memberId || member?.role === "ADMIN") && (
                                                <button
                                                    onClick={() => handleReviewDelete(review.id)}
                                                    className="text-xs text-red-400 hover:text-red-600 transition-colors"
                                                >
                                                    삭제
                                                </button>
                                            )}
                                        </div>
                                    </div>
                                    <p className="text-sm text-gray-600">{review.content}</p>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}