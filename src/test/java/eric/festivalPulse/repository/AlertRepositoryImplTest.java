package eric.festivalPulse.repository;

import eric.festivalPulse.model.AlertStatus;
import eric.festivalPulse.model.CrowdAlert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AlertRepositoryImplTest {

    private AlertRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new AlertRepositoryImpl();
    }

    @Test
    void save_assignsId() {
        CrowdAlert alert = new CrowdAlert(null, 1L, "Area is FULL", AlertStatus.ACTIVE, LocalDateTime.now());

        CrowdAlert saved = repository.save(alert);

        assertNotNull(saved.getId());
        assertEquals(1L, saved.getId());
    }

    @Test
    void save_multipleAlerts_incrementsId() {
        CrowdAlert alert1 = new CrowdAlert(null, 1L, "Alert 1", AlertStatus.ACTIVE, LocalDateTime.now());
        CrowdAlert alert2 = new CrowdAlert(null, 2L, "Alert 2", AlertStatus.ACTIVE, LocalDateTime.now());

        repository.save(alert1);
        repository.save(alert2);

        assertEquals(1L, alert1.getId());
        assertEquals(2L, alert2.getId());
    }

    @Test
    void findById_existing_returnsAlert() {
        CrowdAlert saved = repository.save(
                new CrowdAlert(null, 1L, "Area is FULL", AlertStatus.ACTIVE, LocalDateTime.now()));

        Optional<CrowdAlert> found = repository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Area is FULL", found.get().getMessage());
    }

    @Test
    void findById_nonExisting_returnsEmpty() {
        Optional<CrowdAlert> found = repository.findById(999L);

        assertTrue(found.isEmpty());
    }

    @Test
    void findByStatus_filtersCorrectly() {
        repository.save(new CrowdAlert(null, 1L, "Active 1", AlertStatus.ACTIVE, LocalDateTime.now()));
        repository.save(new CrowdAlert(null, 2L, "Active 2", AlertStatus.ACTIVE, LocalDateTime.now()));
        repository.save(new CrowdAlert(null, 3L, "Resolved", AlertStatus.RESOLVED, LocalDateTime.now()));

        List<CrowdAlert> activeAlerts = repository.findByStatus(AlertStatus.ACTIVE);
        List<CrowdAlert> resolvedAlerts = repository.findByStatus(AlertStatus.RESOLVED);

        assertEquals(2, activeAlerts.size());
        assertEquals(1, resolvedAlerts.size());
    }

    @Test
    void findActiveByAreaId_filtersByAreaAndStatus() {
        repository.save(new CrowdAlert(null, 1L, "Area 1 active", AlertStatus.ACTIVE, LocalDateTime.now()));
        repository.save(new CrowdAlert(null, 1L, "Area 1 resolved", AlertStatus.RESOLVED, LocalDateTime.now()));
        repository.save(new CrowdAlert(null, 2L, "Area 2 active", AlertStatus.ACTIVE, LocalDateTime.now()));

        List<CrowdAlert> result = repository.findActiveByAreaId(1L);

        assertEquals(1, result.size());
        assertEquals("Area 1 active", result.get(0).getMessage());
        assertEquals(AlertStatus.ACTIVE, result.get(0).getStatus());
        assertEquals(1L, result.get(0).getAreaId());
    }
}
