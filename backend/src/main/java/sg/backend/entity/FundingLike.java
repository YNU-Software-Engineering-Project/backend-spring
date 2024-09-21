package sg.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "funding_like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FundingLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fundingLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_id")
    private Funding funding;

    private LocalDateTime createdAt;
}
