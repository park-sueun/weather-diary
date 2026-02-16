package my.diary.weather.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diary")
public class DiaryController {

    /**
     * openweathermap api key 환경설정 @Value 사용
     * openweathermap api 메소드 관리
     * openweathermap 데이터 파싱
     * json dependencies 추가
     * weather 데이터 DB에 저장
     */
    @PostMapping()
    void createDiary() {

    }
}
