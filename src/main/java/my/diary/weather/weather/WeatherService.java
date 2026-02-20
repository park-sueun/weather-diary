package my.diary.weather.weather;

import lombok.RequiredArgsConstructor;
import my.diary.weather.weather.client.OpenWeatherClient;
import my.diary.weather.weather.dto.OpenWeatherResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final OpenWeatherClient openWeatherClient;

    @Transactional
    public Weather getOrFetchWeather(LocalDate date, String city) {

        String normalizedCity = normalizeCity(city);
        LocalDate today = LocalDate.now();

        // 1. 미래 날짜 방어
        if (date.isAfter(today)) {
            throw new IllegalArgumentException("Future weather is not supported");
        }

        // 2. DB 먼저 조회
        return weatherRepository
                .findByWeatherDateAndLocation(date, normalizedCity)
                .orElseGet(() -> {
                    // 3. 과거 날짜인데 없으면 실패
                    if (date.isBefore(today)) {
                        throw new IllegalStateException(
                                "Past weather not cached. Cannot fetch from OpenWeather."
                        );
                    }

                    // 4. 오늘 날짜만 외부 호출
                    return fetchAndSave(date, normalizedCity);
                });
    }

    private Weather fetchAndSave(LocalDate date, String normalizedCity) {

        try {
            OpenWeatherResponse res =
                    openWeatherClient.getCurrentWeather(normalizedCity);

            Weather weather = Weather.of(date, normalizedCity, res);

            return weatherRepository.save(weather);

        } catch (DataIntegrityViolationException e) {
            // ⭐ 동시성 안전 처리 (핵심)
            return weatherRepository
                    .findByWeatherDateAndLocation(date, normalizedCity)
                    .orElseThrow(() -> e);
        }
    }

    private String normalizeCity(String city) {
        return city.trim().toLowerCase();
    }


}
