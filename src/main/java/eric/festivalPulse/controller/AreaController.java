package eric.festivalPulse.controller;

import eric.festivalPulse.model.FestivalArea;
import eric.festivalPulse.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
@CrossOrigin
public class AreaController {

    @Autowired
    private AreaService areaService;

    @PostMapping
    public ResponseEntity<FestivalArea> createArea(@RequestBody FestivalArea area) {
        return ResponseEntity.status(HttpStatus.CREATED).body(areaService.createArea(area));
    }

    @GetMapping
    public ResponseEntity<List<FestivalArea>> getAllAreas() {
        return ResponseEntity.ok(areaService.getAllAreas());
    }
}
