package com.back.team11.domain.cafe.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCafe is a Querydsl query type for Cafe
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCafe extends EntityPathBase<Cafe> {

    private static final long serialVersionUID = 236294218L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCafe cafe = new QCafe("cafe");

    public final StringPath address = createString("address");

    public final EnumPath<CongestionLevel> congestionLevel = createEnum("congestionLevel", CongestionLevel.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final EnumPath<FloorCount> floorCount = createEnum("floorCount", FloorCount.class);

    public final EnumPath<Franchise> franchise = createEnum("franchise", Franchise.class);

    public final BooleanPath hasOutlet = createBoolean("hasOutlet");

    public final BooleanPath hasSeparateSpace = createBoolean("hasSeparateSpace");

    public final BooleanPath hasToilet = createBoolean("hasToilet");

    public final BooleanPath hasWifi = createBoolean("hasWifi");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final NumberPath<java.math.BigDecimal> latitude = createNumber("latitude", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> longitude = createNumber("longitude", java.math.BigDecimal.class);

    public final com.back.team11.domain.member.entity.QMember member;

    public final StringPath name = createString("name");

    public final StringPath phone = createString("phone");

    public final EnumPath<CafeStatus> status = createEnum("status", CafeStatus.class);

    public final EnumPath<CafeType> type = createEnum("type", CafeType.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QCafe(String variable) {
        this(Cafe.class, forVariable(variable), INITS);
    }

    public QCafe(Path<? extends Cafe> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCafe(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCafe(PathMetadata metadata, PathInits inits) {
        this(Cafe.class, metadata, inits);
    }

    public QCafe(Class<? extends Cafe> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.back.team11.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

