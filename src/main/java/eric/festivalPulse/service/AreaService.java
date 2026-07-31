package eric.festivalPulse.service;

import eric.festivalPulse.model.FestivalArea;
import java.util.List;

public interface AreaService {
    FestivalArea createArea(FestivalArea area);
    FestivalArea getAreaById(Long id);
    List<FestivalArea> getAllAreas();
}
