package my.diary.weather.diary.dto;

import my.diary.weather.diary.Diary;
import my.diary.weather.weather.Weather;

import java.time.LocalDate;

public record DiaryResponse(
        Long id,
        LocalDate diaryDate,
        String content,
        WeatherSummary weather
) {

    public record WeatherSummary(
            String location,
            String main,
            String description,
            Double temperature,
            String iconCode
    ) {}

    public static DiaryResponse from (Diary diary) {
        WeatherSummary weather = null;

        if (diary.getWeather() != null) {
            Weather w = diary.getWeather();

            weather = new WeatherSummary(
                    w.getLocation(),
                    w.getMain(),
                    w.getDescription(),
                    w.getTemperature().doubleValue(),
                    w.getIconCode()
            );
        }

        return new DiaryResponse(
                diary.getId(),
                diary.getDiaryDate(),
                diary.getContent(),
                weather
        );
    }
}
