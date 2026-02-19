package my.diary.weather.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // 사용자 기준 조회 (가장 많이 사용)
    Optional<RefreshToken> findByUser_Id(Long userId);

    // 토큰 값으로 조회 (선택)
    Optional<RefreshToken> findByToken(String token);

    // 로그아웃 시 삭제
    void deleteByUser_Id(Long userId);
}

