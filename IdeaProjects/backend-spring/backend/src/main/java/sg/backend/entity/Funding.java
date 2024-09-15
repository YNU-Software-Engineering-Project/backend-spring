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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fundingId;

    //게시물 정보
    private State current;

    private Category category;

    @NotBlank //대표자이름, 이메일, 세금이메일, 신분증 있어야지 funding 생성
    private String organizerName;

    @NotBlank
    private String organizerEmail;

    @NotBlank
    private String taxEmail;

    @NotBlank
    private String organizerIdCard; //파일 경로 작성

    private LocalDateTime startDate;

    private LocalDateTime endDate;

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

    @ManyToMany
    @JoinTable(
            name = "funding_tag",
            joinColumns = @JoinColumn(name = "funding_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tagList;

    @OneToMany(mappedBy = "funding", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Document> documentList;

    @OneToMany(mappedBy =  "funding", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<IntroImage> introImgList;

    @OneToMany(mappedBy = "funding", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Reward> rewardList;

    @OneToMany(mappedBy = "funding", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Funder> funderList;

}