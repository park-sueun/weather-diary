package my.diary.weather.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "weather")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String main;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, precision = 5, scale = 2) // Range: -99.99 ~ 999.99
    private BigDecimal temperature;

    @Column(name = "icon_code", length = 10)
    private String iconCode;

    @Builder
    private Weather(String main, String description, BigDecimal temperature, String iconCode) {
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
}
