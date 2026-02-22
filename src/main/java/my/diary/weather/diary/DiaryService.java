package my.diary.weather.diary;

import lombok.RequiredArgsConstructor;
import my.diary.weather.diary.dto.*;
import my.diary.weather.user.AppUser;
import my.diary.weather.user.AppUserRepository;
import my.diary.weather.weather.Weather;
import my.diary.weather.weather.WeatherService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final WeatherService weatherService;
    private final AppUserRepository userRepository;

    @Transactional
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

    public DiaryResponse get(Long userId, Long diaryId) {

        Diary diary = diaryRepository
                .findByIdAndUserId(diaryId, userId)
                .orElseThrow(() -> new IllegalStateException("Diary not found"));

        return DiaryResponse.from(diary);
    }

    @Transactional
    public DiaryResponse patch(Long userId,
                                Long diaryId,
                                DiaryPatchRequest req) {

        Diary diary = diaryRepository
                .findByIdAndUserId(diaryId, userId)
                .orElseThrow(() -> new IllegalStateException("Diary not found"));

        // update content
        if (req.content() != null) {
            diary.updateContent(req.content());
        }

        // update weather
        if (req.location() != null) {

            if (!diary.getDiaryDate().equals(LocalDate.now())) {
                throw new IllegalStateException("Weather can only be updated for today's diary");
            }

            Weather weather = weatherService.getOrFetchWeather(
                    diary.getDiaryDate(),
                    req.location()
            );

            diary.updateWeather(weather);
        }

        return DiaryResponse.from(diary);
    }

    @Transactional
    public void delete(Long userId, Long diaryId) {

        Diary diary = diaryRepository
                .findByIdAndUserId(diaryId, userId)
                .orElseThrow(() -> new IllegalStateException("Diary not found"));

        diaryRepository.delete(diary);
    }

    public CalendarResponse getCalendar(
            Long userId,
            int year,
            int month
    ) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<Diary> diaries =
                diaryRepository.findAllByUserIdAndDiaryDateBetween(userId, start, end);

        List<CalendarDayResponse> days = diaries.stream()
                .map(CalendarDayResponse::from)
                .toList();

        return new CalendarResponse(
                year, month, days
        );
    }

    public DiaryResponse getByDiaryDate(Long userId, LocalDate date) {
        Diary diary = diaryRepository
                .findByUser_IdAndDiaryDate(userId, date)
                .orElseThrow(() -> new IllegalStateException("Diary not found"));

        return DiaryResponse.from(diary);
    }
}
