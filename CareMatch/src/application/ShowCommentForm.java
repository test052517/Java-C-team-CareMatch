package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ShowCommentForm {

    public void showCommentWindow() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("댓글창");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("comment-root");

        Label header = new Label("댓글창");
        header.getStyleClass().add("detail-title");

        VBox commentList = new VBox(15);
        commentList.getStyleClass().add("comment-list");
        commentList.setPadding(new Insets(10));


        // 구분선
        Separator separator = new Separator();
        separator.getStyleClass().add("separator");
        commentList.getChildren().add(separator);

        // 임시 사용자 댓글
        String[] texts = {
                "코코몽 귀여워요!", "코코몽 입이 멋있어요!", "코코몽 이빨 없다~", "정말 사랑스러워요!",
                "저도 입양하고 싶어요!", "와 진짜 귀엽다", "천사 강아지네요", "최고에요!"
        };

        for (int i=0;i<8;i++) {
            commentList.getChildren().add(createCommentBox("사용자"+(i+1), texts[i]));
        }

        ScrollPane scrollPane = new ScrollPane(commentList);
        
        scrollPane.setId("scrollPane");
      
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(620);

        // 입력 영역
        HBox inputBox = new HBox(5);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        TextArea inputArea = new TextArea();
        inputArea.setPromptText("Value");
        inputArea.setPrefRowCount(2);
        inputArea.setPrefWidth(500);
        

        Button submitBtn = new Button("등록");
        submitBtn.getStyleClass().add("submit-button");
       VBox.setVgrow(submitBtn, Priority.ALWAYS);
       submitBtn.setMaxSize(65,43);

        submitBtn.setOnAction(event -> {
            String content = inputArea.getText().trim();
            if (!content.isEmpty()) {
                VBox newComment = createCommentBox("등록자", content);
                newComment.getStyleClass().add("new-comment");
                commentList.getChildren().add(0, newComment); // separator 위에 추가
                inputArea.clear();
            }
        });
        
        Region spacer = new Region();
        spacer.setPrefHeight(10);

        inputBox.getChildren().addAll(inputArea, submitBtn);

        root.getChildren().addAll(header, scrollPane,spacer, inputBox);

        Scene scene = new Scene(root, 600, 800);
        scene.getStylesheets().add(getClass().getResource("review-detail.css").toExternalForm());

        dialog.setScene(scene);
        inputArea.requestFocus();
        
        
        dialog.showAndWait();
    }

    private VBox createCommentBox(String username, String content) {
        VBox box = new VBox(5);
        box.getStyleClass().add("comment-box");

        Label userLabel = new Label(username + "    " + getCurrentDate());
        userLabel.getStyleClass().add("user-label");

        TextArea text = new TextArea(content);
        text.setWrapText(true);
        text.setEditable(false);
        text.getStyleClass().add("comment-text");

        box.getChildren().addAll(userLabel, text);
        return box;
    }

    private String getCurrentDate() {
        LocalDate date = LocalDate.now();
        return date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }
}
