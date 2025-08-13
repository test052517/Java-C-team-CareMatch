package application;



import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdoptionCheckForm {

    public static void show(Stage owner) {
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("입양신청 확인");

        VBox root = new VBox(15);
        root.getStyleClass().add("root");

        // 상단 타이틀 버튼 스타일 Label (모달 상단)
        Label headerLabel = new Label("입양신청 확인");
        headerLabel.getStyleClass().add("header-label");

        VBox contentBox = new VBox(10);
    
        contentBox.getStyleClass().add("content-box");
        contentBox.setAlignment(Pos.BASELINE_LEFT);
        

        // 제목 라벨
        Label titleLabel = new Label("📄 입양신청 확인");
        titleLabel.getStyleClass().add("section-title");

        // 입양 대상
        Label adoptLabel = new Label("🐶 입양 대상: 코코몽");
        adoptLabel.getStyleClass().add("info-label");

        // 신청일자
        Label dateLabel = new Label("📅 신청일자: 20xx.xx.xx");
        dateLabel.getStyleClass().add("info-label");

        // 상태
        Label statusLabel = new Label("📌 상태: ⏳ 승인 대기 중");
        statusLabel.getStyleClass().add("info-label");

        // 안내 메시지
        Label announce = new Label("🧾 안내 메시지 ");
        announce.getStyleClass().add("info-label");
        Label msgArea = new Label("\"관리자의 승인 후 방문 예약이 가능합니다.\"");
        
        //버튼 
        Button checkBtn = new Button("확인");
        checkBtn.setPrefWidth(190);
        
        Region spacer3 = new Region();
        spacer3.setPrefHeight(200);
        
       
        VBox checkBox = new VBox();
        checkBox.getChildren().add(checkBtn);
        checkBox.setAlignment(Pos.BOTTOM_CENTER);
        

        checkBtn.setOnAction(event->{
        	//창 닫는 메소드
        	//((Stage) checkBtn.getScene().getWindow()).close(); 
        	AdoptionCheckedForm acdf = new AdoptionCheckedForm();
        	acdf.show(owner);
        });
        
        
        //공간1
        Region spacer = new Region();
        spacer.setPrefHeight(30);
        Region spacer2 = new Region();
        spacer.setPrefHeight(20);
        
        contentBox.getChildren().addAll( adoptLabel, dateLabel, statusLabel, announce ,msgArea,spacer3,checkBox);
        root.getChildren().addAll(headerLabel,spacer,titleLabel,spacer2, contentBox);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 600, 800);
        scene.getStylesheets().add(AdoptionCheckForm.class.getResource("adoption-check.css").toExternalForm());

        stage.setScene(scene);
        stage.showAndWait();
    }
}