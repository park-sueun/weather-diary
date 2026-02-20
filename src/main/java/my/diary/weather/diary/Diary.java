package my.diary.weather.diary;

import jakarta.persistence.*;
import lombok.*;
import my.diary.weather.global.entity.BaseTimeEntity;
import my.diary.weather.user.AppUser;
import my.diary.weather.media.Media;
import my.diary.weather.weather.Weather;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "diary",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_user_date", columnNames = {"user_id", "diary_date"})
        },
        indexes = {
                @Index(name = "idx_diary_user_date", columnList = "user_id, diary_date")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // N:1
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_diary_user"))
    private AppUser user;

    @Column(name = "diary_date", nullable = false)
    private LocalDate diaryDate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 1:1 (Diary가 소유)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "weather_id", nullable = true,
            foreignKey = @ForeignKey(name = "fk_diary_weather"))
    private Weather weather;

    // 1:N
    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Media> mediaList = new ArrayList<>();

    @Builder
    private Diary(AppUser user, LocalDate diaryDate, String location, String content, Weather weather) {
        this.user = user;
        this.diaryDate = diaryDate;
        this.content = content;
        this.weather = weather;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void addMedia(Media media) {
        media.attachTo(this);
        this.mediaList.add(media);
    }
}