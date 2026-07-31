package eric.festivalPulse.service;

import eric.festivalPulse.model.*;
import eric.festivalPulse.repository.AreaRepository;
import eric.festivalPulse.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private AreaRepository areaRepository;

    @Mock
    private AlertService alertService;

    @InjectMocks
    private ReportServiceImpl reportService;

    private FestivalArea testArea;
    private CrowdReport testReport;

    @BeforeEach
    void setUp() {
        testArea = new FestivalArea(1L, "Main Stage", "The main stage", "STAGE");
        testReport = new CrowdReport();
        testReport.setAreaId(1L);
        testReport.setCrowdLevel(CrowdLevel.MEDIUM);
        testReport.setNote("Getting busy");
    }

    @Test
    void submitReport_validArea_savesReport() {
        when(areaRepository.findById(1L)).thenReturn(Optional.of(testArea));
        when(reportRepository.save(any(CrowdReport.class))).thenAnswer(invocation -> {
            CrowdReport r = invocation.getArgument(0);
            r.setId(1L);
            return r;
        });

        CrowdReport result = reportService.submitReport(testReport);

        assertNotNull(result.getId());
        assertNotNull(result.getSubmittedAt());
        assertEquals(CrowdLevel.MEDIUM, result.getCrowdLevel());
        verify(reportRepository).save(testReport);
    }

    @Test
    void submitReport_missingArea_throwsException() {
        when(areaRepository.findById(99L)).thenReturn(Optional.empty());
        testReport.setAreaId(99L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> reportService.submitReport(testReport));

        assertEquals("Area not found", ex.getMessage());
        verify(reportRepository, never()).save(any());
    }

    @Test
    void submitReport_fullLevel_createsAlert() {
        testReport.setCrowdLevel(CrowdLevel.FULL);
        when(areaRepository.findById(1L)).thenReturn(Optional.of(testArea));
        when(reportRepository.save(any(CrowdReport.class))).thenAnswer(invocation -> {
            CrowdReport r = invocation.getArgument(0);
            r.setId(1L);
            return r;
        });

        reportService.submitReport(testReport);

        ArgumentCaptor<CrowdAlert> alertCaptor = ArgumentCaptor.forClass(CrowdAlert.class);
        verify(alertService).createAlert(alertCaptor.capture());
        CrowdAlert createdAlert = alertCaptor.getValue();
        assertEquals(1L, createdAlert.getAreaId());
        assertEquals(AlertStatus.ACTIVE, createdAlert.getStatus());
    }

    @Test
    void submitReport_lowLevel_doesNotCreateAlert() {
        testReport.setCrowdLevel(CrowdLevel.LOW);
        when(areaRepository.findById(1L)).thenReturn(Optional.of(testArea));
        when(reportRepository.save(any(CrowdReport.class))).thenReturn(testReport);

        reportService.submitReport(testReport);

        verify(alertService, never()).createAlert(any());
    }

    @Test
    void getAllReports_returnsAll() {
        CrowdReport report1 = new CrowdReport(1L, 1L, CrowdLevel.LOW, "Quiet", null);
        CrowdReport report2 = new CrowdReport(2L, 1L, CrowdLevel.MEDIUM, "Busy", null);
        when(reportRepository.findAll()).thenReturn(List.of(report1, report2));

        List<CrowdReport> results = reportService.getAllReports();

        assertEquals(2, results.size());
    }

    @Test
    void getReportsByArea_returnsFiltered() {
        CrowdReport report1 = new CrowdReport(1L, 1L, CrowdLevel.LOW, "Quiet", null);
        when(reportRepository.findByAreaId(1L)).thenReturn(List.of(report1));

        List<CrowdReport> results = reportService.getReportsByArea(1L);

        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getAreaId());
    }
}
