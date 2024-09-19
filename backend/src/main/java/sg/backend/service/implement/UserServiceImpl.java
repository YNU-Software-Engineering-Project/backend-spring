package sg.backend.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sg.backend.dto.request.user.PatchPhoneNumberRequestDto;
import sg.backend.dto.request.user.PatchUserProfileRequestDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.user.GetUserProfileResponseDto;
import sg.backend.dto.response.user.PatchPhoneNumberResponseDto;
import sg.backend.dto.response.user.PatchUserProfileResponseDto;
import sg.backend.entity.User;
import sg.backend.repository.UserRepository;
import sg.backend.service.UserService;

import java.io.File;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${file.path}")
    private String filePath;

    @Value("${file.url}")
    private String fileUrl;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<? super GetUserProfileResponseDto> getUserProfile(Long userId) {

        User user = null;

        try {
            user = userRepository.findByUserId(userId);
            if(user == null) return GetUserProfileResponseDto.noExistUser();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetUserProfileResponseDto.success(user);
    }

    @Override
    public ResponseEntity<? super PatchPhoneNumberResponseDto> modifyPhoneNumber(PatchPhoneNumberRequestDto dto, Long userId) {

        User user = null;

        try {
            user = userRepository.findByUserId(userId);
            if(user == null) return PatchPhoneNumberResponseDto.noExistUser();

            String phoneNumber = dto.getPhoneNumber();
            user.setPhoneNumber(phoneNumber);
            userRepository.save(user);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PatchPhoneNumberResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PatchUserProfileResponseDto> modifyProfile(MultipartFile profileImage, PatchUserProfileRequestDto dto, Long userId) {

        User user = null;

        try {
            user = userRepository.findByUserId(userId);
            if(user == null) return PatchUserProfileResponseDto.noExistUser();

            String nickname = dto.getNickname();
            String password = dto.getPassword();
            String confirmPassword = dto.getConfirmPassword();
            String postalCode = dto.getPostalCode();
            String roadAddress = dto.getRoadAddress();
            String landLotAddress = dto.getLandLotAddress();
            String detailAddress = dto.getDetailAddress();
            String imageUrl = null;

            if(profileImage != null && !profileImage.isEmpty()) {
                if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
                    String fileUrl = user.getProfileImage();
                    int index = fileUrl.lastIndexOf("/");
                    String fileName = fileUrl.substring(index+1);
                    String path = filePath + fileName;
                    File file = new File(path);

                    if (file.exists()) {
                        try {
                            if (!file.delete()) {
                                return ResponseDto.databaseError();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return ResponseDto.databaseError();
                        }
                    }
                }

                File directory = new File("/app/files/");
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String originalFileName = profileImage.getOriginalFilename();
                String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String uuid = UUID.randomUUID().toString();
                String saveFileName = uuid + extension;
                String savePath = filePath + saveFileName;

                profileImage.transferTo(new File(savePath));

                imageUrl = fileUrl + saveFileName;
            }

            if(!nickname.equals(user.getNickname())) {
                boolean existedNickname = userRepository.existsByNickname(nickname);
                if(existedNickname) return PatchUserProfileResponseDto.duplicateNickname();
            }

            if(password == null || password.isEmpty()) {
                dto.setPassword(user.getPassword());
            } else {
                if(!password.equals(confirmPassword))
                    return PatchUserProfileResponseDto.validationFailed();
                if(password.length() < 8 || password.length() > 20)
                    return PatchUserProfileResponseDto.validationFailed();
                if(passwordEncoder.matches(password, user.getPassword()))
                    return PatchUserProfileResponseDto.PWSameAsCurrent();
                else {
                    String encodedPassword = passwordEncoder.encode(password);
                    dto.setPassword(encodedPassword);
                }
            }

            if(isRequiredAddressValidation(postalCode, roadAddress, landLotAddress, detailAddress)) {
                boolean case1 = !isEmpty(postalCode) && !isEmpty(roadAddress) && !isEmpty(detailAddress);
                boolean case2 = !isEmpty(postalCode) && !isEmpty(landLotAddress) && !isEmpty(detailAddress);

                if (!(case1 || case2)) {
                    return PatchUserProfileResponseDto.validationFailed();
                }
            }

            user.patchUserProfile(dto, imageUrl);
            userRepository.save(user);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PatchUserProfileResponseDto.success();
    }

    private boolean isRequiredAddressValidation(String postalCode, String roadAddress, String landLotAddress, String detailAddress) {
        if(isEmpty(postalCode) && isEmpty(roadAddress) && isEmpty(landLotAddress) && isEmpty(detailAddress))
            return false;
        else
            return true;
    }

    private boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

}