package my.diary.weather.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {}

