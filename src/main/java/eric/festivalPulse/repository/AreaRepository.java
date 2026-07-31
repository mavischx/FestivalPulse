package eric.festivalPulse.repository;

import eric.festivalPulse.model.FestivalArea;
import java.util.List;
import java.util.Optional;

public interface AreaRepository {
    FestivalArea save(FestivalArea area);
    Optional<FestivalArea> findById(Long id);
    List<FestivalArea> findAll();
}
