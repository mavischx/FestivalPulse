package eric.festivalPulse.repository;

import eric.festivalPulse.model.FestivalArea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AreaRepositoryImplTest {

    private AreaRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new AreaRepositoryImpl();
    }

    @Test
    void save_assignsId() {
        FestivalArea area = new FestivalArea(null, "Main Stage", "The main stage", "STAGE");

        FestivalArea saved = repository.save(area);

        assertNotNull(saved.getId());
        assertEquals(1L, saved.getId());
    }

    @Test
    void save_multipleAreas_incrementsId() {
        FestivalArea area1 = new FestivalArea(null, "Main Stage", "The main stage", "STAGE");
        FestivalArea area2 = new FestivalArea(null, "Food Court", "Food area", "FOOD");

        repository.save(area1);
        repository.save(area2);

        assertEquals(1L, area1.getId());
        assertEquals(2L, area2.getId());
    }

    @Test
    void findById_existing_returnsArea() {
        FestivalArea saved = repository.save(new FestivalArea(null, "Main Stage", "The main stage", "STAGE"));

        Optional<FestivalArea> found = repository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Main Stage", found.get().getName());
    }

    @Test
    void findById_nonExisting_returnsEmpty() {
        Optional<FestivalArea> found = repository.findById(999L);

        assertTrue(found.isEmpty());
    }

    @Test
    void findAll_returnsAllSaved() {
        repository.save(new FestivalArea(null, "Main Stage", "The main stage", "STAGE"));
        repository.save(new FestivalArea(null, "Food Court", "Food area", "FOOD"));

        List<FestivalArea> all = repository.findAll();

        assertEquals(2, all.size());
    }

    @Test
    void findAll_empty_returnsEmptyList() {
        List<FestivalArea> all = repository.findAll();

        assertTrue(all.isEmpty());
    }
}
