package my.diary.weather.media;

import jakarta.persistence.*;
import lombok.*;
import my.diary.weather.diary.Diary;
import my.diary.weather.global.entity.BaseTimeEntity;

@Entity
@Table(name = "media")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Media extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // N:1
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "diary_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_media_diary"))
    private Diary diary;

    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", nullable = false, length = 20)
    private MediaType mediaType;

    @Builder
    private Media(String fileUrl, MediaType mediaType) {
        this.fileUrl = fileUrl;
        this.mediaType = mediaType;
    }

    public void attachTo(Diary diary) {
        this.diary = diary;
    }
}

enum MediaType {
    IMAGE, VIDEO
}