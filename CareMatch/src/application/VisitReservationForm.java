package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class VisitReservationForm extends Stage {

    public VisitReservationForm() {
        setTitle("방문 예약");
        setWidth(600);
        setHeight(800);
        initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        //root.getStyleClass().add("container");

        Label header = new Label("방문 예약");
        header.getStyleClass().add("title-label");

        Label subtitle = new Label("입양할 동물: xx (입양 신청 확인에서 넘어온 경우 자동표기)");
        subtitle.getStyleClass().add("subtitle");

        // 예약자 정보
        VBox formBox = new VBox(15);

        Label nameLabel = new Label("이름:");
        TextField nameField = new TextField("홍길동 (수정 불가능)");
        nameField.setDisable(true);

        Label contactLabel = new Label("연락처:");
        TextField contactField = new TextField("010-8888-8888");

        Label reasonLabel = new Label("방문 사유:");
        TextArea reasonArea = new TextArea("입양, 구경 등");
        reasonArea.setPrefRowCount(3);

        // 날짜 선택
        Label dateLabel = new Label("📅 날짜 선택");
        DatePicker datePicker = new DatePicker();

        // 시간 선택
        Label timeLabel = new Label("🕒 시간 선택");
        ComboBox<String> timeBox = new ComboBox<>();
        timeBox.getItems().addAll("오전 10시", "오후 1시", "오후 3시", "오후 5시");
        timeBox.setValue("오전 10시");

        // 버튼 및 안내 문구
        Label promiseLabel = new Label("\"입양은 한 생명의 남은 삶을 함께하는 약속입니다\"");
        promiseLabel.getStyleClass().add("promise");

        Button submitBtn = new Button("예약 완료");
        submitBtn.getStyleClass().add("submit-button");
        submitBtn.setOnAction(e -> this.close());
        HBox submitBox = new HBox(submitBtn);
        submitBox.setAlignment(Pos.CENTER);

        formBox.getChildren().addAll(
                nameLabel, nameField,
                contactLabel, contactField,
                reasonLabel, reasonArea,
                dateLabel, datePicker,
                timeLabel, timeBox,
                promiseLabel,
                submitBox
        );

        root.getChildren().addAll(header, subtitle, formBox);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("AdoptionForm.css").toExternalForm());
        setScene(scene);
    }
}