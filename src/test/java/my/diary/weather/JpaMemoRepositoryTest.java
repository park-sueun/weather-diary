package my.diary.weather;

import jakarta.transaction.Transactional;
import my.diary.weather.model.Memo;
import my.diary.weather.repository.JpaMemoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class JpaMemoRepositoryTest {

    @Autowired
    JpaMemoRepository jpaMemoRepository;

    @Test
    void insertMemoTest() {
        // given
        Memo newMemo = new Memo("This is jpa memo");

        // when
        jpaMemoRepository.save(newMemo);

        // then
        List<Memo> memoList = jpaMemoRepository.findAll();
        assertTrue(memoList.size() > 0);
    }

    @Test
    void findByIdTest() {
        // given
        Memo newMemo = new Memo("jpa");

        // when
        Memo memo = jpaMemoRepository.save(newMemo);
        System.out.println(memo.getId() + ": " + memo.getText());

        // then
        Optional<Memo> result = jpaMemoRepository.findById(memo.getId());
        assertEquals(result.get().getText(), "jpa");
    }
}
