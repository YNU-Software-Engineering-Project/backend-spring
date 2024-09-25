package sg.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sg.backend.dto.request.community.QuestionRequestDto;
import sg.backend.dto.response.community.QuestionResponseDto;
import sg.backend.entity.Funding;
import sg.backend.entity.Question;
import sg.backend.entity.User;
import sg.backend.repository.FundingRepository;
import sg.backend.repository.QuestionRepository;
import sg.backend.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final FundingRepository fundingRepository;
    private final UserRepository userRepository;

    public User getUserFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey("YOUR_SECRET_KEY")
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();

        Long userId = claims.get("id", Long.class);
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    public ResponseEntity<String> createQuestion(Long fundingId, QuestionRequestDto requestDto, User authenticatedUser){
        Optional<Funding> optionalFunding = fundingRepository.findById(fundingId);
        Funding funding = null;
        if(optionalFunding.isPresent())
            funding = optionalFunding.get();
        Question question = requestDto.toEntity(funding, authenticatedUser);
        questionRepository.save(question);
        return QuestionResponseDto.success("질문이 생성 되었습니다.");
    }

    public ResponseEntity<List<QuestionResponseDto>> getQuestionsByFunding(Long fundingId, int page, int size){
        Optional<Funding> optionalFunding = fundingRepository.findById(fundingId);
        Funding funding = null;
        if(optionalFunding.isPresent())
            funding = optionalFunding.get();
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Question> questionPage = questionRepository.findByFunding(funding,pageRequest);

        List<QuestionResponseDto> responseDtoe = QuestionResponseDto.fromEntityList(questionPage.getContent());
        return ResponseEntity.ok(responseDtoe);
    }

    public ResponseEntity<String> deleteQuestion(Long questionId, User authenticatedUser){
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        Question question = null;
        if(optionalQuestion.isPresent()) {
            question = optionalQuestion.get();
        }
        if(!question.getUser().getUserId().equals(authenticatedUser.getUserId())) {
            throw new SecurityException("삭제할 수 있는 권한이 없습니다.");
        }
        questionRepository.deleteById(questionId);
        return QuestionResponseDto.success("질문이 삭제 되었습니다.");
    }
}
