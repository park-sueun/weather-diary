package my.diary.weather.diary.dto;

import java.util.List;

public record CalendarResponse(
        int year,
        int month,
        List<CalendarDayResponse> days
) {
}
