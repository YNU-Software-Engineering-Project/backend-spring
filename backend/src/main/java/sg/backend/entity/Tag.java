package sg.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Tag {

    public enum Category{
        캐릭터_굿즈, 홈_리빙, 사진, 게임, 키즈, 도서_전자책, 여행, 만화_웹툰,
        스포츠_아웃도어, 테크_가전, 자동차, 패션, 아트, 소셜, 영화_음악, 반려동물, 디자인
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tag_id;

    private Category large_category;
    private Category small_category;

    private String tag_name;

    @OneToMany(mappedBy = "tag")
    private List<FundingTag> fundingTag;

}
