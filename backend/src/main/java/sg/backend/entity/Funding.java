package sg.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "funding")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Funding {

    public enum Category{
        캐릭터_굿즈, 홈_리빙, 사진, 게임, 키즈, 도서_전자책, 여행, 만화_웹툰,
        스포츠_아웃도어, 테크_가전, 자동차, 패션, 아트, 소셜, 영화_음악, 반려동물, 디자인
    }

    public enum State {작성중, 심사중, 심사완료, 진행중}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long funding_id;

    //게시물 정보
    private State current;
    private Category category;

    @NotBlank //대표자이름, 이메일, 세금이메일, 신분증 있어야지 funding 생성
    private String organizer_name;
    @NotBlank
    private String organizer_email;
    @NotBlank
    private String tax_email;
    @NotBlank
    private String organizer_id_card; //파일 경로 작성

    private LocalDateTime start_date;

    private LocalDateTime end_date;

    private Integer target_amount;


    //게시물 내용
    private String title;

    private String main_image; //파일 경로 작성

    private String project_summary;


    //정책
    private String product_info;

    private String refund_policy;


    //상황판
    private Integer total_likes;

    private Integer total_visitors;

    private Integer today_visitors;

    private Integer today_amount;

    private Integer current_amount;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "funding")
    private List<FundingTag> tagList;
    @OneToMany(mappedBy = "funding")
    private List<Document> documentList;
    @OneToMany(mappedBy =  "funding")
    private List<IntroImage> introimgList;
    @OneToMany(mappedBy = "funding")
    private List<Reward> rewardList;
    @OneToMany(mappedBy = "funding")
    private List<Funder> funderList;

}