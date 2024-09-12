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
    private Long introImgId;

    private String introImgPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_id")
    private Funding funding;
}
