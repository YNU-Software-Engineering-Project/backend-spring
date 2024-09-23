package sg.backend.service;

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

import java.util.Optional;


@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final QuestionRepository questionRepository;

    public CommentService(CommentRepository commentRepository, QuestionRepository questionRepository){
        this.commentRepository = commentRepository;
        this.questionRepository = questionRepository;
    }

    public ResponseEntity<String> createComment(Long questionId, CommentRequestDto requestDto, User authenticatedUser){
        Optional<Question> question = questionRepository.findById(questionId);
        Comment comment = requestDto.toEntity(question, authenticatedUser);
        commentRepository.save(comment);
        return CommentResponseDto.success("댓글이 생성 되었습니다.");
    }

    public Page<Comment> getCommentsByQuestion(Long questionId, int page, int size){
        Optional<Question> question = questionRepository.findById(questionId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return commentRepository.findByQuestion(question, pageRequest);
    }

    public ResponseEntity<String> deleteComment(Long commentId, User authenticatedUser){
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (!comment.get().getUser().getUserId().equals(authenticatedUser.getUserId())){
            throw new SecurityException("삭제할 수 있는 권한이 없습니다.");
        }
        commentRepository.deleteById(commentId);
        return CommentResponseDto.success("댓글이 삭제 되었습니다.");
    }
    }
}
