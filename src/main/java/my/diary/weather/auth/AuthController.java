package my.diary.weather.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.diary.weather.auth.dto.LoginRequest;
import my.diary.weather.auth.dto.RefreshRequest;
import my.diary.weather.auth.dto.SignupRequest;
import my.diary.weather.auth.dto.TokenResponse;
import my.diary.weather.global.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Valid @RequestBody SignupRequest req) {
        authService.signup(req);
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@Valid @RequestBody RefreshRequest req) {
        return authService.refresh(req.refreshToken());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@AuthenticationPrincipal CustomUserDetails user) {
        authService.logout(user.getUserId());
    }
}
