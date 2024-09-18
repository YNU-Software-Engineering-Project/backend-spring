package sg.backend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import sg.backend.dto.request.user.PatchPhoneNumberRequestDto;
import sg.backend.dto.request.user.PatchUserProfileRequestDto;
import sg.backend.dto.response.user.GetUserProfileResponseDto;
import sg.backend.dto.response.user.PatchPhoneNumberResponseDto;
import sg.backend.dto.response.user.PatchUserProfileResponseDto;

public interface UserService {

    ResponseEntity<? super GetUserProfileResponseDto> getUserProfile(Long userId);
    ResponseEntity<? super PatchPhoneNumberResponseDto> modifyPhoneNumber(PatchPhoneNumberRequestDto dto, Long userId);
    ResponseEntity<? super PatchUserProfileResponseDto> modifyProfile(MultipartFile profileImage, PatchUserProfileRequestDto dto, Long userId);
}
