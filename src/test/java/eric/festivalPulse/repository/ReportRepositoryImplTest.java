package eric.festivalPulse.repository;

import eric.festivalPulse.model.CrowdLevel;
import eric.festivalPulse.model.CrowdReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ReportRepositoryImplTest {

    private ReportRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new ReportRepositoryImpl();
    }

    @Test
    void save_assignsId() {
        CrowdReport report = new CrowdReport(null, 1L, CrowdLevel.LOW, "Quiet", LocalDateTime.now());

        CrowdReport saved = repository.save(report);

        assertNotNull(saved.getId());
        assertEquals(1L, saved.getId());
    }

    @Test
    void save_multipleReports_incrementsId() {
        CrowdReport report1 = new CrowdReport(null, 1L, CrowdLevel.LOW, "Quiet", LocalDateTime.now());
        CrowdReport report2 = new CrowdReport(null, 1L, CrowdLevel.MEDIUM, "Busy", LocalDateTime.now());

        repository.save(report1);
        repository.save(report2);

        assertEquals(1L, report1.getId());
        assertEquals(2L, report2.getId());
    }

    @Test
    void findAll_returnsAllSaved() {
        repository.save(new CrowdReport(null, 1L, CrowdLevel.LOW, "A", LocalDateTime.now()));
        repository.save(new CrowdReport(null, 2L, CrowdLevel.FULL, "B", LocalDateTime.now()));

        List<CrowdReport> all = repository.findAll();

        assertEquals(2, all.size());
    }

    @Test
    void findById_existing_returnsReport() {
        CrowdReport saved = repository.save(new CrowdReport(null, 1L, CrowdLevel.LOW, "A", LocalDateTime.now()));

        Optional<CrowdReport> found = repository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("A", found.get().getNote());
    }

    @Test
    void findById_nonExisting_returnsEmpty() {
        Optional<CrowdReport> found = repository.findById(999L);

        assertTrue(found.isEmpty());
    }

    @Test
    void findByAreaId_filtersCorrectly() {
        repository.save(new CrowdReport(null, 1L, CrowdLevel.LOW, "A", LocalDateTime.now()));
        repository.save(new CrowdReport(null, 2L, CrowdLevel.MEDIUM, "B", LocalDateTime.now()));
        repository.save(new CrowdReport(null, 1L, CrowdLevel.FULL, "C", LocalDateTime.now()));

        List<CrowdReport> area1Reports = repository.findByAreaId(1L);

        assertEquals(2, area1Reports.size());
        assertTrue(area1Reports.stream().allMatch(r -> r.getAreaId().equals(1L)));
    }
}
