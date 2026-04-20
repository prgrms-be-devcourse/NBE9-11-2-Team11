// 관리자 타입 정의 추가

// 카페 승인 상태 (승인대기/승인완료/거절)
export type CafeStatus = 'PENDING' | 'APPROVED' | 'REJECTED';

// 카페 종류 (프랜차이즈/개인카페)
export type CafeType = 'FRANCHISE' | 'INDIVIDUAL';

// 프랜차이즈 종류 (스타벅스/메가커피/해당없음)
// cafe.ts 에서 FranchiseType 으로 쓰고 있어서 맞춤
export type FranchiseType = 'STARBUCKS' | 'MEGA_COFFEE' | 'NONE';

// 혼잡도 ( 여유/보통/혼잡)
export type CongestionLevel = 'LOW' | 'MEDIUM' | 'HIGH';

// 층수 (1층/2층/3층이상)
export type FloorCount = 'ONE' | 'TWO' | 'THREE_OR_MORE';


// 백엔드에서 프론트로 오는 데이터 형태
export interface AdminCafe {
    cafeId: number;               // 카페 고유 번호
    name: string;                 // 카페 이름
    address: string;              // 주소
    latitude: number;             // 위도 (지도 좌표)
    longitude: number;            // 경도 (지도 좌표)
    phone: string | null;         // 전화번호 (없을 수도 있음)
    description: string | null;   // 설명 (없을 수도 있음)
    type: CafeType;               // 카페 종류
    franchise: FranchiseType;     // 프랜차이즈 종류
    hasToilet: boolean;           // 화장실 있음/없음
    hasOutlet: boolean;           // 콘센트 있음/없음
    hasWifi: boolean;             // 와이파이 있음/없음
    floorCount: FloorCount;       // 층수
    hasSeparateSpace: boolean;    // 독립 공간 있음/없음
    congestionLevel: CongestionLevel; // 혼잡도
    imageUrl: string | null;      // 이미지 주소 (없을 수도 있음)
    status: CafeStatus;           // 승인 상태 (관리자만 볼 수 있음)
    createdAt: string;            // 등록일
}



// 카페 등록할 때 백엔드로 보내는 데이터 형태
export interface CafeCreateRequest {
    name: string;
    address: string;
    latitude: number;
    longitude: number;
    type: CafeType;
    franchise: FranchiseType;
    hasToilet: boolean;
    hasOutlet: boolean;
    hasWifi: boolean;
    floorCount: FloorCount;
    hasSeparateSpace: boolean;
    congestionLevel: CongestionLevel;
    phone: string;
    description?: string;             // 선택
    imageUrl?: string;                // 선택
}


// 카페 수정할 때 백엔드로 보내는 데이터 형태
// 수정은 바뀐 것만 보내도 돼서 전부 ? (선택)
export interface CafeUpdateRequest {
    name?: string;
    address?: string;
    latitude?: number;
    longitude?: number;
    phone?: string;
    description?: string;
    type?: CafeType;
    franchise?: FranchiseType;
    hasToilet?: boolean;
    hasOutlet?: boolean;
    hasWifi?: boolean;
    floorCount?: FloorCount;
    hasSeparateSpace?: boolean;
    congestionLevel?: CongestionLevel;
    imageUrl?: string;
}


// 목록 조회할 때 백엔드가 페이지 형태로 데이터를 줌
export interface PageResponse<T> {
    content: T[];          // 실제 데이터 목록
    totalElements: number; // 전체 데이터 개수
    totalPages: number;    // 전체 페이지 수
    currentPage: number;   // 현재 페이지 번호
}