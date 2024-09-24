package sg.backend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEmailToken is a Querydsl query type for EmailToken
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailToken extends EntityPathBase<EmailToken> {

    private static final long serialVersionUID = 347267796L;

    public static final QEmailToken emailToken = new QEmailToken("emailToken");

    public final StringPath email = createString("email");

    public final StringPath emailTokenId = createString("emailTokenId");

    public final DateTimePath<java.time.LocalDateTime> expirationDate = createDateTime("expirationDate", java.time.LocalDateTime.class);

    public final BooleanPath expired = createBoolean("expired");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QEmailToken(String variable) {
        super(EmailToken.class, forVariable(variable));
    }

    public QEmailToken(Path<? extends EmailToken> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmailToken(PathMetadata metadata) {
        super(EmailToken.class, metadata);
    }

}

