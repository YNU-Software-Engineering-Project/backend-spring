package sg.backend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSelectedReward is a Querydsl query type for SelectedReward
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSelectedReward extends EntityPathBase<SelectedReward> {

    private static final long serialVersionUID = 526626113L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSelectedReward selectedReward = new QSelectedReward("selectedReward");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final QFunder funder;

    public final QReward reward;

    public final NumberPath<Long> selRewardId = createNumber("selRewardId", Long.class);

    public QSelectedReward(String variable) {
        this(SelectedReward.class, forVariable(variable), INITS);
    }

    public QSelectedReward(Path<? extends SelectedReward> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSelectedReward(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSelectedReward(PathMetadata metadata, PathInits inits) {
        this(SelectedReward.class, metadata, inits);
    }

    public QSelectedReward(Class<? extends SelectedReward> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.funder = inits.isInitialized("funder") ? new QFunder(forProperty("funder"), inits.get("funder")) : null;
        this.reward = inits.isInitialized("reward") ? new QReward(forProperty("reward"), inits.get("reward")) : null;
    }

}

