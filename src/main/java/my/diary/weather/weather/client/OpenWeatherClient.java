package my.diary.weather.weather.client;

import lombok.RequiredArgsConstructor;
import my.diary.weather.weather.dto.OpenWeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class OpenWeatherClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${openweather.api-key}")
    private String apikey;

    @Value("${openweather.base-url}")
    private String baseUrl;

    public OpenWeatherResponse getCurrentWeather(String city) {

        return webClientBuilder
                .baseUrl(baseUrl)
                .build()
                .get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("/data/2.5/weather")
                                .queryParam("q", city)
                                .queryParam("appid", apikey)
                                .queryParam("units", "metric")
                                .build())
                .retrieve()
                .bodyToMono(OpenWeatherResponse.class)
                .block();
    }

}