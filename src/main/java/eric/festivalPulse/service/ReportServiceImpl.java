package eric.festivalPulse.service;

import eric.festivalPulse.model.CrowdLevel;
import eric.festivalPulse.model.CrowdReport;
import eric.festivalPulse.repository.AreaRepository;
import eric.festivalPulse.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final AreaRepository areaRepository;
    private final AlertService alertService;

    public ReportServiceImpl(ReportRepository reportRepository, AreaRepository areaRepository, AlertService alertService) {
        this.reportRepository = reportRepository;
        this.areaRepository = areaRepository;
        this.alertService = alertService;
    }

    @Override
    public CrowdReport submitReport(CrowdReport report) {
        // Validate that the area exists
        areaRepository.findById(report.getAreaId())
                .orElseThrow(() -> new IllegalArgumentException("Area not found"));

        // Save the report
        report.setSubmittedAt(LocalDateTime.now());
        CrowdReport saved = reportRepository.save(report);

        // Trigger alert if FULL
        if (report.getCrowdLevel() == CrowdLevel.FULL) {
            alertService.createAlert(new eric.festivalPulse.model.CrowdAlert(
                    null,
                    report.getAreaId(),
                    "Area is FULL",
                    eric.festivalPulse.model.AlertStatus.ACTIVE,
                    LocalDateTime.now()
            ));
        }

        return saved;
    }

    @Override
    public List<CrowdReport> getAllReports() {
        return reportRepository.findAll();
    }

    @Override
    public List<CrowdReport> getReportsByArea(Long areaId) {
        return reportRepository.findByAreaId(areaId);
    }
}
