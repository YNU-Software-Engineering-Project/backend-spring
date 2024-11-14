package sg.backend.dto.request.community;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sg.backend.entity.Comment;
import sg.backend.entity.Question;
import sg.backend.entity.User;


@Getter
@NoArgsConstructor
@Setter
public class CommentRequestDto {

    private String content;
    private String title;

    public Comment toEntity(Question question, User user){
        return new Comment(question, user, title, content);
    }
}
