package sg.backend.service;

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

import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final FundingRepository fundingRepository;

    public QuestionService(QuestionRepository questionRepository, FundingRepository fundingRepository){
        this.questionRepository = questionRepository;
        this.fundingRepository = fundingRepository;
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

    public Page<Question> getQuestionsByFunding(Long fundingId, int page, int size){
        Optional<Funding> optionalFunding = fundingRepository.findById(fundingId);
        Funding funding = null;
        if(optionalFunding.isPresent())
            funding = optionalFunding.get();
        PageRequest pageRequest = PageRequest.of(page, size);
        return questionRepository.findByFunding(funding, pageRequest);
    }

    public ResponseEntity<String> deleteQuestion(Long questionId, User authenticatedUser){
        Optional<Question> question = questionRepository.findById(questionId);
        if (!question.get().getUser().getUserId().equals(authenticatedUser.getUserId())){
            throw new SecurityException("삭제할 수 있는 권한이 없습니다.");
        }
        questionRepository.deleteById(questionId);
        return QuestionResponseDto.success("질문이 삭제 되었습니다.");
    }
}
