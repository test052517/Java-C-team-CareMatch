package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class ManagerReviewForm {

    public void show(String animalName, String userName, String writeDate, String title, String content, Image image1, Image image2) {
        Font.loadFont(getClass().getResource("/Pretendard-Medium.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/Pretendard-Bold.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/Pretendard-ExtraBold.ttf").toExternalForm(), 12);

        Stage stage = new Stage();
        stage.setTitle("후기 상세 확인");

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("root");

        // 동물 이름
        Label animalLabel = new Label("동물 이름");
        TextField animalField = new TextField(animalName);
        animalField.setDisable(true);
        animalLabel.getStyleClass().add("Form-label");
        VBox group1 = new VBox(2, animalLabel, animalField);

        // 사용자
        Label userLabel = new Label("사용자");
        TextField userField = new TextField(userName);
        userField.setDisable(true);
        userLabel.getStyleClass().add("Form-label");
        VBox group2 = new VBox(2, userLabel, userField);

        // 작성일자
        Label dateLabel = new Label("작성 일자");
        DatePicker datePicker = new DatePicker(LocalDate.parse(writeDate));
        datePicker.setDisable(true);
        dateLabel.getStyleClass().add("Form-label");
        VBox group3 = new VBox(2, dateLabel, datePicker);
        datePicker.setMaxWidth(Double.MAX_VALUE); // 최대 폭 사용

        // 제목
        Label titleLabel = new Label("제목");
        TextField titleField = new TextField(title);
        titleField.setDisable(true);
        titleField.setPrefHeight(100);
        titleLabel.getStyleClass().add("Form-label");
        VBox group4 = new VBox(2, titleLabel, titleField);

        // 이미지
        Label imageLabel = new Label("이미지");
        imageLabel.getStyleClass().add("Form-label");

        ImageView imageView1 = new ImageView(image1);
        ImageView imageView2 = new ImageView(image2);
        
        // 이미지 링크 연결(나중에 수정 가능)
        if (image1 != null) {
            imageView1.setImage(image1);
        } else {
            imageView1.setImage(new Image("file:resources/img_placeholder.png"));
        }
        if (image2 != null) {
            imageView2.setImage(image2);
        } else {
            imageView2.setImage(new Image("file:resources/img_placeholder.png"));
        }
        
        // 크기 및 스타일
        for (ImageView view : new ImageView[]{imageView1, imageView2}) {
            view.setFitWidth(55);
            view.setFitHeight(55);
            view.setStyle("-fx-background-color: #ddd; -fx-border-radius: 8;");
            view.setPreserveRatio(true);
        }

        HBox imageBox = new HBox(10, imageView1, imageView2);
        VBox group5 = new VBox(2, imageLabel, imageBox);

        // 내용
        Label contentLabel = new Label("내용");
        TextArea contentArea = new TextArea(content);
        contentArea.setDisable(true);
        contentArea.setWrapText(true);
        contentArea.setMinHeight(100);
        contentLabel.getStyleClass().add("Form-label");
        VBox group6 = new VBox(2, contentLabel, contentArea);

        root.getChildren().addAll(group1, group2, group3, group4, group5, group6);

        Scene scene = new Scene(root, 450, 650);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}