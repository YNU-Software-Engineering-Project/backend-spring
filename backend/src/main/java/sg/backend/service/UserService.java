package sg.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.object.FundingDataDto;
import sg.backend.dto.request.user.PatchPhoneNumberRequestDto;
import sg.backend.dto.request.user.PatchUserProfileRequestDto;
import sg.backend.dto.response.user.PatchPhoneNumberResponseDto;
import sg.backend.dto.response.user.PatchUserProfileResponseDto;
import sg.backend.dto.object.ShortFundingDataDto;
import sg.backend.dto.request.auth.LoginRequestDto;
import sg.backend.dto.request.auth.SignUpRequestDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.auth.LoginResponseDto;
import sg.backend.dto.response.auth.SignUpResponseDto;
import sg.backend.dto.response.funding.GetFundingListResponseDto;
import sg.backend.dto.response.funding.GetMyFundingListResponseDto;
import sg.backend.dto.response.user.GetUserProfileResponseDto;
import sg.backend.entity.Funding;
import sg.backend.entity.Notification;
import sg.backend.entity.User;
import sg.backend.jwt.TokenProvider;
import sg.backend.repository.*;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static sg.backend.service.FundingService.convertToDto;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FundingLikeRepository fundingLikeRepository;
    private final FunderRepository funderRepository;
    private final FundingRepository fundingRepository;
    private final NotificationRepository notificationRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${profile.path}")
    private String profileFilePath;

    @Value("${profile.url}")
    private String profileFileUrl;

    public ResponseEntity<ResponseDto> signup(SignUpRequestDto signupRequestDto) {
        if (userRepository.findByEmail(signupRequestDto.getEmail()).isPresent()) {
            return SignUpResponseDto.duplicateEmail();
        }

        if (!signupRequestDto.getPassword().equals(signupRequestDto.getPasswordConfirm())) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDto(ResponseCode.VALIDATION_FAILED, ResponseMessage.VALIDATION_FAILED));
        }

        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        User user = new User(signupRequestDto.getEmail(), encodedPassword, signupRequestDto.getPhoneNumber());
        userRepository.save(user);

        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2));

        Notification notification = new Notification(user, LocalDateTime.now().format(formatter));
        notification.setStartMessage();
        notificationRepository.save(notification);

        return SignUpResponseDto.success(accessToken);
    }
  
    public ResponseEntity<?> login(LoginRequestDto loginRequestDto){
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if(user.getUserId() == 1 || user.getUserId() == 2) {
            if(!loginRequestDto.getPassword().equals(user.getPassword()))
                return LoginResponseDto.failure();
        } else if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            return LoginResponseDto.failure();
        }

        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2));
        return LoginResponseDto.success(accessToken);
    }

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }
    
    public ResponseEntity<? super GetUserProfileResponseDto> getUserProfile(String email) {

        User user;

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if(optionalUser.isEmpty()) return GetUserProfileResponseDto.noExistUser();
            user = optionalUser.get();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetUserProfileResponseDto.success(user);
    }

    public ResponseEntity<? super PatchPhoneNumberResponseDto> modifyPhoneNumber(PatchPhoneNumberRequestDto dto, String email) {

        User user;

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if(optionalUser.isEmpty()) return PatchPhoneNumberResponseDto.noExistUser();
            user = optionalUser.get();

            String phoneNumber = dto.getPhoneNumber();
            user.setPhoneNumber(phoneNumber);
            userRepository.save(user);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PatchPhoneNumberResponseDto.success();
    }
    public ResponseEntity<? super PatchUserProfileResponseDto> modifyProfile(MultipartFile profileImage, PatchUserProfileRequestDto dto, String email) {

        User user;

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if(optionalUser.isEmpty()) return PatchUserProfileResponseDto.noExistUser();
            user = optionalUser.get();

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
                    String path = profileFilePath + fileName;
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

                File directory = new File("/app/data/profile_images/");
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String originalFileName = profileImage.getOriginalFilename();
                String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String uuid = UUID.randomUUID().toString();
                String saveFileName = uuid + extension;
                String savePath = profileFilePath + saveFileName;

                profileImage.transferTo(new File(savePath));

                imageUrl = profileFileUrl + saveFileName;
            }

            if(nickname != null && !nickname.isEmpty() && !nickname.equals(user.getNickname())) {
                boolean existedNickname = userRepository.existsByNickname(nickname);
                if(existedNickname) return PatchUserProfileResponseDto.duplicateNickname();
            }

            if(password == null || password.isEmpty()) {
                dto.setPassword(user.getPassword());
            } else {
                if(!password.equals(confirmPassword))
                    return PatchUserProfileResponseDto.validationFailed();

                String regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\\$%\\^&\\*])(?=\\S+$).{8,20}$";
                Pattern pattern = Pattern.compile(regexp);

                if(!pattern.matcher(password).matches())
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
                boolean case3 = !isEmpty(postalCode) && !isEmpty(roadAddress) && !isEmpty(landLotAddress) && !isEmpty(detailAddress);

                if (!(case1 || case2 || case3)) {
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
        return value == null || value.isBlank();
    }

    @Transactional
    public ResponseEntity<? super GetFundingListResponseDto> getWishList(String email, int page, int size) {

        User user;
        Page<Funding> fundingList;
        List<FundingDataDto> data = new ArrayList<>();


        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if(optionalUser.isEmpty()) return GetUserProfileResponseDto.noExistUser();
            user = optionalUser.get();

            PageRequest pageRequest = PageRequest.of(page, size);
            fundingList = fundingLikeRepository.findFundingLikedByUserIdOrderByLikeCreatedAt(user.getUserId(), pageRequest);

            for(Funding f : fundingList) {
                data.add(convertToDto(f, fundingLikeRepository, true, user));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetFundingListResponseDto.success(fundingList, data);
    }

    @Transactional
    public ResponseEntity<? super GetFundingListResponseDto> getPledgeList(String email, int page, int size) {

        User user;
        Page<Funding> fundingList;
        List<FundingDataDto> data = new ArrayList<>();

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) return GetFundingListResponseDto.noExistUser();
            user = optionalUser.get();

            PageRequest pageRequest = PageRequest.of(page, size);
            fundingList = funderRepository.findFundingByUserIdOrderByFunderCreatedAt(user.getUserId(), pageRequest);

            for (Funding f : fundingList) {
                data.add(convertToDto(f, fundingLikeRepository, true, user));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetFundingListResponseDto.success(fundingList, data);
    }

    public ResponseEntity<? super GetMyFundingListResponseDto> getMyFundingList(String email, int page, int size) {

        User user;
        Page<Funding> fundingList;
        List<ShortFundingDataDto> data = new ArrayList<>();
        int todayAmount = 0;
        int todayLikes = 0;

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if(optionalUser.isEmpty()) return GetMyFundingListResponseDto.noExistUser();
            user = optionalUser.get();

            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
            fundingList = fundingRepository.findByUser(user, pageRequest);

            for(Funding f : fundingList) {
                ShortFundingDataDto dto = new ShortFundingDataDto();
                dto.setTitle(f.getTitle());
                dto.setMainImage(f.getMainImage());
                dto.setState(String.valueOf(f.getCurrent()));

                todayAmount += f.getTodayAmount();
                todayLikes += f.getTodayLikes();
                data.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetMyFundingListResponseDto.success(fundingList, data, todayAmount, todayLikes);
    }
}
