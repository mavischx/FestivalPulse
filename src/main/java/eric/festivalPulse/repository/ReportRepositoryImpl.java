package eric.festivalPulse.repository;

import eric.festivalPulse.model.CrowdReport;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class ReportRepositoryImpl implements ReportRepository {

    private final Map<Long, CrowdReport> store = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public CrowdReport save(CrowdReport report) {
        if (report.getId() == null) {
            report.setId(idCounter.getAndIncrement());
        }
        store.put(report.getId(), report);
        return report;
    }

    @Override
    public List<CrowdReport> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<CrowdReport> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<CrowdReport> findByAreaId(Long areaId) {
        return store.values().stream()
                .filter(report -> report.getAreaId().equals(areaId))
                .collect(Collectors.toList());
    }
}
