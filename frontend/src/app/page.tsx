"use client";

import dynamic from "next/dynamic";
import { useState } from "react";
import { CafeResponse } from "@/types/cafe";
import CafeDetail from "@/components/cafe/CafeDetail";
import Header from "@/components/common/Header";
import SearchModal from "@/components/common/SearchModal";
import WishlistPanel from "@/components/cafe/WishlistPanel";
import FilterModal, { FilterState } from "@/components/common/FilterModal";
import ReportModal from "@/components/cafe/ReportModal";
import PopularCafeList from "@/components/cafe/PopularCafeList";
import { Search, Heart, SlidersHorizontal, X } from "lucide-react";

const KakaoMap = dynamic(() => import("@/components/map/Map"), { ssr: false });

const initialFilters: FilterState = {
  franchise: [],
  hasWifi: null,
  hasOutlet: null,
  hasToilet: null,
  hasSeparateSpace: null,
  floorCount: [],
  congestionLevel: [],
};

export default function Home() {
  const [selectedCafe, setSelectedCafe] = useState<CafeResponse | null>(null);
  const [showSearch, setShowSearch] = useState(false);
  const [showWishlist, setShowWishlist] = useState(false);
  const [showFilter, setShowFilter] = useState(false);
  const [filters, setFilters] = useState<FilterState>(initialFilters);
  const [mapCenter, setMapCenter] = useState<{ lat: number; lng: number } | null>(null);
  const [showReport, setShowReport] = useState(false);

  const handleSearchSelect = (lat: number, lng: number) => {
    setMapCenter({ lat, lng });
  };

  const handleFilterApply = (newFilters: FilterState) => {
    setFilters(newFilters);
  };

  // 적용된 필터 태그 목록
  const activeFilterTags: { label: string; onRemove: () => void }[] = [
    ...filters.franchise.map((v) => ({
      label: v === "STARBUCKS" ? "스타벅스" : v === "MEGA_COFFEE" ? "메가커피" : "개인카페",
      onRemove: () => setFilters((prev) => ({ ...prev, franchise: prev.franchise.filter((f) => f !== v) })),
    })),
    ...(filters.hasWifi ? [{ label: "와이파이", onRemove: () => setFilters((prev) => ({ ...prev, hasWifi: null })) }] : []),
    ...(filters.hasOutlet ? [{ label: "콘센트", onRemove: () => setFilters((prev) => ({ ...prev, hasOutlet: null })) }] : []),
    ...(filters.hasToilet ? [{ label: "화장실", onRemove: () => setFilters((prev) => ({ ...prev, hasToilet: null })) }] : []),
    ...(filters.hasSeparateSpace ? [{ label: "분리된 공간", onRemove: () => setFilters((prev) => ({ ...prev, hasSeparateSpace: null })) }] : []),
    ...filters.floorCount.map((v) => ({
      label: v === "ONE" ? "1층" : v === "TWO" ? "2층" : "3층 이상",
      onRemove: () => setFilters((prev) => ({ ...prev, floorCount: prev.floorCount.filter((f) => f !== v) })),
    })),
    ...filters.congestionLevel.map((v) => ({
      label: v === "LOW" ? "한산" : v === "MEDIUM" ? "보통" : "혼잡",
      onRemove: () => setFilters((prev) => ({ ...prev, congestionLevel: prev.congestionLevel.filter((f) => f !== v) })),
    })),
  ];

  return (
    <main className="relative flex-1">
      <Header
        onSearchClick={() => setShowSearch(true)}
        onReportClick={() => setShowReport(true)}
      />
      <KakaoMap
        onCafeSelect={setSelectedCafe}
        center={mapCenter}
      />

      {/* 우측 아이콘 버튼 그룹 */}
      <div className="fixed top-24 right-6 z-40 flex flex-col gap-2 items-end">
        <button
          onClick={() => setShowSearch(true)}
          className="w-10 h-10 bg-white/80 backdrop-blur-sm border border-gray-100 rounded-2xl shadow-md flex items-center justify-center text-gray-600 hover:text-gray-900 hover:bg-white transition-colors"
        >
          <Search size={18} />
        </button>
        <button
          onClick={() => setShowWishlist(!showWishlist)}
          className={`w-10 h-10 bg-white/80 backdrop-blur-sm border border-gray-100 rounded-2xl shadow-md flex items-center justify-center transition-colors
            ${showWishlist ? "text-red-500" : "text-gray-600 hover:text-gray-900 hover:bg-white"}`}
        >
          <Heart size={18} fill={showWishlist ? "#ef4444" : "none"} />
        </button>
        <button
          onClick={() => setShowFilter(true)}
          className={`w-10 h-10 bg-white/80 backdrop-blur-sm border border-gray-100 rounded-2xl shadow-md flex items-center justify-center transition-colors
            ${activeFilterTags.length > 0 ? "text-gray-800" : "text-gray-600 hover:text-gray-900 hover:bg-white"}`}
        >
          <SlidersHorizontal size={18} />
        </button>

        {/* 적용된 필터 태그 */}
        {activeFilterTags.map((tag, index) => (
          <div
            key={index}
            className="flex items-center gap-1.5 bg-gray-800 text-white text-xs font-medium px-3 py-1.5 rounded-full shadow-md"
          >
            <span>{tag.label}</span>
            <button
              onClick={tag.onRemove}
              className="text-gray-300 hover:text-white transition-colors"
            >
              <X size={12} />
            </button>
          </div>
        ))}
      </div>

      {showWishlist && (
        <WishlistPanel
          onClose={() => setShowWishlist(false)}
          onCafeSelect={setSelectedCafe}
        />
      )}

      {!selectedCafe && !showWishlist && (
        <PopularCafeList onCafeSelect={setSelectedCafe} />
      )}

      {selectedCafe && (
        <CafeDetail
          cafe={selectedCafe}
          onClose={() => setSelectedCafe(null)}
        />
      )}

      {showSearch && (
        <SearchModal
          onClose={() => setShowSearch(false)}
          onSelect={handleSearchSelect}
        />
      )}

      {showFilter && (
        <FilterModal
          onClose={() => setShowFilter(false)}
          onApply={handleFilterApply}
          currentFilters={filters}
        />
      )}

      {showReport && (
        <ReportModal onClose={() => setShowReport(false)} />
      )}
    </main>
  );
}