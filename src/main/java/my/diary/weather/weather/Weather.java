package my.diary.weather.weather;

import jakarta.persistence.*;
import lombok.*;
import my.diary.weather.weather.dto.OpenWeatherResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "weather")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String location;

    @Column(nullable = false)
    private LocalDate weatherDate;

    @Column(nullable = false, length = 50)
    private String main;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, precision = 5, scale = 2) // Range: -99.99 ~ 999.99
    private BigDecimal temperature;

    @Column(name = "icon_code", length = 10)
    private String iconCode;

    @Builder
    private Weather(LocalDate weatherDate, String location, String main, String description, BigDecimal temperature, String iconCode) {
        this.weatherDate = weatherDate;
        this.location = location;
        this.main = main;
        this.description = description;
        this.temperature = temperature;
        this.iconCode = iconCode;
    }

    public String toEmoji() {
        return switch (main) {
            case "Clear" -> "☀️";
            case "Rain" -> "🌧️";
            case "Clouds" -> "☁️";
            case "Snow" -> "❄️";
            default -> "🌈";
        };
    }

    public static Weather of(
            LocalDate date,
            String location,
            OpenWeatherResponse res
    ) {
        if (res.getWeather() == null || res.getWeather().isEmpty()) {
            throw new IllegalStateException("Weather data missing");
        }

        OpenWeatherResponse.WeatherInfo w = res.getWeather().get(0);

        return new Weather(
                date,
                location,
                w.getMain(),
                w.getDescription(),
                BigDecimal.valueOf(res.getMain().getTemp()),
                w.getIcon()
        );
    }
}
