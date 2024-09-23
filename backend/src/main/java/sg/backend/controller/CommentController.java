package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sg.backend.dto.request.community.CommentRequestDto;
import sg.backend.dto.response.community.CommentResponseDto;
import sg.backend.entity.Comment;
import sg.backend.entity.User;
import sg.backend.service.CommentService;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @Operation(summary = "커뮤니티 댓글 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 생성 성공"),
    })
    @PostMapping("/question/{questionId}")
    public ResponseEntity<String> createComment(@PathVariable Long questionId, @RequestBody CommentRequestDto requestDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authentication.getPrincipal();
        return commentService.createComment(questionId, requestDto, authenticatedUser);
    }

    @Operation(summary = "특정 질문의 댓글들 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 조회 성공"),
    })
    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByQuestion(
            @PathVariable Long questionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Comment> commentPage = commentService.getCommentsByQuestion(questionId, page, size);
        List<CommentResponseDto> responseDtos = CommentResponseDto.fromEntityList(commentPage.getContent());
        return CommentResponseDto.success(responseDtos);
    }

    @Operation(summary = "커뮤니티 댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authentication.getPrincipal();
        return commentService.deleteComment(commentId, authenticatedUser);
    }
}
