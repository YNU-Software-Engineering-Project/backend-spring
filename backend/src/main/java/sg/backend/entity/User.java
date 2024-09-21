package sg.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import sg.backend.dto.request.user.PatchUserProfileRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;

    private String nickname;

    private String password;

    @Enumerated(EnumType.STRING) // Enum을 String으로 매핑
    private Role role;

    private String phoneNumber;

    private String schoolEmail;

    private Boolean schoolEmailVerified;

    private String postalCode;

    private String roadAddress;

    private String landLotAddress;

    private String detailAddress;

    private String profileImage;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Funding> fundingList;

    @OneToMany(mappedBy = "user")
    private List<Funder> funderList;

    public void patchUserProfile(PatchUserProfileRequestDto dto, String imageUrl) {
        this.profileImage = imageUrl;
        this.nickname = dto.getNickname();
        this.password = dto.getPassword();
        this.postalCode = dto.getPostalCode();
        this.roadAddress = dto.getRoadAddress();
        this.landLotAddress = dto.getLandLotAddress();
        this.detailAddress = dto.getDetailAddress();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setSchoolEmail(String email) {
        this.schoolEmail = email;
    }

    public void setSchoolEmailVerified(boolean b) {
        schoolEmailVerified = b;
    }
}
