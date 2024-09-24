package sg.backend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QIntroImage is a Querydsl query type for IntroImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QIntroImage extends EntityPathBase<IntroImage> {

    private static final long serialVersionUID = 275642726L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QIntroImage introImage = new QIntroImage("introImage");

    public final QFunding funding;

    public final NumberPath<Long> introImgId = createNumber("introImgId", Long.class);

    public final StringPath introImgPath = createString("introImgPath");

    public QIntroImage(String variable) {
        this(IntroImage.class, forVariable(variable), INITS);
    }

    public QIntroImage(Path<? extends IntroImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QIntroImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QIntroImage(PathMetadata metadata, PathInits inits) {
        this(IntroImage.class, metadata, inits);
    }

    public QIntroImage(Class<? extends IntroImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.funding = inits.isInitialized("funding") ? new QFunding(forProperty("funding"), inits.get("funding")) : null;
    }

}

