package sg.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.time.LocalDate;
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

    //게시물 정보
    @Enumerated(EnumType.STRING)
    private State current;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private SubCategory subCategory;

    @NotBlank //대표자이름, 이메일, 세금이메일, 신분증 있어야지 funding 생성
    private String organizerName;

    @NotBlank
    private String organizerEmail;

    @NotBlank
    private String taxEmail;

    @NotBlank
    private String organizerIdCard; //파일 경로 작성

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer targetAmount;


    //게시물 내용
    private String title;

    private String mainImage; //파일 경로 작성

    private String projectSummary;


    //정책
    private String productInfo;

    private String refundPolicy;


    //상황판
    private Integer totalLikes;

    private Integer totalVisitors;

    private Integer todayVisitors;

    private Integer todayAmount;

    private Integer currentAmount;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "funding", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Document> documentList;

    @OneToMany(mappedBy =  "funding", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<IntroImage> introImgList;

    @OneToMany(mappedBy = "funding", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Reward> rewardList;

    @OneToMany(mappedBy = "funding", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Funder> funderList;

}