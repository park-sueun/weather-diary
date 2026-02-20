package my.diary.weather.weather.dto;

import my.diary.weather.weather.Weather;

import java.time.LocalDate;

public record WeatherResponse(
        String main,
        String description,
        Double temperature,
        String iconCode,
        String location,
        LocalDate weatherDate
) {

    public static WeatherResponse from(Weather weather) {
        return new WeatherResponse(
                weather.getMain(),
                weather.getDescription(),
                weather.getTemperature().doubleValue(),
                weather.getIconCode(),
                weather.getLocation(),
                weather.getWeatherDate()
        );
    }
}
