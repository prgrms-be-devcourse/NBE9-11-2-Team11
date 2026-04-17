// types/cafe.ts
export type CafeType = "FRANCHISE" | "INDIVIDUAL";
export type FranchiseType = "STARBUCKS" | "MEGA_COFFEE" | "NONE";
export type FloorCount = "ONE" | "TWO" | "THREE_OR_MORE";
export type CongestionLevel = "LOW" | "MEDIUM" | "HIGH";

export interface CafeResponse {
    cafeId: number;
    name: string;
    address: string;
    latitude: number;
    longitude: number;
    phone: string | null;
    description: string | null;
    type: CafeType;
    franchise: FranchiseType;
    hasToilet: boolean;
    hasOutlet: boolean;
    hasWifi: boolean;
    floorCount: FloorCount;
    hasSeparateSpace: boolean;
    congestionLevel: CongestionLevel;
    imageUrl: string | null;

    // 유지할 필드
    wishlistCount: number;     // 찜 개수
    reviewCount: number;       // 리뷰 개수

}

// 목록/마커용
export interface CafeListResponse {
    cafeId: number;
    name: string;
    address: string;
    latitude: number;
    longitude: number;
    phone: string | null;
    description: string | null;
    type: CafeType;
    franchise: FranchiseType;
    hasToilet: boolean;
    hasOutlet: boolean;
    hasWifi: boolean;
    floorCount: FloorCount;
    hasSeparateSpace: boolean;
    congestionLevel: CongestionLevel;
    imageUrl: string | null;
    wishlistCount: number;
    createdAt: string;
}

// 상세 조회용
export interface CafeDetailResponse {
    cafeId: number;
    name: string;
    address: string;
    latitude: number;
    longitude: number;
    phone: string | null;
    description: string | null;
    type: CafeType;
    franchise: FranchiseType;
    hasToilet: boolean;
    hasOutlet: boolean;
    hasWifi: boolean;
    floorCount: FloorCount;
    hasSeparateSpace: boolean;
    congestionLevel: CongestionLevel;
    imageUrl: string | null;
    wishlistCount: number;
    isWishlisted: boolean;
    createdAt: string;
}

// 리뷰용
export interface ReviewResponse {
    id: number;
    cafeId: number;
    nickname: string;
    content: string;
    createdAt: string;
}

//찜 목록
export interface WishlistResponse {
    wishlistId: number;
    cafeId: number;
    cafeName: string;
    createAt: string;
}