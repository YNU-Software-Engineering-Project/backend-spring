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
    private Long reward_id;

    private Integer amount;

    private String reward_name;

    private String reward_description;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "funding_id")
    private Funding funding;

    @OneToMany(mappedBy = "reward")
    private List<SelectedReward> selrewardList;
}
