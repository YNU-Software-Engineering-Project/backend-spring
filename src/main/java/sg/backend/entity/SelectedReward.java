package sg.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "selected_reward")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class SelectedReward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long selRewardId;

    private Integer selQuantity;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funder_id")
    private Funder funder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_id")
    private Reward reward;

    public SelectedReward(Funder funder, Reward reward, int quantity){
        this.funder = funder;
        this.reward = reward;
        this.selQuantity = quantity;
    }
}
