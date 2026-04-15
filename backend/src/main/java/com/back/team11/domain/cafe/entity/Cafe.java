package com.back.team11.domain.cafe.entity;

import com.back.team11.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder  // cafe-CRUD 추가: 필드명 기반 명시적 생성, 선택 필드 생략 가능
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class) // createdAt, updatedAt 자동 관리
@Table(name = "cafe")
public class Cafe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 11, scale = 7)
    private BigDecimal longitude;

    @Column(length = 20)
    private String phone;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CafeType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Franchise franchise;

    @Column(nullable = false)
    private Boolean hasToilet;

    @Column(nullable = false)
    private Boolean hasOutlet;

    @Column(nullable = false)
    private Boolean hasWifi;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FloorCount floorCount;

    @Column(nullable = false)
    private Boolean hasSeparateSpace;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CongestionLevel congestionLevel;

    @Column(length = 500)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CafeStatus status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;


    // ─────────────────────────────────────────────
    // 정적 팩토리 메서드 - new 대신 static 사용
    // member 없이 생성 - 관리자가 직접 등록하기에 필요 X
    // ─────────────────────────────────────────────
    public static Cafe createByAdmin(
            String name,
            String address,
            BigDecimal latitude,
            BigDecimal longitude,
            String phone,
            String description,
            CafeType type,
            Franchise franchise,
            Boolean hasToilet,
            Boolean hasOutlet,
            Boolean hasWifi,
            FloorCount floorCount,
            Boolean hasSeparateSpace,
            CongestionLevel congestionLevel,
            String imageUrl
    ) {
        return Cafe.builder()
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .phone(phone)
                .description(description)
                .type(type)
                .franchise(franchise)
                .hasToilet(hasToilet)
                .hasOutlet(hasOutlet)
                .hasWifi(hasWifi)
                .floorCount(floorCount)
                .hasSeparateSpace(hasSeparateSpace)
                .congestionLevel(congestionLevel)
                .imageUrl(imageUrl)
                .status(CafeStatus.APPROVED) // status는 APPROVED 고정: 관리자 직접 등록 → 즉시 승인
                .build();
    } // @Builder 사용으로 생성자 대신 정적 팩토리 메서드로 명시적 생성, 필드명 기반, 선택 필드 생략 가능

    // 승인 처리
    public void approve() {
        this.status = CafeStatus.APPROVED;
    }

    // 거절 처리
    public void reject() {
        this.status = CafeStatus.REJECTED;
    }

}
