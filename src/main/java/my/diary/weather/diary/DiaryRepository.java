package my.diary.weather.diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    boolean existsByUser_IdAndDiaryDate(Long userId, LocalDate date);
    Optional<Diary> findByUser_IdAndDiaryDate(Long userId, LocalDate date);
    Optional<Diary> findByIdAndUserId(Long diaryId, Long userId);
}
