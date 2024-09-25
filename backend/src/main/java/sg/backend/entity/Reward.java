package sg.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "reward")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rewardId;

    private Integer amount;

    private String rewardName;

    private String rewardDescription;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_id")
    private Funding funding;

    @OneToMany(mappedBy = "reward")
    private List<SelectedReward> selrewardList;

    public Reward(Funding funding, Integer amount, String rewardName, String rewardDescription, Integer quantity) {
        this.funding = funding;
        this.amount = amount;
        this.rewardName = rewardName;
        this.rewardDescription = rewardDescription;
        this.quantity = quantity;
    }
}
