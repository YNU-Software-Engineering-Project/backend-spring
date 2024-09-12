package sg.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Getter
public class User {

    public enum Role { USER, ADMIN }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

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

    @Column(name = "created_at") // 칼럼명을 명시적으로 설정
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private List<Funding> fundingList;

    @OneToMany(mappedBy = "user")
    private List<Funder> funderList;
}
