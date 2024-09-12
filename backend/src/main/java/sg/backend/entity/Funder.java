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
    private Long funder_id;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")  //닉네임, 전화번호, 주소,
    private User user;
    @ManyToOne
    @JoinColumn(name = "funding_id")
    private Funding funding;


    @OneToMany(mappedBy = "funder")
    private List<SelectedReward> selrewardList;
}