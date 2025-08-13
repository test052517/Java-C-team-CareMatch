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

public class ManagerVisitApplicationForm {

    public void show(String animalName, String applicantName, String applyDate, String visitDate) {
        Font.loadFont(getClass().getResource("/Pretendard-Medium.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/Pretendard-Bold.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/Pretendard-ExtraBold.ttf").toExternalForm(), 12);

        Stage stage = new Stage();
        stage.setTitle("방문 신청서 확인");

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("root");

        // 이름
        Label nameLabel = new Label("이름");
        TextField nameField = new TextField(animalName);
        nameField.setDisable(true);
        nameLabel.getStyleClass().add("Form-label");
        VBox group1 = new VBox(2, nameLabel, nameField);

        // 신청인
        Label applicantLabel = new Label("입양 신청인");
        TextField applicantField = new TextField(applicantName);
        applicantField.setDisable(true);
        applicantLabel.getStyleClass().add("Form-label");
        VBox group2 = new VBox(2, applicantLabel, applicantField);

        // 신청일자
        Label applyDateLabel = new Label("신청 일자");
        DatePicker applyDatePicker = new DatePicker();
        applyDatePicker.setValue(LocalDate.parse(applyDate));
        applyDatePicker.setDisable(true);
        applyDateLabel.getStyleClass().add("Form-label");
        VBox group3 = new VBox(2, applyDateLabel, applyDatePicker);

        // 희망 방문일자
        Label visitDateLabel = new Label("희망 방문 일자");
        DatePicker visitDatePicker = new DatePicker();
        visitDatePicker.setValue(LocalDate.parse(visitDate));
        visitDatePicker.setDisable(true);
        visitDateLabel.getStyleClass().add("Form-label");
        VBox group4 = new VBox(2, visitDateLabel, visitDatePicker);

        // 방문 목적
        Label reasonLabel = new Label("방문 목적");
        TextArea reasonArea = new TextArea();
        reasonArea.setPromptText("방문 목적을 입력하세요.");
        reasonArea.setPrefRowCount(8);
        reasonArea.setMinHeight(170);
        reasonArea.setWrapText(true);
        reasonArea.setDisable(true);
        reasonLabel.getStyleClass().add("Form-label");
        VBox group5 = new VBox(2, reasonLabel, reasonArea);

        // 라디오 버튼: 방문 거절 사유
        ToggleGroup reasonGroup = new ToggleGroup();
        RadioButton reason1 = new RadioButton("해당 방문 일정 불가능");
        RadioButton reason2 = new RadioButton("방문 목적 사유 불충족");
        reason1.setToggleGroup(reasonGroup);
        reason2.setToggleGroup(reasonGroup);
        reason1.getStyleClass().add("Radio");
        reason2.getStyleClass().add("Radio");

        VBox radioBox = new VBox(5, reason1, reason2);
        radioBox.setAlignment(Pos.CENTER_RIGHT);
        radioBox.setPadding(new Insets(0, 0, 8, 0));
        radioBox.getStyleClass().add("radio-container");

        // 버튼
        Button approveButton = new Button("방문 승인");
        Button rejectButton = new Button("방문 거절");
        approveButton.getStyleClass().add("Orange-Btn-Form");
        rejectButton.getStyleClass().add("Brown-Btn-Form");
        
        VBox approveSection = new VBox(approveButton);
        approveSection.setAlignment(Pos.BOTTOM_LEFT);
        
        VBox rejectSection = new VBox(8, radioBox, rejectButton);
        rejectSection.setAlignment(Pos.BOTTOM_RIGHT);
        
        HBox buttonBox = new HBox(80, approveSection, rejectSection);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getStyleClass().add("button-container");
        
        VBox.setMargin(group5, new Insets(0, 0, -30, 0));
        applyDatePicker.setMaxWidth(Double.MAX_VALUE);
        visitDatePicker.setMaxWidth(Double.MAX_VALUE);

        // 레이아웃 추가
        root.getChildren().addAll(
                group1, group2, group3, group4, group5, buttonBox
        );

        Scene scene = new Scene(root, 450, 720);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setTitle("방문 신청서 확인");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}