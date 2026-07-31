package eric.festivalPulse.service;

import eric.festivalPulse.model.AlertStatus;
import eric.festivalPulse.model.CrowdAlert;
import java.util.List;

public interface AlertService {
    CrowdAlert createAlert(CrowdAlert alert);
    CrowdAlert resolveAlert(Long id);
    List<CrowdAlert> getAlertsByStatus(AlertStatus status);
    List<CrowdAlert> getActiveAlertsByArea(Long areaId);
}
