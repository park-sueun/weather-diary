package my.diary.weather.diary.dto;

import java.util.List;

public record CalendarResponse(
        int year,
        int month,
        List<DayInfo> days
) {

    public record DayInfo(
            String date,
            boolean hasDiary,
            String weather
    ) {

        public static DayInfo of(String date, boolean hasDiary, String weather) {
            return new DayInfo(date, hasDiary, weather);
        }
    }

    public static CalendarResponse of(int year, int month, List<DayInfo> days) {
        return new CalendarResponse(year, month, days);
    }

}
