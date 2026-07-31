package eric.festivalPulse.service;

import eric.festivalPulse.model.AlertStatus;
import eric.festivalPulse.model.CrowdAlert;
import eric.festivalPulse.repository.AlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertServiceImplTest {

    @Mock
    private AlertRepository alertRepository;

    @InjectMocks
    private AlertServiceImpl alertService;

    private CrowdAlert testAlert;

    @BeforeEach
    void setUp() {
        testAlert = new CrowdAlert(null, 1L, "Area is FULL", null, null);
    }

    @Test
    void createAlert_noExistingAlert_savesSuccessfully() {
        when(alertRepository.findActiveByAreaId(1L)).thenReturn(Collections.emptyList());
        when(alertRepository.save(any(CrowdAlert.class))).thenAnswer(invocation -> {
            CrowdAlert a = invocation.getArgument(0);
            a.setId(1L);
            return a;
        });

        CrowdAlert result = alertService.createAlert(testAlert);

        assertNotNull(result.getId());
        assertEquals(AlertStatus.ACTIVE, result.getStatus());
        assertNotNull(result.getCreatedAt());
        verify(alertRepository).save(testAlert);
    }

    @Test
    void createAlert_duplicateExists_throwsIllegalStateException() {
        CrowdAlert existing = new CrowdAlert(1L, 1L, "Existing", AlertStatus.ACTIVE, LocalDateTime.now());
        when(alertRepository.findActiveByAreaId(1L)).thenReturn(List.of(existing));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> alertService.createAlert(testAlert));

        assertEquals("Active alert already exists for this area", ex.getMessage());
        verify(alertRepository, never()).save(any());
    }

    @Test
    void resolveAlert_existingActiveAlert_resolvesSuccessfully() {
        CrowdAlert activeAlert = new CrowdAlert(1L, 1L, "Area is FULL", AlertStatus.ACTIVE, LocalDateTime.now());
        when(alertRepository.findById(1L)).thenReturn(Optional.of(activeAlert));
        when(alertRepository.save(any(CrowdAlert.class))).thenReturn(activeAlert);

        CrowdAlert result = alertService.resolveAlert(1L);

        assertEquals(AlertStatus.RESOLVED, result.getStatus());
        verify(alertRepository).save(activeAlert);
    }

    @Test
    void resolveAlert_notFound_throwsIllegalArgumentException() {
        when(alertRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> alertService.resolveAlert(99L));

        assertEquals("Alert not found", ex.getMessage());
        verify(alertRepository, never()).save(any());
    }

    @Test
    void resolveAlert_alreadyResolved_throwsIllegalStateException() {
        CrowdAlert resolvedAlert = new CrowdAlert(1L, 1L, "Area is FULL", AlertStatus.RESOLVED, LocalDateTime.now());
        when(alertRepository.findById(1L)).thenReturn(Optional.of(resolvedAlert));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> alertService.resolveAlert(1L));

        assertEquals("Alert is already resolved", ex.getMessage());
        verify(alertRepository, never()).save(any());
    }

    @Test
    void getAlertsByStatus_delegatesToRepository() {
        CrowdAlert alert1 = new CrowdAlert(1L, 1L, "Alert 1", AlertStatus.ACTIVE, LocalDateTime.now());
        when(alertRepository.findByStatus(AlertStatus.ACTIVE)).thenReturn(List.of(alert1));

        List<CrowdAlert> result = alertService.getAlertsByStatus(AlertStatus.ACTIVE);

        assertEquals(1, result.size());
        verify(alertRepository).findByStatus(AlertStatus.ACTIVE);
    }

    @Test
    void getActiveAlertsByArea_delegatesToRepository() {
        CrowdAlert alert1 = new CrowdAlert(1L, 1L, "Alert 1", AlertStatus.ACTIVE, LocalDateTime.now());
        when(alertRepository.findActiveByAreaId(1L)).thenReturn(List.of(alert1));

        List<CrowdAlert> result = alertService.getActiveAlertsByArea(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getAreaId());
        verify(alertRepository).findActiveByAreaId(1L);
    }
}
