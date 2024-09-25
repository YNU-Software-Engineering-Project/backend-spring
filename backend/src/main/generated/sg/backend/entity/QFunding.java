package sg.backend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFunding is a Querydsl query type for Funding
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFunding extends EntityPathBase<Funding> {

    private static final long serialVersionUID = 1059778374L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFunding funding = new QFunding("funding");

    public final EnumPath<Category> category = createEnum("category", Category.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final EnumPath<State> current = createEnum("current", State.class);

    public final NumberPath<Integer> currentAmount = createNumber("currentAmount", Integer.class);

    public final ListPath<Document, QDocument> documentList = this.<Document, QDocument>createList("documentList", Document.class, QDocument.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final ListPath<Funder, QFunder> funderList = this.<Funder, QFunder>createList("funderList", Funder.class, QFunder.class, PathInits.DIRECT2);

    public final NumberPath<Long> fundingId = createNumber("fundingId", Long.class);

    public final ListPath<IntroImage, QIntroImage> introImgList = this.<IntroImage, QIntroImage>createList("introImgList", IntroImage.class, QIntroImage.class, PathInits.DIRECT2);

    public final StringPath mainImage = createString("mainImage");

    public final StringPath organizerEmail = createString("organizerEmail");

    public final StringPath organizerIdCard = createString("organizerIdCard");

    public final StringPath organizerName = createString("organizerName");

    public final StringPath productInfo = createString("productInfo");

    public final StringPath projectSummary = createString("projectSummary");

    public final StringPath refundPolicy = createString("refundPolicy");

    public final NumberPath<Integer> rewardAmount = createNumber("rewardAmount", Integer.class);

    public final ListPath<Reward, QReward> rewardList = this.<Reward, QReward>createList("rewardList", Reward.class, QReward.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final ListPath<Tag, QTag> tagList = this.<Tag, QTag>createList("tagList", Tag.class, QTag.class, PathInits.DIRECT2);

    public final NumberPath<Integer> targetAmount = createNumber("targetAmount", Integer.class);

    public final StringPath taxEmail = createString("taxEmail");

    public final StringPath title = createString("title");

    public final NumberPath<Integer> todayAmount = createNumber("todayAmount", Integer.class);

    public final NumberPath<Integer> todayLikes = createNumber("todayLikes", Integer.class);

    public final NumberPath<Integer> todayVisitors = createNumber("todayVisitors", Integer.class);

    public final NumberPath<Integer> totalLikes = createNumber("totalLikes", Integer.class);

    public final NumberPath<Integer> totalVisitors = createNumber("totalVisitors", Integer.class);

    public final QUser user;

    public QFunding(String variable) {
        this(Funding.class, forVariable(variable), INITS);
    }

    public QFunding(Path<? extends Funding> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFunding(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFunding(PathMetadata metadata, PathInits inits) {
        this(Funding.class, metadata, inits);
    }

    public QFunding(Class<? extends Funding> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

