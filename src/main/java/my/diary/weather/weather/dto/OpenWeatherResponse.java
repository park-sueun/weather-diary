package my.diary.weather.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherResponse {

    private List<WeatherInfo> weather;
    private MainInfo main;
    private String name; // city name
    private Long dt;     // unix timestamp

    // ===== inner class =====

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherInfo {
        private String main;
        private String description;
        private String icon;
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MainInfo {
        private Double temp;
    }
}
