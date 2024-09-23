package sg.backend.dto.response.community;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import sg.backend.entity.Question;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class QuestionResponseDto {

    private Long questionId;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;
    private int commentCount;

    public static QuestionResponseDto fromEntity(Question question){
        QuestionResponseDto dto = new QuestionResponseDto();
        dto.setQuestionId(question.getQuestionId());
        dto.setContent(question.getContent());
        dto.setCreatedAt(question.getCreatedAt());
        dto.setNickname(question.getUser().getEmail());
        return dto;
    }

    public static List<QuestionResponseDto> fromEntityList(List<Question> questions){
        return questions.stream()
                .map(QuestionResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public static ResponseEntity<List<QuestionResponseDto>> success(List<QuestionResponseDto> data){
        return ResponseEntity.ok(data);
    }

    public static ResponseEntity<String> success(String message){ return ResponseEntity.ok(message); }
}
