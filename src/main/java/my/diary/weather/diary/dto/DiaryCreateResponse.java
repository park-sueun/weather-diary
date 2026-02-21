package my.diary.weather.diary.dto;

import my.diary.weather.diary.Diary;
import my.diary.weather.weather.Weather;

import java.time.LocalDate;

public record DiaryCreateResponse(
        Long id,
        LocalDate diaryDate,
        String content,
        WeatherSummary weather
) {
    public record WeatherSummary(
            Long id,
            String location,
            String main,
            String description,
            Double temperature,
            String iconCode
    ) {
    }

    public static DiaryCreateResponse from(Diary diary) {

        WeatherSummary weatherSummary = null;

        if (diary.getWeather() != null) {
            Weather w = diary.getWeather();

            weatherSummary = new WeatherSummary(
                    w.getId(),
                    w.getLocation(),
                    w.getMain(),
                    w.getDescription(),
                    w.getTemperature().doubleValue(),
                    w.getIconCode()
            );
        }

        return new DiaryCreateResponse(
                diary.getId(),
                diary.getDiaryDate(),
                diary.getContent(),
                weatherSummary
        );
    }
}
