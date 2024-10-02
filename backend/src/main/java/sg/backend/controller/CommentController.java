package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sg.backend.dto.request.community.CommentRequestDto;
import sg.backend.dto.response.community.CommentResponseDto;
import sg.backend.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "커뮤니티 댓글 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 생성 성공"),
    })
    @PostMapping("/question/{questionId}")
    public ResponseEntity<String> createComment(
            @PathVariable("questionId") Long questionId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal(expression = "username") String email){
        return commentService.createComment(questionId, requestDto, email);
    }

    @Operation(summary = "특정 질문의 댓글들 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 조회 성공"),
    })
    @Transactional
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByQuestion(
            @PathVariable Long questionId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size",defaultValue = "5") int size) {
        return commentService.getCommentsByQuestion(questionId, page, size);
    }

    @Operation(summary = "커뮤니티 댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal(expression = "username")String email){
        return commentService.deleteComment(commentId, email);
    }
}
