package sg.backend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFundingLike is a Querydsl query type for FundingLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFundingLike extends EntityPathBase<FundingLike> {

    private static final long serialVersionUID = -971374595L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFundingLike fundingLike = new QFundingLike("fundingLike");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final QFunding funding;

    public final NumberPath<Long> fundingLikeId = createNumber("fundingLikeId", Long.class);

    public final QUser user;

    public QFundingLike(String variable) {
        this(FundingLike.class, forVariable(variable), INITS);
    }

    public QFundingLike(Path<? extends FundingLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFundingLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFundingLike(PathMetadata metadata, PathInits inits) {
        this(FundingLike.class, metadata, inits);
    }

    public QFundingLike(Class<? extends FundingLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.funding = inits.isInitialized("funding") ? new QFunding(forProperty("funding"), inits.get("funding")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

