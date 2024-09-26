package sg.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "intro_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class IntroImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long img_id;

    private String uuid;   //uuid로 지정된 이름

    private String fpath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_id")
    private Funding funding;

    public IntroImage(Funding funding,String image_uuid, String introImgPath){
        this.funding = funding;
        this.uuid = image_uuid;
        this.fpath = introImgPath;
    }
}
