package sg.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "funding_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class FundingTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long funding_tag_id;

    @ManyToOne
    @JoinColumn(name = "funding_id")
    private Funding funding;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

}
