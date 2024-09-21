package sg.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "funding_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FundingTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fundingTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_id")
    private Funding funding;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
