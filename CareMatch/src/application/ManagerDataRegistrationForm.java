package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.stage.Modality;

public class ManagerDataRegistrationForm {

    public void show(Stage stage) {
    	Font.loadFont(getClass().getResource("/Pretendard-Medium.ttf").toExternalForm(), 12);
		Font.loadFont(getClass().getResource("/Pretendard-Bold.ttf").toExternalForm(), 12);
		Font.loadFont(getClass().getResource("/Pretendard-ExtraBold.ttf").toExternalForm(), 12);
		
        // VBox 레이아웃 설정
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("root");

        // 이름
        Label nameLabel = new Label("이름");
        TextField nameField = new TextField();
        nameLabel.getStyleClass().add("Form-label");
        VBox group1 = new VBox(2);
        group1.getChildren().addAll(nameLabel, nameField);
        
        // 나이
        Label ageLabel = new Label("나이");
        TextField ageField = new TextField();
        ageLabel.getStyleClass().add("Form-label");
        VBox group2 = new VBox(2);
        group2.getChildren().addAll(ageLabel, ageField);
        
        // 무게
        Label weightLabel = new Label("무게");
        TextField weightField = new TextField();
        weightLabel.getStyleClass().add("Form-label");
        VBox group3 = new VBox(2);
        group3.getChildren().addAll(weightLabel, weightField);

        // 성별
        Label genderLabel = new Label("성별");
        genderLabel.getStyleClass().add("Form-label");
        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll("수컷", "암컷");
        genderCombo.setPromptText("선택");
        genderCombo.getStyleClass().add("combo-box2");
        VBox group4 = new VBox(2);
        group4.getChildren().addAll(genderLabel, genderCombo);

        // 중성화 여부
        Label neuteredLabel = new Label("중성화 여부");
        neuteredLabel.getStyleClass().add("Form-label");
        ComboBox<String> neuteredCombo = new ComboBox<>();
        neuteredCombo.getItems().addAll("예", "아니오");
        neuteredCombo.setPromptText("선택");
        neuteredCombo.getStyleClass().add("combo-box2");
        VBox group5 = new VBox(2);
        group5.getChildren().addAll(neuteredLabel, neuteredCombo);

        // 보호소 입소 일자
        Label shelterDateLabel = new Label("보호소 입소 일자");
        DatePicker shelterDatePicker = new DatePicker();
        shelterDateLabel.getStyleClass().add("Form-label");
        VBox group6 = new VBox(2);
        group6.getChildren().addAll(shelterDateLabel, shelterDatePicker);

        // 버튼
        Button submitButton = new Button("입양 동물 추가");
        submitButton.getStyleClass().add("Form-button");
        
        HBox buttonBox = new HBox(submitButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getStyleClass().add("button-container");
        
        genderCombo.setMaxWidth(Double.MAX_VALUE);
        neuteredCombo.setMaxWidth(Double.MAX_VALUE);
        shelterDatePicker.setMaxWidth(Double.MAX_VALUE);
        shelterDatePicker.setPrefWidth(Double.MAX_VALUE); 

        // VBox에 모두 추가
        root.getChildren().addAll(
        		group1, group2, group3,
        		group4, group5, group6,
        		buttonBox
        );

        // 씬 생성 후 css 연결
        Scene scene = new Scene(root, 450, 650);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setTitle("입양 동물 등록");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL); // 다른 창 눌릴 수 없게 함
        stage.show();
    }

    public static void main(String[] args) {

    }
}