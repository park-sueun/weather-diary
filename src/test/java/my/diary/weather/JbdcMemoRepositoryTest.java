package my.diary.weather;

import my.diary.weather.model.Memo;
import my.diary.weather.repository.JdbcMemoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
//@Transactional
public class JbdcMemoRepositoryTest {

    @Autowired
    JdbcMemoRepository jdbcMemoRepository;

    @Test
    void insertMemoTest() {

        // given
        Memo newMeno = new Memo(2, "this is old memo~");

        // when
        jdbcMemoRepository.save(newMeno);

        // then
        Optional<Memo> result = Optional.ofNullable(jdbcMemoRepository.findById(2));
        assertEquals(result.get().getText(), "this is old memo~");
    }

    @Test
    void findAllMemoTest() {
        List<Memo> memoList = jdbcMemoRepository.findAll();
        System.out.println(memoList);
        assertNotNull(memoList);
    }
}
