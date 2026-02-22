package my.diary.weather.diary.dto;

import my.diary.weather.diary.Diary;

import java.time.LocalDate;

public record CalendarDayResponse(
        LocalDate date,
        Long diaryId,
        String iconCode
) {
    public static CalendarDayResponse from(Diary diary) {

        return new CalendarDayResponse(
                diary.getDiaryDate(),
                diary.getId(),
                diary.getWeather().getIconCode()
        );
    }
}
