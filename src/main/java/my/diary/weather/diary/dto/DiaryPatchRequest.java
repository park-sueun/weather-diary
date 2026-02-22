package my.diary.weather.diary.dto;

public record DiaryPatchRequest(
        String content,
        String location
) {
}
