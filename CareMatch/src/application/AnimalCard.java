package application;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AnimalCard extends HBox {
    public AnimalCard(Animal animal) {
        this.getStyleClass().add("animal-card");
        this.setSpacing(20);

        // 이미지 자리 (더미 or 실제 이미지)
        ImageView imageView = new ImageView("file:resources/img_placeholder.png");
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setStyle("-fx-background-color: #ddd; -fx-border-radius: 8;");

        // 동물 정보 텍스트 영역
        VBox textBox = new VBox(5);
        textBox.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(animal.getName());
        nameLabel.getStyleClass().add("card-title");

        Label typeLabel = new Label("종: " + animal.getType());
        Label ageLabel = new Label("나이: " + animal.getAge() + "세");
        Label weightLabel = new Label("무게: " + animal.getWeight() + "kg");
        Label genderLabel = new Label("성별: " + animal.getGender());
        Label neuteredLabel = new Label("중성화: " + (animal.isNeutered() ? "O" : "X"));
        Label dateLabel = new Label("입소일: " + animal.getAdmissionDate());

        typeLabel.getStyleClass().add("card-info");
        ageLabel.getStyleClass().add("card-info");
        weightLabel.getStyleClass().add("card-info");
        genderLabel.getStyleClass().add("card-info");
        neuteredLabel.getStyleClass().add("card-info");
        dateLabel.getStyleClass().add("card-info");

        textBox.getChildren().addAll(
            nameLabel, typeLabel, ageLabel, weightLabel,
            genderLabel, neuteredLabel, dateLabel
        );

        // 우측 메뉴 버튼
        ComboBox<String> statusDropdown = new ComboBox<>();
        statusDropdown.getItems().addAll("치료중", "보호중", "종료(반환)", "종료(입양)");
        statusDropdown.setValue(animal.getStatus()); // 초기값 설정
        statusDropdown.setPromptText("상태");
        statusDropdown.getStyleClass().add("combo-box");

        statusDropdown.setOnAction(e -> {
            String selected = statusDropdown.getValue();
            animal.setStatus(selected); // 데이터 업데이트
        });
        
        VBox statusBox = new VBox(statusDropdown);
        statusBox.setAlignment(Pos.TOP_RIGHT);
        HBox.setHgrow(statusBox, Priority.ALWAYS);  

        this.getChildren().addAll(imageView, textBox, statusBox);
    }
}