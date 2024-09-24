package sg.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_id")
    private Funding funding;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> commentList;

    public Question(Funding funding, User user, String content) {
        this.funding = funding;
        this.user = user;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public Collection<Comment> getComments() {
        return this.commentList;
    }
}
