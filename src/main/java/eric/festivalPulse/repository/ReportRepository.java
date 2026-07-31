package eric.festivalPulse.repository;

import eric.festivalPulse.model.CrowdReport;
import java.util.List;

public interface ReportRepository {
    CrowdReport save(CrowdReport report);
    List<CrowdReport> findAll();
}
