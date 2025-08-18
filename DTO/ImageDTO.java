package DTO;

import java.util.Arrays;

public class ImageDTO {
    private String imageId;
    private String imageUrl;
    private byte[] imageData;


    public ImageDTO() {}

    public ImageDTO(String imageId, String imageUrl, byte[] imageData) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.imageData = imageData;
    }

    public String getImageId() { return imageId; }
    public void setImageId(String imageId) { this.imageId = imageId; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public byte[] getImageData() { return imageData; }
    public void setImageData(byte[] imageData) { this.imageData = imageData; }


    @Override
    public String toString() {
        return "ImageDTO{" +
                "imageId='" + imageId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", imageData length=" + (imageData != null ? imageData.length : 0) +
                '}';
    }
}