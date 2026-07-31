package eric.festivalPulse.service;

import eric.festivalPulse.model.AlertStatus;
import eric.festivalPulse.model.CrowdAlert;
import eric.festivalPulse.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;

    public AlertServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public CrowdAlert createAlert(CrowdAlert alert) {
        // Reject duplicate active alerts for the same area
        List<CrowdAlert> existingAlerts = alertRepository.findActiveByAreaId(alert.getAreaId());
        if (!existingAlerts.isEmpty()) {
            throw new IllegalStateException("Active alert already exists for this area");
        }

        // Set status and timestamp
        alert.setStatus(AlertStatus.ACTIVE);
        alert.setCreatedAt(LocalDateTime.now());
        return alertRepository.save(alert);
    }

    @Override
    public CrowdAlert resolveAlert(Long id) {
        CrowdAlert alert = alertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found"));

        if (alert.getStatus() == AlertStatus.RESOLVED) {
            throw new IllegalStateException("Alert is already resolved");
        }

        alert.setStatus(AlertStatus.RESOLVED);
        return alertRepository.save(alert);
    }

    @Override
    public List<CrowdAlert> getAlertsByStatus(AlertStatus status) {
        return alertRepository.findByStatus(status);
    }

    @Override
    public List<CrowdAlert> getActiveAlertsByArea(Long areaId) {
        return alertRepository.findActiveByAreaId(areaId);
    }
}
