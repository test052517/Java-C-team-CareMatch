package application;

import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ManagerAdoptionApplicationForm {

    public void show(String animalName, String applicantName, String applyDate) {
    	Font.loadFont(getClass().getResource("/Pretendard-Medium.ttf").toExternalForm(), 12);
		Font.loadFont(getClass().getResource("/Pretendard-Bold.ttf").toExternalForm(), 12);
		Font.loadFont(getClass().getResource("/Pretendard-ExtraBold.ttf").toExternalForm(), 12);
		
        Stage stage = new Stage();
        stage.setTitle("입양 신청서 확인");

        // VBox 레이아웃 설정
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("root");

        // 이름
        Label nameLabel = new Label("이름");
        TextField nameField = new TextField(animalName);
        nameLabel.getStyleClass().add("Form-label");
        VBox group1 = new VBox(2);
        group1.getChildren().addAll(nameLabel, nameField);
        nameField.setDisable(true);

        // 신청인
        Label applicantLabel = new Label("입양 신청인");
        TextField applicantField = new TextField(applicantName);
        applicantLabel.getStyleClass().add("Form-label");
        VBox group2 = new VBox(2);
        group2.getChildren().addAll(applicantLabel, applicantField);
        applicantField.setDisable(true);

        // 신청일
        Label applyDateLabel = new Label("입양 신청일");
        DatePicker applyDatePicker = new DatePicker();
        applyDateLabel.getStyleClass().add("Form-label");
        VBox group3 = new VBox(2);
        group3.getChildren().addAll(applyDateLabel, applyDatePicker);
        applyDatePicker.setDisable(true);
        applyDatePicker.setValue(LocalDate.parse(applyDate));

        // 사유
        Label reasonLabel = new Label("입양 사유");
        TextArea reasonArea = new TextArea();
        reasonArea.setPromptText("입양 사유 내용");
        reasonArea.setPrefRowCount(8); 
        reasonArea.setMinHeight(220); 
        reasonArea.setWrapText(true);  
        reasonLabel.getStyleClass().add("Form-label");
        VBox group4 = new VBox(2);
        group4.getChildren().addAll(reasonLabel, reasonArea);
        reasonArea.setDisable(true);

        // 버튼
        Button approveButton = new Button("입양 승인");
        Button rejectButton = new Button("입양 거절");

        approveButton.getStyleClass().add("Orange-Btn-Form");
        rejectButton.getStyleClass().add("Brown-Btn-Form");
        
        HBox buttonBox = new HBox(120, approveButton, rejectButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getStyleClass().add("button-container");
        
        applyDatePicker.setMaxWidth(Double.MAX_VALUE);

        // 레이아웃 추가
        root.getChildren().addAll(
        		group1, group2, group3,
        		group4, buttonBox
        );

        // 씬 생성 후 css 연결
        Scene scene = new Scene(root, 450, 650);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        
        stage.setTitle("입양 신청서 확인");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.show();
    }
}