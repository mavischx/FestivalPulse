package eric.festivalPulse.repository;

import eric.festivalPulse.model.FestivalArea;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class AreaRepositoryImpl implements AreaRepository {

    private final Map<Long, FestivalArea> festivalAreaHashMap = new LinkedHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @Override
    public FestivalArea save(FestivalArea area) {
        if (area.getId() == null) area.setId(idGen.getAndIncrement());
        festivalAreaHashMap.put(area.getId(), area);
        return area;
    }

    @Override
    public Optional<FestivalArea> findById(Long id) {
        return Optional.ofNullable(festivalAreaHashMap.get(id));
    }

    @Override
    public List<FestivalArea> findAll() {
        return new ArrayList<>(festivalAreaHashMap.values());
    }
}
