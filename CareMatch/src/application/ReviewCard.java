package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class ReviewCard extends HBox {
    public ReviewCard(Review review) {
        this.getStyleClass().add("animal-card");
        this.setSpacing(20);
        this.setAlignment(Pos.CENTER_LEFT); 

        // 이미지
        ImageView imageView = new ImageView("file:resources/img_placeholder.png");
        imageView.setFitWidth(55);
        imageView.setFitHeight(55);
        imageView.setStyle("-fx-background-color: #ddd; -fx-border-radius: 8;");

        // 텍스트 정보
        Label title = new Label(review.getTitle());
        title.getStyleClass().add("card-title");

        Label author = new Label("작성인: " + review.getAuthor());
        author.getStyleClass().add("card-info");

        VBox infoBox = new VBox(5, title, author);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        // 좋아요 수
        ImageView heartIcon = new ImageView("file:resources/icon/icon-heart.png");
        heartIcon.setFitWidth(16);
        heartIcon.setFitHeight(16);

        Label likesLabel = new Label(String.valueOf(review.getLikes()));
        likesLabel.getStyleClass().add("likes-label");

        HBox likeBox = new HBox(5, heartIcon, likesLabel);
        likeBox.setAlignment(Pos.CENTER_LEFT);

        // 버튼
        Button detailBtn = new Button("상세 확인");
        Button deleteBtn = new Button("삭제");

        detailBtn.setOnAction(e -> {
            ManagerReviewForm form = new ManagerReviewForm();
            form.show(
                review.getAnimalName(),           // 동물 이름
                review.getAuthor(),               // 사용자 이름
                review.getWriteDate(),            // 작성일 (yyyy-MM-dd)
                review.getTitle(),                // 제목
                review.getContent(),              // 내용
                review.getImage1(),               // 이미지 1
                review.getImage2()                // 이미지 2
            );
        });
        
        detailBtn.getStyleClass().add("Orange-Btn2");
        deleteBtn.getStyleClass().add("Brown-Btn2");

        HBox buttonBar = new HBox(10, likeBox, detailBtn, deleteBtn);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);
        buttonBar.setPadding(new Insets(20, 0, 0, 0));

        // 별 아이콘
        ImageView starIcon = new ImageView(
        	    review.isBest() 
        	        ? "file:resources/icon/icon-star.png"
        	        : "file:resources/icon/icon-star2.png"
        	);
        starIcon.setFitWidth(16);
        starIcon.setFitHeight(16);

        VBox rightBox = new VBox(10, starIcon, buttonBar);
        rightBox.setAlignment(Pos.TOP_RIGHT);

        // Spacer로 오른쪽 정렬
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // 최종 조립
        this.getChildren().addAll(imageView, infoBox, spacer, rightBox);
    }
}