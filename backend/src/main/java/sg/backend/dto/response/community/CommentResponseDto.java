package sg.backend.dto.response.community;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import sg.backend.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class CommentResponseDto {
    private Long commentId;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;

    public static CommentResponseDto fromEntity(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setCommentId(comment.getCommentId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setNickname(comment.getUser().getNickname());
        return dto;
    }

    public static List<CommentResponseDto> fromEntityList(List<Comment> comments){
        return comments.stream()
                .map(CommentResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public static ResponseEntity<List<CommentResponseDto>> success(List<CommentResponseDto> data){
        return ResponseEntity.ok(data);
    }

    public static ResponseEntity<String> success(String message){ return ResponseEntity.ok(message); }
}
