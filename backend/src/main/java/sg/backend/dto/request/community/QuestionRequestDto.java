package sg.backend.dto.request.community;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sg.backend.entity.Funding;
import sg.backend.entity.Question;
import sg.backend.entity.User;

import java.util.Optional;

@Getter
@NoArgsConstructor
@Setter
public class QuestionRequestDto {

    private String content;

    public Question toEntity(Optional<Funding> funding, User user){
        return new Question(funding, user, content);
    }
}

