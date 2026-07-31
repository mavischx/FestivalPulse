package eric.festivalPulse.service;

import eric.festivalPulse.model.FestivalArea;
import eric.festivalPulse.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaRepository areaRepository;

    @Override
    public FestivalArea createArea(FestivalArea area) {
        return areaRepository.save(area);
    }

    @Override
    public FestivalArea getAreaById(Long id) {
        return areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Area not found: " + id));
    }

    @Override
    public List<FestivalArea> getAllAreas() {
        return areaRepository.findAll();
    }
}
