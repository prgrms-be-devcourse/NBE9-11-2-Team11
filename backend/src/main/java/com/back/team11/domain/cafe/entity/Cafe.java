package com.back.team11.domain.cafe.entity;

import com.back.team11.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "cafe")
public class Cafe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
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

    // 승인 처리
    public void approve() {
        this.status = CafeStatus.APPROVED;
    }

    // 거절 처리
    public void reject() {
        this.status = CafeStatus.REJECTED;
    }

}
