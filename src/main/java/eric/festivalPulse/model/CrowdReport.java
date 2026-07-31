package eric.festivalPulse.model;

import java.time.LocalDateTime;

public class CrowdReport {
    private Long id;
    private Long areaId;
    private CrowdLevel crowdLevel;
    private String note;
    private LocalDateTime submittedAt;

    public CrowdReport() {}

    public CrowdReport(Long id, Long areaId, CrowdLevel crowdLevel, String note, LocalDateTime submittedAt) {
        this.id = id;
        this.areaId = areaId;
        this.crowdLevel = crowdLevel;
        this.note = note;
        this.submittedAt = submittedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAreaId() { return areaId; }
    public void setAreaId(Long areaId) { this.areaId = areaId; }

    public CrowdLevel getCrowdLevel() { return crowdLevel; }
    public void setCrowdLevel(CrowdLevel crowdLevel) { this.crowdLevel = crowdLevel; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}
