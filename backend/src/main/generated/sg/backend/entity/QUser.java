package sg.backend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 645352482L;

    public static final QUser user = new QUser("user");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath detailAddress = createString("detailAddress");

    public final StringPath email = createString("email");

    public final ListPath<Funder, QFunder> funderList = this.<Funder, QFunder>createList("funderList", Funder.class, QFunder.class, PathInits.DIRECT2);

    public final ListPath<Funding, QFunding> fundingList = this.<Funding, QFunding>createList("fundingList", Funding.class, QFunding.class, PathInits.DIRECT2);

    public final StringPath landLotAddress = createString("landLotAddress");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath postalCode = createString("postalCode");

    public final StringPath profileImage = createString("profileImage");

    public final StringPath roadAddress = createString("roadAddress");

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final StringPath schoolEmail = createString("schoolEmail");

    public final BooleanPath schoolEmailVerified = createBoolean("schoolEmailVerified");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

