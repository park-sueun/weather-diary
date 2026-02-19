package my.diary.weather.auth;

import lombok.RequiredArgsConstructor;
import my.diary.weather.auth.dto.LoginRequest;
import my.diary.weather.auth.dto.SignupRequest;
import my.diary.weather.auth.dto.TokenResponse;
import my.diary.weather.global.security.CustomUserDetails;
import my.diary.weather.global.security.JwtTokenProvider;
import my.diary.weather.user.AppUser;
import my.diary.weather.user.AppUserRepository;
import my.diary.weather.user.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final AppUserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    // =========================
    // 회원가입
    // =========================
    @Transactional
    public void signup(SignupRequest req) {

        if (userRepository.existsByEmail(req.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        AppUser user = AppUser.builder()
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))
                .nickname(req.nickname())
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
    }

    // =========================
    // 로그인
    // =========================
    @Transactional
    public TokenResponse login(LoginRequest req) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.email(),
                        req.password()
                )
        );

        CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();

        AppUser user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        String access = tokenProvider.createAccessToken(user);
        String refresh = tokenProvider.createRefreshToken(user);

        saveOrUpdateRefreshToken(user, refresh);

        return new TokenResponse(access, refresh);
    }

    // =========================
    // refresh
    // =========================
    @Transactional
    public TokenResponse refresh(String refreshToken) {

        if (!tokenProvider.isValid(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        Long userId = tokenProvider.getUserId(refreshToken);

        RefreshToken stored = refreshTokenRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        if (!stored.getToken().equals(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh mismatch");
        }

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        String newAccess = tokenProvider.createAccessToken(user);
        String newRefresh = tokenProvider.createRefreshToken(user);

        stored.rotate(newRefresh, OffsetDateTime.now().plusDays(14));

        return new TokenResponse(newAccess, newRefresh);
    }

    // =========================
    // logout
    // =========================
    @Transactional
    public void logout(Long userId) {
        refreshTokenRepository.deleteByUser_Id(userId);
    }

    private void saveOrUpdateRefreshToken(AppUser user, String refresh) {
        OffsetDateTime exp = OffsetDateTime.now().plusDays(14);

        refreshTokenRepository.findByUser_Id(user.getId())
                .ifPresentOrElse(
                        rt -> rt.rotate(refresh, exp),
                        () -> refreshTokenRepository.save(
                                new RefreshToken(user, refresh, exp)
                        )
                );
    }
}
