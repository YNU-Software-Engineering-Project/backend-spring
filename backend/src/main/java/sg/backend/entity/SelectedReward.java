package sg.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "selected_reward")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class SelectedReward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long selreward_id;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "funder_id")
    private Funder funder;
    @ManyToOne
    @JoinColumn(name = "reward_id")
    private Reward reward;
}
