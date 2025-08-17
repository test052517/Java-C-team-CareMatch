package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import application.ManagerChatPage.Chat;

public class ChatCard extends HBox {
    public ChatCard(Chat chat) {
        this.getStyleClass().add("animal-card"); // 공통 스타일
        this.setSpacing(20);
        this.setAlignment(Pos.CENTER_LEFT);
        
        this.setPadding(new Insets(15));
        this.setMaxWidth(Double.MAX_VALUE); // 너비 최대 설정

        // 텍스트 정보
        VBox infoBox = new VBox(5);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        Label userIdLabel = new Label(chat.getUserId());
        userIdLabel.getStyleClass().add("card-title");

        Label dateLabel = new Label(chat.getCreatedDate().toString());
        dateLabel.getStyleClass().add("card-info");

        infoBox.getChildren().addAll(userIdLabel, dateLabel);

        // 버튼 영역
        Button chatBtn = new Button("채팅 입장");
        chatBtn.getStyleClass().add("Orange-Btn2");

        Button saveBtn = new Button("내용 저장");
        saveBtn.getStyleClass().add("Brown-Btn2");

        HBox buttonBox = new HBox(10, chatBtn, saveBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        VBox rightBox = new VBox(10, buttonBox);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        // 오른쪽 밀어내기 위한 Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // 최종 조립
        this.getChildren().addAll(infoBox, spacer, rightBox);
    }
}