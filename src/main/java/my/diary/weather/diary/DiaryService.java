package my.diary.weather.diary;

import lombok.RequiredArgsConstructor;
import my.diary.weather.diary.dto.DiaryCreateRequest;
import my.diary.weather.diary.dto.DiaryCreateResponse;
import my.diary.weather.user.AppUser;
import my.diary.weather.user.AppUserRepository;
import my.diary.weather.weather.Weather;
import my.diary.weather.weather.WeatherService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final WeatherService weatherService;
    private final AppUserRepository userRepository;

    public DiaryCreateResponse create(Long userId, DiaryCreateRequest req) {

        LocalDate date = req.diaryDate();

        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Future diary is not supported");
        }

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        // pre-check 1 dairy for 1 day
        if (diaryRepository.existsByUser_IdAndDiaryDate(userId, date)) {
            throw new IllegalStateException("Diary already exists for this date");
        }

        Weather weather = null;

        // Connect weather if req has location field
        if (req.location() != null && !req.location().isBlank()) {
            weather = weatherService.getOrFetchWeather(date, req.location());
        }

        Diary diary = Diary.of(user, date, req.content(), weather);

        try {
            Diary saved = diaryRepository.save(diary);
            return DiaryCreateResponse.from(saved);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Diary already exists for this date", e);
        }
    }
}
