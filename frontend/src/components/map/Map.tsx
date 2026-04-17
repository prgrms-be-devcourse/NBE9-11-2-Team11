"use client";

import { useEffect, useRef, useCallback } from "react";
import { CafeResponse } from "@/types/cafe";

declare global {
    interface Window {
        kakao: any;
    }
}

interface KakaoMapProps {
    onCafeSelect: (cafe: CafeResponse) => void;
    center: { lat: number; lng: number } | null;
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

export default function KakaoMap({ onCafeSelect, center }: KakaoMapProps) {

    const mapRef = useRef<HTMLDivElement>(null);
    const mapInstance = useRef<any>(null);

    const addMarkers = useCallback((map: any, cafes: CafeResponse[]) => {
        const clusterer = new window.kakao.maps.MarkerClusterer({
            map,
            averageCenter: true,
            minLevel: 6,
            gridSize: 60,
        });

        const markers = cafes.map((cafe) => {
            const position = new window.kakao.maps.LatLng(cafe.latitude, cafe.longitude);
            const marker = new window.kakao.maps.Marker({ position, title: cafe.name });

            window.kakao.maps.event.addListener(marker, "click", () => {
                onCafeSelect(cafe);
            });

            return marker;
        });

        clusterer.addMarkers(markers);
    }, [onCafeSelect]);

    const initMap = useCallback(() => {
        if (!mapRef.current || !window.kakao?.maps || mapInstance.current) return;

        const options = {
            center: new window.kakao.maps.LatLng(37.5665, 126.9780),
            level: 3,
        };

        mapInstance.current = new window.kakao.maps.Map(mapRef.current, options);
        addMarkers(mapInstance.current, dummyCafes);
    }, [addMarkers]);

    // 검색으로 선택된 위치로 지도 이동
    useEffect(() => {
        if (!center || !mapInstance.current) return;

        const moveLatLng = new window.kakao.maps.LatLng(center.lat, center.lng);
        mapInstance.current.setCenter(moveLatLng);
        mapInstance.current.setLevel(3);
    }, [center]);

    useEffect(() => {
        if (window.kakao?.maps) {
            window.kakao.maps.load(initMap);
            return;
        }

        const script = document.createElement("script");
        script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${process.env.NEXT_PUBLIC_KAKAO_MAP_KEY}&autoload=false&libraries=clusterer`;
        script.async = true;
        script.onload = () => {
            window.kakao.maps.load(initMap);
        };
        document.head.appendChild(script);

        return () => {
            if (script.parentNode) {
                script.parentNode.removeChild(script);
            }
        };
    }, [initMap]);

    useEffect(() => {
        const handleRelayout = () => {
            if (mapInstance.current) {
                mapInstance.current.relayout();
                setTimeout(() => mapInstance.current?.relayout(), 150);
                setTimeout(() => mapInstance.current?.relayout(), 300);
            }
        };

        const observer = new ResizeObserver(handleRelayout);
        if (mapRef.current) {
            observer.observe(mapRef.current);
        }

        const initialTimer = setTimeout(handleRelayout, 600);

        return () => {
            observer.disconnect();
            clearTimeout(initialTimer);
        };
    }, []);

    return (
        <div
            ref={mapRef}
            style={{ width: "100%", height: "100vh" }}
            className="bg-gray-100"
        />
    );
}