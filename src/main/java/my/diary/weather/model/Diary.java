package my.diary.weather.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import javax.print.attribute.standard.Media;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate diaryDate;

    private String content;

    private String moodEmoji; // 😊 😢 😡 ☀️

    @OneToOne(cascade = CascadeType.ALL)
    private Weather weather;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
    private List<Media> mediaList = new ArrayList<>();
}
