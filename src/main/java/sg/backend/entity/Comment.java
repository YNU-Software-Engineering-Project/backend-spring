package sg.backend.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;
    private String title;
    private LocalDateTime createdAt;

    public Comment(Question question, User user, String title, String content) {
        this.question = question;
        this.user = user;
        this.content = content;
        this.title = title;
        this.createdAt = LocalDateTime.now();
    }
}

