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
import sg.backend.dto.object.FundingDataDto;
import sg.backend.dto.object.ShortFundingDataDto;
import sg.backend.dto.object.UserDataDto;
import sg.backend.dto.request.auth.LoginRequestDto;
import sg.backend.dto.request.auth.SignUpRequestDto;
import sg.backend.dto.request.user.PatchPhoneNumberRequestDto;
import sg.backend.dto.request.user.PatchUserProfileRequestDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.auth.LoginResponseDto;
import sg.backend.dto.response.auth.SignUpResponseDto;
import sg.backend.dto.response.funding.GetFundingListResponseDto;
import sg.backend.dto.response.funding.GetMyFundingListResponseDto;
import sg.backend.dto.response.user.GetUserListResponseDto;
import sg.backend.dto.response.user.GetUserProfileResponseDto;
import sg.backend.entity.*;
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
    private final JPAQueryFactory queryFactory;

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${profile.path}")
    private String profileFilePath;

    @Value("${profile.url}")
    private String profileFileUrl;

    @Value("${spring.invite.adminCode}")
    private String adminInviteCode;

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

        Notification notification = new Notification(user, LocalDateTime.now().format(formatter));
        notification.setStartMessage();
        notificationRepository.save(notification);

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

    public ResponseEntity<? super ResponseDto> modifyPhoneNumber(PatchPhoneNumberRequestDto dto, String email) {

        User user;

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if(optionalUser.isEmpty()) return ResponseDto.noExistUser();
            user = optionalUser.get();

            String phoneNumber = dto.getPhoneNumber();
            user.setPhoneNumber(phoneNumber);
            userRepository.save(user);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success();
    }
    public ResponseEntity<? super ResponseDto> modifyProfile(MultipartFile profileImage, PatchUserProfileRequestDto dto, String email) {

        User user;

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if(optionalUser.isEmpty()) return ResponseDto.noExistUser();
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
                if(existedNickname) return ResponseDto.duplicateNickname();
            }

            if(password == null || password.isEmpty()) {
                dto.setPassword(user.getPassword());
            } else {
                if(!password.equals(confirmPassword))
                    return ResponseDto.validationFailed();

                String regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\\$%\\^&\\*])(?=\\S+$).{8,20}$";
                Pattern pattern = Pattern.compile(regexp);

                if(!pattern.matcher(password).matches())
                    return ResponseDto.validationFailed();
                if(passwordEncoder.matches(password, user.getPassword()))
                    return ResponseDto.PWSameAsCurrent();
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
                    return ResponseDto.validationFailed();
                }
            }

            user.patchUserProfile(dto, imageUrl);
            userRepository.save(user);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success();
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
                dto.setFundingId(f.getFunding_id());
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

    public ResponseEntity<? super GetUserListResponseDto> getUserList(String email, String sort, String id, int page, int size) {

        User admin;
        QUser user = QUser.user;
        BooleanBuilder filterBuilder = new BooleanBuilder();
        Page<User> userList;
        List<UserDataDto> data = new ArrayList<>();

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if(optionalUser.isEmpty()) return ResponseDto.noExistUser();
            admin = optionalUser.get();

            if(!admin.getRole().equals(Role.ADMIN))
                return ResponseDto.noPermission();

            filterBuilder.and(user.role.ne(Role.ADMIN));

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

            userList = new PageImpl<>(results, pageable, total);

            for(User u : userList) {
                data.add(convertToUserDataDto(u));
            }


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetUserListResponseDto.success(userList, data, sort);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sort, QUser user) {
        switch (sort) {
            case "noAsc":
                return user.userId.asc();
            case "noDesc":
                return user.userId.desc();
            case "idAsc":
                return user.email.asc();
            case "isDesc":
                return user.email.desc();
            case "nicknameAsc":
                return user.nickname.asc();
            case "nicknameDesc":
                return user.nickname.desc();
            case "emailAsc":
                return user.schoolEmail.asc();
            case "emailDesc":
                return user.schoolEmail.desc();
            case "phoneNumAsc":
                return user.phoneNumber.asc();
            case "phoneNumDesc":
                return user.phoneNumber.desc();
            case "adAsc":
                return Expressions.stringTemplate("CASE WHEN {0} IS NOT NULL AND {0} != '' THEN CONCAT({0}, ' ', {1}) ELSE CONCAT({2}, ' ', {1}) END",
                        user.roadAddress, user.detailAddress, user.landLotAddress).asc();
            case "adDesc":
                return Expressions.stringTemplate("CASE WHEN {0} IS NOT NULL AND {0} != '' THEN CONCAT({0}, ' ', {1}) ELSE CONCAT({2}, ' ', {1}) END",
                        user.roadAddress, user.detailAddress, user.landLotAddress).desc();
            case "latest":
                return user.createdAt.desc();
            case "oldest":
                return user.createdAt.asc();
            default:
                return user.createdAt.asc();
        }
    }

    public UserDataDto convertToUserDataDto(User user) {
        UserDataDto dto = new UserDataDto();

        dto.setUserId(user.getUserId());

        if(user.getNickname() == null)
            dto.setNickname("");
        else
            dto.setNickname(user.getNickname());

        String email = user.getEmail();
        int emailIndex = email.indexOf("@");
        dto.setId(email.substring(0, emailIndex));
        dto.setPhoneNumber(user.getPhoneNumber());

        if(user.getSchoolEmail() == null)
            dto.setSchoolEmail("");
        else
            dto.setSchoolEmail(user.getSchoolEmail());

        String roadAddress = user.getRoadAddress();
        String landLotAddress = user.getLandLotAddress();
        String detailAddress = user.getDetailAddress();
        StringBuilder address = new StringBuilder();
        if(roadAddress != null && detailAddress != null) {
            address.append(roadAddress + " ").append(detailAddress);
        } else if(landLotAddress != null && detailAddress != null) {
            address.append(landLotAddress + " ").append(detailAddress);

        }
        dto.setAddress(address.toString());
        dto.setCreatedAt(user.getCreatedAt().format(formatter));

        return dto;
    }

    public ResponseEntity<? super ResponseDto> changeUserState(String email, Long userId, String role) {

        User admin;

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) return ResponseDto.noExistUser();
            admin = optionalUser.get();

            if(!admin.getRole().equals(Role.ADMIN))
                return ResponseDto.noPermission();

            User user = userRepository.findByUserId(userId);
            if(user == null) return ResponseDto.noExistUser();

            Role existedRole = user.getRole();
            user.setRole(Role.valueOf(role));
            userRepository.save(user);

            if(existedRole != user.getRole()) {
                if(role.equals("USER")) {
                    Notification notification = new Notification(user, LocalDateTime.now().format(formatter));
                    notification.setAccountRestorationMessage();
                    notificationRepository.save(notification);
                }
                if(role.equals("SUSPENDED")) {
                    Notification notification = new Notification(user, LocalDateTime.now().format(formatter));
                    notification.setAccountSuspensionMessage();
                    notificationRepository.save(notification);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success();
    }
}