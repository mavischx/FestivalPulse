package eric.festivalPulse.repository;

import eric.festivalPulse.model.AlertStatus;
import eric.festivalPulse.model.CrowdAlert;
import java.util.List;
import java.util.Optional;

public interface AlertRepository {
    CrowdAlert save(CrowdAlert alert);
    Optional<CrowdAlert> findById(Long id);
    List<CrowdAlert> findByStatus(AlertStatus status);
    List<CrowdAlert> findActiveByAreaId(Long areaId);
}
