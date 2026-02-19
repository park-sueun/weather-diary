package my.diary.weather.user;

import jakarta.persistence.*;
import lombok.*;
import my.diary.weather.global.entity.BaseTimeEntity;

@Entity
@Table(
        name = "app_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_user_email", columnNames = "email")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // // For JPA use only - "객체를 아무 곳에서나 new 하지 못하게 하기 위해"
public class AppUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password; // BCrypt 해시 저장

    @Column(nullable = false, length = 100)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role = UserRole.USER;

    @Builder
    private AppUser(String email, String password, String nickname, UserRole role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = (role == null) ? UserRole.USER : role;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
}

