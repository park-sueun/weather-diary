package my.diary.weather.controller;

import lombok.RequiredArgsConstructor;
import my.diary.weather.global.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestAuthController {

    // USER 이상 접근 가능
    @GetMapping("/user")
    public String userEndpoint(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return "USER OK: " + user.getEmail();
    }

    // ADMIN만 접근 가능
    @GetMapping("/admin")
    public String adminEndpoint(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return "ADMIN OK: " + user.getEmail();
    }

    // 인증만 되면 접근 가능
    @GetMapping("/auth")
    public String authEndpoint(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return "AUTH OK: " + user.getUserId();
    }
}
