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