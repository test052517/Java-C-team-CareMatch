package DTO;

public class FavAnimalDTO {
    private int favId;
    private String animalId;
    private String userId;

    public FavAnimalDTO() {}

    public FavAnimalDTO(int favId, String animalId, String userId) {
        this.favId = favId;
        this.animalId = animalId;
        this.userId = userId;
    }

    public int getFavId() { return favId; }
    public void setFavId(int favId) { this.favId = favId; }
    public String getAnimalId() { return animalId; }
    public void setAnimalId(String animalId) { this.animalId = animalId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "FavAnimalDTO{" +
                "favId=" + favId +
                ", animalId='" + animalId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}