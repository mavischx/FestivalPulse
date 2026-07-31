package eric.festivalPulse.controller;

import eric.festivalPulse.model.CrowdReport;
import eric.festivalPulse.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<CrowdReport> submitReport(@RequestBody CrowdReport report) {
        CrowdReport saved = reportService.submitReport(report);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<CrowdReport>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/area/{areaId}")
    public ResponseEntity<List<CrowdReport>> getReportsByArea(@PathVariable Long areaId) {
        return ResponseEntity.ok(reportService.getReportsByArea(areaId));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalidInput(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
