package sg.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "story_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class StoryImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storyImage_id;

    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_id")
    private Funding funding;

    public StoryImage(String uuid, Funding funding) {
        this.uuid = uuid;
        this.funding = funding;
    }

}
