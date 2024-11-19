package sg.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.writefunding.PaymentEmailResponseDto;
import sg.backend.entity.User;
import sg.backend.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final UserRepository userRepository;

    public ResponseEntity<? super PaymentEmailResponseDto> paymentEmail(Long user_id){
        try{
            Optional<User> option = userRepository.findById(user_id);
            if(option.isEmpty()) {
                return ResponseDto.noExistUser();
            }
            User user = option.get();

            Boolean result = false;
            //이메일 확인
            if(user.getEmail() == null || user.getEmail().isEmpty()) {
                result = true;
            }
            //주소 확인
            if(user.getDetailAddress() == null || user.getDetailAddress().isEmpty()) {
                result = true;
            }
            if(user.getPostalCode() == null || user.getPostalCode().isEmpty()) {
                result = true;
            }
            if(user.getRoadAddress() == null || user.getRoadAddress().isEmpty()) {
                result = true;
            }
            if(user.getLandLotAddress() == null || user.getLandLotAddress().isEmpty()) {
                result = true;
            }
            //휴대전화 확인
            if(user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty()) {
                result = true;
            }

            if(result){
                return PaymentEmailResponseDto.not_existed_data();
            }
            return ResponseDto.success();
        } catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
    }

}
