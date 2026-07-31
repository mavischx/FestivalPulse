package eric.festivalPulse.repository;

import eric.festivalPulse.model.AlertStatus;
import eric.festivalPulse.model.CrowdAlert;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class AlertRepositoryImpl implements AlertRepository {

    private final Map<Long, CrowdAlert> store = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public CrowdAlert save(CrowdAlert alert) {
        if (alert.getId() == null) {
            alert.setId(idCounter.getAndIncrement());
        }
        store.put(alert.getId(), alert);
        return alert;
    }

    @Override
    public Optional<CrowdAlert> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<CrowdAlert> findByStatus(AlertStatus status) {
        return store.values().stream()
                .filter(alert -> alert.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<CrowdAlert> findActiveByAreaId(Long areaId) {
        return store.values().stream()
                .filter(alert -> alert.getAreaId().equals(areaId) && alert.getStatus() == AlertStatus.ACTIVE)
                .collect(Collectors.toList());
    }
}
