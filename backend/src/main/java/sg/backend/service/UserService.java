package sg.backend.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.object.*;
import sg.backend.dto.request.auth.LoginRequestDto;
import sg.backend.dto.request.auth.SignUpRequestDto;
import sg.backend.dto.request.user.PatchPhoneNumberRequestDto;
import sg.backend.dto.request.user.PatchUserProfileRequestDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.auth.LoginResponseDto;
import sg.backend.dto.response.auth.SignUpResponseDto;
import sg.backend.dto.response.funding.GetFundingListResponseDto;
import sg.backend.dto.response.funding.GetMyFundingListResponseDto;
import sg.backend.dto.response.user.GetUserInfoResponseDto;
import sg.backend.dto.response.user.GetUserListResponseDto;
import sg.backend.dto.response.user.GetUserProfileResponseDto;
import sg.backend.entity.*;
import sg.backend.exception.CustomException;
import sg.backend.exception.UnauthorizedAccessException;
import sg.backend.jwt.TokenProvider;
import sg.backend.repository.*;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static sg.backend.service.FileService.deleteCurrentProfileImage;

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
    private final JPAQueryFactory queryFactory;

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${profile.path}")
    private String profileFilePath;

    @Value("${profile.url}")
    private String profileFileUrl;

    @Value("${spring.invite.adminCode}")
    private String adminInviteCode;

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    public static User findUserByEmail(String email, UserRepository userRepository) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER));
    }

    public static User findUserById(Long userId, UserRepository userRepository) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER));
    }

    public static void checkAdminAccess(User user) {
        if(!user.getRole().equals(Role.ADMIN))
            throw new UnauthorizedAccessException();
    }

    public ResponseEntity<ResponseDto> signup(SignUpRequestDto signupRequestDto) {
        if (userRepository.findByEmail(signupRequestDto.getEmail()).isPresent()) {
            return SignUpResponseDto.duplicateEmail();
        }

        if (!signupRequestDto.getPassword().equals(signupRequestDto.getPasswordConfirm())) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDto(ResponseCode.VALIDATION_FAILED, ResponseMessage.VALIDATION_FAILED));
        }

        Role role = Role.USER;
        if(signupRequestDto.getInviteCode() != null && signupRequestDto.getInviteCode().equals(adminInviteCode)){
            role = Role.ADMIN;
        }

        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        User user = new User(signupRequestDto.getEmail(), encodedPassword, signupRequestDto.getPhoneNumber(),role);
        userRepository.save(user);

        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2));

        if(user.getRole() != Role.ADMIN) {
            Notification notification = new Notification(user, LocalDateTime.now().format(formatter));
            notification.setStartMessage();
            notificationRepository.save(notification);
        }

        return SignUpResponseDto.success(accessToken, role);
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

        Role role = user.getRole();

        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2));
        return LoginResponseDto.success(accessToken, role);
    }

    public ResponseEntity<GetUserProfileResponseDto> getUserProfile(String email) {
        User user = findUserByEmail(email, userRepository);

        UserProfileDataDto data = UserProfileDataDto.of(user);

        return GetUserProfileResponseDto.success(data);
    }

    @Transactional
    public ResponseEntity<ResponseDto> modifyPhoneNumber(PatchPhoneNumberRequestDto dto, String email) {
        User user = findUserByEmail(email, userRepository);

        String phoneNumber = dto.getPhoneNumber();
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);

        return ResponseDto.success();
    }

    @Transactional
    public ResponseEntity<ResponseDto> modifyProfile(MultipartFile profileImage, PatchUserProfileRequestDto dto, String email) {

        User user = findUserByEmail(email, userRepository);

        String imageUrl = processProfileImage(profileImage, user.getProfileImage(), user);

        if(dto.getNickname() != null) {
            validateNickname(dto.getNickname());
        } else {
            dto.setNickname(user.getNickname());
        }

        if(dto.getPassword() != null) {
            validatePassword(dto.getPassword(), dto.getConfirmPassword(), user.getPassword());
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        } else {
            dto.setPassword(user.getPassword());
        }

        if(dto.getPostalCode() != null || dto.getRoadAddress() != null || dto.getLandLotAddress() != null || dto.getDetailAddress() != null) {
            validateAddress(dto);
        }

        user.toEntity(dto, imageUrl);
        userRepository.save(user);
        return ResponseDto.success();
    }

    @Transactional
    public ResponseEntity<GetFundingListResponseDto> getWishList(String email, int page, int size) {

        User user = findUserByEmail(email, userRepository);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Funding> fundingList = fundingLikeRepository.findFundingLikedByUserIdOrderByLikeCreatedAt(user.getUserId(), pageRequest);

        List<FundingDataDto> data = fundingList.stream()
                .map(f -> FundingDataDto.of(f, fundingLikeRepository, true, user))
                .collect(Collectors.toList());

        return GetFundingListResponseDto.success(fundingList, data);
    }

    @Transactional
    public ResponseEntity<GetFundingListResponseDto> getPledgeList(String email, int page, int size) {

        User user = findUserByEmail(email, userRepository);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Funding> fundingList = funderRepository.findFundingByUserIdOrderByFunderCreatedAt(user.getUserId(), pageRequest);

        List<FundingDataDto> data = fundingList.stream()
                .map(f -> FundingDataDto.of(f, fundingLikeRepository, true, user))
                .collect(Collectors.toList());

        return GetFundingListResponseDto.success(fundingList, data);
    }

    public ResponseEntity<GetMyFundingListResponseDto> getMyFundingList(String email, int page, int size) {

        User user = findUserByEmail(email, userRepository);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Funding> fundingList = fundingRepository.findByUser(user, pageRequest);

        List<ShortFundingDataDto> data = fundingList.stream()
                .map(ShortFundingDataDto::of)
                .collect(Collectors.toList());

        int todayAmount = fundingList.stream().mapToInt(Funding::getTodayAmount).sum();
        int todayLikes = fundingList.stream().mapToInt(Funding::getTodayLikes).sum();

        return GetMyFundingListResponseDto.success(fundingList, data, todayAmount, todayLikes);
    }

    public ResponseEntity<GetUserListResponseDto> getUserList(String email, String sort, String id, int page, int size) {

        User admin = findUserByEmail(email, userRepository);
        checkAdminAccess(admin);

        QUser user = QUser.user;
        BooleanBuilder filterBuilder = new BooleanBuilder()
                .and(user.role.ne(Role.ADMIN));

        if(id != null) {
            StringTemplate userId = Expressions.stringTemplate("SUBSTRING({0}, 1, LOCATE('@', {0}) - 1)", user.email);
            filterBuilder.and(userId.contains(id));
        }

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sort, user);

        Pageable pageable = PageRequest.of(page, size);
        List<User> results = queryFactory.selectFrom(user)
                .where(filterBuilder)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(user)
                .where(filterBuilder)
                .fetch()
                .size();

        Page<User> userList = new PageImpl<>(results, pageable, total);

        List<UserDataDto> data = userList.stream()
                .map(UserDataDto::of)
                .collect(Collectors.toList());

        return GetUserListResponseDto.success(userList, data, sort);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sort, QUser user) {
        return switch (sort) {
            case "noAsc" -> user.userId.asc();
            case "noDesc" -> user.userId.desc();
            case "idAsc" ->
                    Expressions.stringTemplate("SUBSTRING({0}, 1, LOCATE('@', {0}) - 1)", user.email).asc();
            case "isDesc" ->
                    Expressions.stringTemplate("SUBSTRING({0}, 1, LOCATE('@', {0}) - 1)", user.email).desc();
            case "nicknameAsc" -> user.nickname.asc();
            case "nicknameDesc" -> user.nickname.desc();
            case "emailAsc" -> user.schoolEmail.asc();
            case "emailDesc" -> user.schoolEmail.desc();
            case "phoneNumAsc" -> user.phoneNumber.asc();
            case "phoneNumDesc" -> user.phoneNumber.desc();
            case "adAsc" ->
                    Expressions.stringTemplate("CASE WHEN {0} IS NOT NULL AND {0} != '' THEN CONCAT({0}, ' ', {1}) ELSE CONCAT({2}, ' ', {1}) END",
                            user.roadAddress, user.detailAddress, user.landLotAddress).asc();
            case "adDesc" ->
                    Expressions.stringTemplate("CASE WHEN {0} IS NOT NULL AND {0} != '' THEN CONCAT({0}, ' ', {1}) ELSE CONCAT({2}, ' ', {1}) END",
                            user.roadAddress, user.detailAddress, user.landLotAddress).desc();
            case "latest" -> user.createdAt.desc();
            default -> user.createdAt.asc();
        };
    }

    public ResponseEntity<GetUserInfoResponseDto> getUserInfo(String email, Long userId) {

        User admin = findUserByEmail(email, userRepository);
        checkAdminAccess(admin);
        User user = findUserById(userId, userRepository);

        UserInfoDataDto data = UserInfoDataDto.of(user);

        return GetUserInfoResponseDto.success(data);
    }

    public ResponseEntity<ResponseDto> changeUserState(String email, Long userId, String role) {

        User admin = findUserByEmail(email, userRepository);
        checkAdminAccess(admin);
        User user = findUserById(userId, userRepository);

        Role existedRole = user.getRole();
        Role newRole = Role.valueOf(role);

        user.setRole(Role.valueOf(role));
        userRepository.save(user);

        if(existedRole != user.getRole()) {
            sendRoleChangeNotification(user, newRole);
        }

        return ResponseDto.success();
    }

    private void validateNickname(String nickname) {
        boolean existedNickname = userRepository.existsByNickname(nickname);
        if(existedNickname)
            throw new CustomException(ResponseCode.DUPLICATE_NICKNAME, ResponseMessage.DUPLICATE_NICKNAME);
    }

    private void validatePassword(String newPassword, String confirmPassword, String userPassword) {
        if(!newPassword.equals(confirmPassword))
            throw new CustomException("PM", "The passwords do not match.");

        String regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=\\S+$).{8,20}$";
        Pattern pattern = Pattern.compile(regexp);
        if(!pattern.matcher(newPassword).matches())
            throw new CustomException(ResponseCode.VALIDATION_FAILED, "비밀번호는 8~20자이며, 숫자, 대문자, 소문자, 특수문자를 포함해야 합니다.");

        if(passwordEncoder.matches(newPassword, userPassword))
            throw new CustomException(ResponseCode.PASSWORD_SAME_AS_CURRENT, ResponseMessage.PASSWORD_SAME_AS_CURRENT);
    }

    @Transactional
    public String processProfileImage(MultipartFile profileImage, String currentImageUrl, User user) {
        if(profileImage == null)
            return currentImageUrl;

        if(currentImageUrl != null) {
            user.setProfileImage(null);
            deleteCurrentProfileImage(currentImageUrl, profileFilePath);
        }

        return saveProfileImage(profileImage);
    }

    private String saveProfileImage(MultipartFile profileImage) {
        String originalFileName = profileImage.getOriginalFilename();
        if (originalFileName == null) {
            throw new CustomException("OM", "Original file name is missing.");
        }

        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        String saveFileName = uuid + extension;
        String savePath = profileFilePath + saveFileName;

        try {
            profileImage.transferTo(new File(savePath));
        } catch (Exception e) {
            throw new CustomException("FF", "Failed to save file: " + e.getMessage());
        }

        return profileFileUrl + saveFileName;
    }

    private void validateAddress(PatchUserProfileRequestDto dto) {

        boolean hasPostalCode = dto.getPostalCode() != null && !dto.getPostalCode().isEmpty();
        boolean hasRoadAddress = dto.getRoadAddress() != null && !dto.getRoadAddress().isEmpty();
        boolean hasLandLotAddress = dto.getLandLotAddress() != null && !dto.getLandLotAddress().isEmpty();
        boolean hasDetailAddress = dto.getDetailAddress() != null && !dto.getDetailAddress().isEmpty();

        if(!hasPostalCode)
            throw new CustomException(ResponseCode.VALIDATION_FAILED, "우편번호를 입력해주세요.");
        if(!hasRoadAddress && !hasLandLotAddress)
            throw new CustomException(ResponseCode.VALIDATION_FAILED, "도로명 주소 또는 지번 주소를 입력해주세요");
        if(!hasDetailAddress)
            throw new CustomException(ResponseCode.VALIDATION_FAILED, "상세 주소를 입력해주세요.");
    }

    private void sendRoleChangeNotification(User user, Role role) {
        Notification notification = new Notification(user, LocalDateTime.now().format(formatter));
        notification.setAccountMessage(role);
        notificationRepository.save(notification);
    }
}