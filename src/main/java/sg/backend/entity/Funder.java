package sg.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "funder")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Funder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long funderId;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")  //닉네임, 전화번호, 주소,
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_id")
    private Funding funding;

    @OneToMany(mappedBy = "funder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SelectedReward> selrewardList;

    public Funder(User user,Funding funding){
        this.user = user;
        this.funding = funding;
    }
}