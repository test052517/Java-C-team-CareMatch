package DTO;

public class SpeciesDTO {
    private String kindId;
    private String kindName;
    private String typeName;
    private String category;

    public SpeciesDTO() {}

    public SpeciesDTO(String kindId, String kindName, String typeName, String category) {
        this.kindId = kindId;
        this.kindName = kindName;
        this.typeName = typeName;
        this.category = category;
    }

    public String getKindId() { return kindId; }
    public void setKindId(String kindId) { this.kindId = kindId; }
    public String getKindName() { return kindName; }
    public void setKindName(String kindName) { this.kindName = kindName; }
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String toString() {
        return "SpeciesDTO{" +
                "kindId='" + kindId + '\'' +
                ", kindName='" + kindName + '\'' +
                ", typeName='" + typeName + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}