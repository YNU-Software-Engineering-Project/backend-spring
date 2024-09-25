package sg.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sg.backend.dto.request.community.CommentRequestDto;
import sg.backend.dto.response.community.CommentResponseDto;
import sg.backend.entity.Question;
import sg.backend.entity.User;
import sg.backend.entity.Comment;
import sg.backend.repository.CommentRepository;
import sg.backend.repository.QuestionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final QuestionRepository questionRepository;

    public ResponseEntity<String> createComment(Long questionId, CommentRequestDto requestDto, User authenticatedUser){
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        Question question = null;
        if(optionalQuestion.isPresent())
            question = optionalQuestion.get();
        Comment comment = requestDto.toEntity(question, authenticatedUser);
        commentRepository.save(comment);
        return CommentResponseDto.success("댓글이 생성 되었습니다.");
    }

    public ResponseEntity<List<CommentResponseDto>> getCommentsByQuestion(Long questionId, int page, int size){
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        Question question = null;
        if(optionalQuestion.isPresent())
            question = optionalQuestion.get();
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findByQuestion(question,pageRequest);

        List<CommentResponseDto> responseDtoe = CommentResponseDto.fromEntityList(commentPage.getContent());
        return ResponseEntity.ok(responseDtoe);
    }

    public ResponseEntity<String> deleteComment(Long commentId, User authenticatedUser) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Comment comment = null;
        if(optionalComment.isPresent()){
            comment = optionalComment.get();
        }
        if(!comment.getUser().getUserId().equals(authenticatedUser.getUserId())) {
            throw new SecurityException("삭제할 수 있는 권한이 없습니다.");
        }
        commentRepository.deleteById(commentId);
        return CommentResponseDto.success("댓글이 삭제 되었습니다.");
    }
}
