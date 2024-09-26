package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sg.backend.dto.request.community.QuestionRequestDto;
import sg.backend.dto.response.community.QuestionResponseDto;
import sg.backend.service.QuestionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    @Operation(summary = "커뮤니티 질문 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "질문 생성 성공"),
    })
    @PostMapping("/funding/{fundingId}")
    public ResponseEntity<String> createQuestion(
            @PathVariable("fundingId") Long fundingId,
            @RequestBody QuestionRequestDto requestDto,
            @AuthenticationPrincipal(expression = "username") String email){
        return questionService.createQuestion(fundingId, requestDto, email);
    }

    @Operation(summary = "특정 펀딩의 질문들 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "질문 조회 성공"),
    })
    @Transactional
    @GetMapping("/funding/{fundingId}")
    public ResponseEntity<List<QuestionResponseDto>> getQuestionsByFunding(
            @PathVariable("fundingId") Long fundingId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return questionService.getQuestionsByFunding(fundingId, page, size);
    }

    @Operation(summary = "커뮤니티 질문 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "질문 삭제 성공"),
    })
    @DeleteMapping("/{questionId}")
    public ResponseEntity<String> deleteQuestion(
            @PathVariable("questionId") Long questionId,
            @AuthenticationPrincipal(expression = "username") String email){
        return questionService.deleteQuestion(questionId, email);
    }
}
