package sg.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sg.backend.dto.request.auth.SignUpRequestDto;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickname;

    private String password;

    public User(SignUpRequestDto dto) {
        this.email = dto.getEmail();
        this.nickname = dto.getNickname();
        this.password = dto.getPassword();
    }
}