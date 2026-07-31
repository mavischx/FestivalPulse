package eric.festivalPulse.controller;

import eric.festivalPulse.model.AlertStatus;
import eric.festivalPulse.model.CrowdAlert;
import eric.festivalPulse.service.AlertService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public ResponseEntity<List<CrowdAlert>> getAlerts(
            @RequestParam(defaultValue = "ACTIVE") AlertStatus status) {
        return ResponseEntity.ok(alertService.getAlertsByStatus(status));
    }

    @GetMapping("/area/{areaId}")
    public ResponseEntity<List<CrowdAlert>> getActiveAlertsByArea(@PathVariable Long areaId) {
        return ResponseEntity.ok(alertService.getActiveAlertsByArea(areaId));
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<CrowdAlert> resolveAlert(@PathVariable Long id) {
        CrowdAlert resolved = alertService.resolveAlert(id);
        return ResponseEntity.ok(resolved);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNotFound(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleConflict(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
