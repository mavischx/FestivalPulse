package eric.festivalPulse.repository;

import eric.festivalPulse.model.CrowdReport;
import java.util.List;
import java.util.Optional;

public interface ReportRepository {
    CrowdReport save(CrowdReport report);
    List<CrowdReport> findAll();
    Optional<CrowdReport> findById(Long id);
    List<CrowdReport> findByAreaId(Long areaId);
}
