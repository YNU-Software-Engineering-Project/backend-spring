package sg.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "funding")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Funding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long funding_id;

    public Funding(Long fundingId) {
        this.funding_id = fundingId;
    }

    //게시물 정보
    @Enumerated(EnumType.STRING)
    private State current;

    @Enumerated(EnumType.STRING)
    private Category category;

    //대표자이름, 이메일, 세금이메일, 신분증 있어야지 funding 생성
    private String organizerName;

    private String organizerEmail;

    private String taxEmail;

    private String organizerIdCard; //파일 경로 작성

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer targetAmount;


    //게시물 내용
    private String title;

    private String mainImage; //파일 경로 작성

    private String projectSummary;

    @Lob
    private String story;  // 마크다운 형식의 스토리

    //정책
    private String rewardInfo;

    private String refundPolicy;


    //상황판
    private Integer totalLikes;

    private Integer todayLikes;

    private Integer totalVisitors;

    private Integer todayVisitors;

    private Integer todayAmount;

    private Integer currentAmount;


    private Integer rewardAmount;

    private boolean isTargetAmountAchieved;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY) //not blank
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "funding", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Tag> tagList;

    @OneToMany(mappedBy = "funding", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Document> documentList;

    @OneToMany(mappedBy =  "funding", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<IntroImage> introImgList;

    @OneToMany(mappedBy = "funding", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Reward> rewardList;

    @OneToMany(mappedBy = "funding", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Funder> funderList;

    @OneToMany(mappedBy = "funding", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Question> questionList;

    public Funding(String organizerName, String organizerEmail, String taxEmail, User user){
        this.organizerName = organizerName;
        this.organizerEmail = organizerEmail;
        this.taxEmail = taxEmail;
        this.current = State.DRAFT;
        this.user = user;
    }
}


