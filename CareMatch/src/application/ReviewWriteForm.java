package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class ReviewWriteForm {

    public void show(Stage parentStage) {
        Stage modalStage = new Stage();
        modalStage.initOwner(parentStage);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("후기 작성");

        VBox root = new VBox(40);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);
        root.setPrefSize(600, 800);
        root.getStyleClass().add("modal-root");

        // 제목 영역
        Label titleLabel = new Label("후기 작성");
        titleLabel.getStyleClass().add("modal-title");

        // 제목 입력
        Label subjectLabel = new Label("제목");
        subjectLabel.setFont(Font.font("bold"));
        TextField subjectField = new TextField();
        subjectField.setPromptText("Value");
        subjectField.getStyleClass().add("text-field");
        
        //제목 영역 박스
        HBox subjectLabelBox = new HBox();
        subjectLabelBox.getChildren().add(subjectLabel);
        subjectLabelBox.setAlignment(Pos.CENTER_LEFT);
        
        VBox subjectBox = new VBox(10);
        subjectBox.getChildren().addAll(subjectLabelBox,subjectField);
        
        // 내용 입력
        Label contentLabel = new Label("내용");
        TextArea contentArea = new TextArea();
        contentArea.setPromptText("Value");
        contentArea.setPrefHeight(300);
        contentArea.getStyleClass().add("text-area");
        
        //내용 구역 박스
        HBox contentLabelBox = new HBox();
        contentLabelBox.getChildren().add(contentLabel);
        contentLabelBox.setAlignment(Pos.CENTER_LEFT);
        
        VBox contentBox = new VBox(10);
        contentBox.getChildren().addAll(contentLabelBox,contentArea);
        

        // 사진 첨부
        HBox photoBox = new HBox(30);
        photoBox.setAlignment(Pos.CENTER_LEFT);
        photoBox.getStyleClass().add("photo-box");

        ImageView photoIcon = new ImageView(new Image("file:src/application/menu_icon/attatch.png"));
        photoIcon.setFitWidth(30);
        photoIcon.setFitHeight(30);
        
        
        Label photoLabel = new Label("사진 첨부");
        Button attachButton = new Button();
        attachButton.setGraphic(photoBox);
        attachButton.getStyleClass().add("attach-button");
        photoBox.getChildren().addAll(photoIcon, photoLabel);
        photoBox.setAlignment(Pos.CENTER);
        
        

        attachButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("사진 선택");
            File selectedFile = fileChooser.showOpenDialog(modalStage);
            if (selectedFile != null) {
                System.out.println("선택한 파일: " + selectedFile.getAbsolutePath());
                photoLabel.setText(selectedFile.getAbsolutePath());
                // 여기에 선택한 파일을 저장하거나 미리보기 구현 가능
            }
        });

        // 등록 버튼
        Button submitButton = new Button("등록");
        submitButton.getStyleClass().add("submit-button");
        
        //등록버튼 이벤트 구현
        submitButton.setOnAction(event->{
        	
        });
        
        
        root.getChildren().addAll(
                titleLabel,
                subjectBox,
                contentBox,
                attachButton,
                submitButton
        );

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("review.css").toExternalForm());

        modalStage.setScene(scene);
        modalStage.showAndWait();
    }
}