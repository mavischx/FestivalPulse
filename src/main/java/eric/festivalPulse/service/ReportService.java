package eric.festivalPulse.service;

import eric.festivalPulse.model.CrowdReport;
import java.util.List;

public interface ReportService {
    CrowdReport submitReport(CrowdReport report);
    List<CrowdReport> getAllReports();
    List<CrowdReport> getReportsByArea(Long areaId);
}
