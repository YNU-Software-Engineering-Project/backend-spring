package sg.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sg.backend.dto.request.community.QuestionRequestDto;
import sg.backend.dto.response.community.QuestionResponseDto;
import sg.backend.entity.Funding;
import sg.backend.entity.Notification;
import sg.backend.entity.Question;
import sg.backend.entity.User;
import sg.backend.repository.FundingRepository;
import sg.backend.repository.NotificationRepository;
import sg.backend.repository.QuestionRepository;
import sg.backend.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static sg.backend.service.UserService.formatter;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final FundingRepository fundingRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public ResponseEntity<String> createQuestion(Long fundingId, QuestionRequestDto requestDto, String email){
        Optional<Funding> optionalFunding = fundingRepository.findById(fundingId);
        Funding funding = null;
        if(optionalFunding.isPresent())
            funding = optionalFunding.get();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Question question = requestDto.toEntity(funding, user);
        questionRepository.save(question);

        Notification notification = new Notification(funding.getUser(), LocalDateTime.now().format(formatter));
        String name = user.getNickname();
        if(name == null) {
            int index = email.indexOf("@");
            name = email.substring(0, index);
        }
        notification.setQuestionMessage(name, funding.getTitle());
        notificationRepository.save(notification);
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

    public ResponseEntity<String> deleteQuestion(Long questionId, String email){
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        Question question = null;
        if(optionalQuestion.isPresent()) {
            question = optionalQuestion.get();
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("User not found"));

        if(!question.getUser().getUserId().equals(user.getUserId())) {
            throw new SecurityException("삭제할 수 있는 권한이 없습니다.");
        }
        questionRepository.deleteById(questionId);
        return QuestionResponseDto.success("질문이 삭제 되었습니다.");
    }
}
