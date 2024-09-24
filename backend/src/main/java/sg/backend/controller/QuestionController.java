package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sg.backend.dto.request.community.QuestionRequestDto;
import sg.backend.dto.response.community.QuestionResponseDto;
import sg.backend.entity.Question;
import sg.backend.entity.User;
import sg.backend.service.QuestionService;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService){
        this.questionService = questionService;
    }

    @Operation(summary = "커뮤니티 질문 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "질문 생성 성공"),
    })
    @PostMapping("/funding/{fundingId}")
    public ResponseEntity<String> createQuestion(@PathVariable Long fundingId, @RequestBody QuestionRequestDto requestDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authentication.getPrincipal();
        return questionService.createQuestion(fundingId, requestDto, authenticatedUser);
    }

    @Operation(summary = "특정 펀딩의 질문들 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "질문 조회 성공"),
    })
    @GetMapping("/funding/{fundingId}")
    public ResponseEntity<List<QuestionResponseDto>> getQuestionsByFunding(
            @PathVariable("fundingId") Long fundingId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Question> questionPage = questionService.getQuestionsByFunding(fundingId, page, size);
        List<QuestionResponseDto> responseDtos = QuestionResponseDto.fromEntityList(questionPage.getContent());
        return QuestionResponseDto.success(responseDtos);
    }

    @Operation(summary = "커뮤니티 질문 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "질문 삭제 성공"),
    })
    @DeleteMapping("/{questionId}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long questionId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authentication.getPrincipal();
        return questionService.deleteQuestion(questionId, authenticatedUser);
    }
}
