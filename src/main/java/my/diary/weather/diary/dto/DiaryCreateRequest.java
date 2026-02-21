package my.diary.weather.diary.dto;

import java.time.LocalDate;

public record DiaryCreateRequest(
        String content,
        String location,
        LocalDate diaryDate
) {
}
