package my.diary.weather.diary;

import lombok.RequiredArgsConstructor;
import my.diary.weather.diary.dto.DiaryCreateRequest;
import my.diary.weather.diary.dto.DiaryCreateResponse;
import my.diary.weather.diary.dto.DiaryResponse;
import my.diary.weather.diary.dto.DiaryPatchRequest;
import my.diary.weather.global.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public DiaryResponse get(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id
    ) {
        return diaryService.get(user.getUserId(), id);
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

}
