package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AnimalDetailView extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 🔹 메인 레이아웃
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");
        
        ImageView back = new ImageView(new Image("file:src/application/menu_icon/Back.png"));
        back.setFitWidth(35);
        back.setFitHeight(35);
        back.setPickOnBounds(true);
        StackPane backContainer = new StackPane(back);
        backContainer.setStyle("-fx-border-color: transparent;");
        
        // 마우스가 들어가면 테두리 표시
        backContainer.setOnMouseEntered(e -> backContainer.setStyle("-fx-border-color: #888; -fx-border-width: 1; -fx-border-radius: 4;"));

        // 마우스가 나가면 테두리 제거
        backContainer.setOnMouseExited(e -> backContainer.setStyle("-fx-border-color: transparent;"));
        
        back.setOnMouseClicked(event->{
        	MainListApp mla = new MainListApp();
        	mla.start(primaryStage);
        });
        
        HBox backBox = new HBox(20);
        backBox.setAlignment(Pos.TOP_LEFT);
        backBox.getChildren().addAll(backContainer);
        
        //뒤로가기 기능


        // 🔹 이미지 영역 (두 개의 이미지 placeholder)
        HBox imageBox = new HBox(20);
        imageBox.setAlignment(Pos.CENTER);

        Region image1 = createImagePlaceholder();
        image1.getStyleClass().add("image-placeholder");
        Region image2 = createImagePlaceholder();
        
        //이미지 클릭 이벤트.
        image1.setOnMouseClicked(event ->{
        	System.out.println("image clicked");
        });

        imageBox.getChildren().addAll(image1, image2);

        // 🔹 이름/품종/정보
        Label nameLabel = new Label("별이 (코리안 숏 헤어)");
        nameLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #4B2E2E; -fx-font-weight: bold;");
        

        Label infoLabel = new Label("여아 | 2살 | 2kg | 중성화 O");
        infoLabel.getStyleClass().add("animal-info");
        infoLabel.setFont(Font.font(14));
        infoLabel.setTextFill(Color.web("#4B2E2E"));

        VBox nameInfoBox = new VBox(5, nameLabel, infoLabel);
        nameInfoBox.setAlignment(Pos.CENTER);

        // 🔹 보호소 정보 박스
        VBox infoBox = new VBox(5);
        infoBox.setPadding(new Insets(15));
        infoBox.setStyle("-fx-background-color: #FDF8F3; -fx-border-color: #E0CBB5; -fx-border-radius: 8; -fx-background-radius: 8;");

        infoBox.getChildren().addAll(
                createInfoRow("상태", "보호중"),
                createInfoRow("보호소", "경기도 반려동물 입양센터"),
                createInfoRow("보호주소", "경기도 수원시 팔달구 경수대로 460 청진빌딩 2층"),
                createInfoRow("보호소 연락처", "031-546-8488")
        );

        // 🔹 입양 신청 버튼
        Button adoptButton = new Button("입양 신청");
        adoptButton.setCursor(Cursor.HAND);
        adoptButton.setStyle("-fx-background-color: #F17C2B; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 30 10 30; -fx-background-radius: 8;");
        adoptButton.setPrefWidth(200);
        //입양신청 액션 이벤트
        adoptButton.setOnAction(e ->{
        	new AdoptionForm().showModal(primaryStage);
        });
        

        VBox.setMargin(adoptButton, new Insets(10, 0, 0, 0));
        VBox.setMargin(nameInfoBox, new Insets(10, 0, 0, 0));

        root.getChildren().addAll(backBox,imageBox, nameInfoBox, infoBox, adoptButton);
        root.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("입양 상세 화면");
        primaryStage.show();
    }

    // 🔸 이미지 placeholder (회색 박스)
    private Region createImagePlaceholder() {
        Region region = new Region();
        
        region.setPrefSize(400, 400);
        region.setStyle("-fx-background-color: #EFEFEF; -fx-background-radius: 8;");
        return region;
    }
    
   
    

    // 🔸 선택된 이미지처럼 보이게 (파란 테두리)
   /* private Region createImagePlaceholderWithBorder() {
        Region region = new Region();
        region.setPrefSize(170, 170);
        region.setStyle("-fx-background-color: #EFEFEF; -fx-border-color: #339CFF; -fx-border-width: 2; -fx-background-radius: 8; -fx-border-radius: 8;");
        return region;
    }*/

    // 🔸 보호소 정보 행
    private HBox createInfoRow(String label, String value) {
        Label keyLabel = new Label(label);
        keyLabel.setFont(Font.font(13));
        keyLabel.setTextFill(Color.GRAY);
        keyLabel.setPrefWidth(80);

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font(13));
        valueLabel.setWrapText(true);
        valueLabel.setMaxWidth(400);

        HBox row = new HBox(10, keyLabel, valueLabel);
        return row;
    }

    public static void main(String[] args) {
        launch(args);
    }
}