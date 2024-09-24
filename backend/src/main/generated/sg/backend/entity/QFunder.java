package sg.backend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFunder is a Querydsl query type for Funder
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFunder extends EntityPathBase<Funder> {

    private static final long serialVersionUID = 1281112265L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFunder funder = new QFunder("funder");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> funderId = createNumber("funderId", Long.class);

    public final QFunding funding;

    public final ListPath<SelectedReward, QSelectedReward> selrewardList = this.<SelectedReward, QSelectedReward>createList("selrewardList", SelectedReward.class, QSelectedReward.class, PathInits.DIRECT2);

    public final QUser user;

    public QFunder(String variable) {
        this(Funder.class, forVariable(variable), INITS);
    }

    public QFunder(Path<? extends Funder> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFunder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFunder(PathMetadata metadata, PathInits inits) {
        this(Funder.class, metadata, inits);
    }

    public QFunder(Class<? extends Funder> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.funding = inits.isInitialized("funding") ? new QFunding(forProperty("funding"), inits.get("funding")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

