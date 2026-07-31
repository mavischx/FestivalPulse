package eric.festivalPulse.model;

public class FestivalArea {
    private Long id;
    private String name;
    private String description;
    private String areaType;

    public FestivalArea() {}

    public FestivalArea(Long id, String name, String description, String areaType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.areaType = areaType;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAreaType() { return areaType; }
    public void setAreaType(String areaType) { this.areaType = areaType; }
}
