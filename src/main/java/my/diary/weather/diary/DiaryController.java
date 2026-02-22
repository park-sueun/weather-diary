package my.diary.weather.diary;

import lombok.RequiredArgsConstructor;
import my.diary.weather.diary.dto.*;
import my.diary.weather.global.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public DiaryCreateResponse create(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody DiaryCreateRequest req
    ) {
        return diaryService.create(user.getUserId(), req);
    }

    @GetMapping("/{date}")
    public DiaryResponse getByDate(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable LocalDate date
    ) {
        return diaryService.getByDiaryDate(user.getUserId(), date);
    }


    @PatchMapping("/{id}")
    public DiaryResponse patch(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id,
            @RequestBody DiaryPatchRequest req
    ) {
        return diaryService.patch(user.getUserId(), id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id
    ) {
        diaryService.delete(user.getUserId(), id);
    }

    @GetMapping("/calendar")
    public CalendarResponse getCalendar(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return diaryService.getCalendar(
                user.getUserId(),
                year,
                month
        );
    }

}
