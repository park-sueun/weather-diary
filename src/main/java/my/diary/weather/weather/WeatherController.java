package my.diary.weather.weather;

import lombok.RequiredArgsConstructor;
import my.diary.weather.weather.dto.WeatherResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public WeatherResponse getWeather(
            @RequestParam LocalDate date,
            @RequestParam String city
    ) {
        Weather weather =
                weatherService.getOrFetchWeather(date, city);

        return WeatherResponse.from(weather);
    }
}
