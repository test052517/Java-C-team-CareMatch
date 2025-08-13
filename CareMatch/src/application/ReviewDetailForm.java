package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ReviewDetailForm {

    private boolean liked = false;

    public void showModal(Stage owner) {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initOwner(owner);
        modal.setTitle("후기 상세보기");

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.getStyleClass().add("review-detail-root");

        // 타이틀 바
        Label titleLabel = new Label("후기 상세보기");
        titleLabel.getStyleClass().add("detail-title");

        // 이미지
        Region imagePlaceholder = new Region();
        imagePlaceholder.setPrefSize(300, 300);
        imagePlaceholder.setMaxSize(300, 300);
        imagePlaceholder.getStyleClass().add("image-placeholder");

        // 제목
        Label postTitle = new Label("제목: 코코몽의 하루 일기");
        postTitle.getStyleClass().add("post-title");

        // 날짜
        Label dateLabel = new Label("날짜: 20xx.xx.xx");
        dateLabel.getStyleClass().add("post-date");

        // 내용
        Label contentLabel = new Label("내용");
        contentLabel.getStyleClass().add("content-Label");
        HBox contentLabelBox = new HBox(contentLabel);
        contentLabelBox.setAlignment(Pos.CENTER_LEFT);
        
        
        
        TextArea contentArea = new TextArea("오늘 코코몽과의 첫 만남은 이러이러...");
        contentArea.setPrefHeight(200);
        contentArea.setEditable(false);
        contentArea.getStyleClass().add("content-area");
        
        VBox contentBox = new VBox(5);
        contentBox.getChildren().addAll(contentLabelBox,contentArea);

        // 버튼들
        Button likeButton = new Button("  좋아요");
        likeButton.getStyleClass().add("like-button");

        ImageView heartIcon = new ImageView("file:src/application/menu_icon/whiteheart.png");
        heartIcon.setFitHeight(26);
        heartIcon.setFitWidth(26);
        likeButton.setGraphic(heartIcon);

        likeButton.setOnAction(e -> {
            liked = !liked;
            if (liked) {
                heartIcon.setImage(new javafx.scene.image.Image("file:src/application/menu_icon/redheart.png"));
            } else {
                heartIcon.setImage(new javafx.scene.image.Image("file:src/application/menu_icon/whiteheart.png"));
            }
        });

        Button commentButton = new Button("  댓글창");
        commentButton.getStyleClass().add("comment-button");
        ImageView commentIcon = new ImageView("file:src/application/menu_icon/chat.png");
        commentIcon.setFitHeight(26);
        commentIcon.setFitWidth(26);
        commentButton.setGraphic(commentIcon);
        
        //댓글창 버튼 이벤트
        commentButton.setOnAction(event->{
        	ShowCommentForm scf = new ShowCommentForm();
        	scf.showCommentWindow();
        });

        HBox buttonBox = new HBox(60, likeButton, commentButton);
        buttonBox.setAlignment(Pos.CENTER);

        // 조립
        root.getChildren().addAll(titleLabel, imagePlaceholder, postTitle, dateLabel, contentBox, buttonBox);

        Scene scene = new Scene(root, 600, 800);
        scene.getStylesheets().add(getClass().getResource("review-detail.css").toExternalForm());

        modal.setScene(scene);
        modal.show();
    }
}