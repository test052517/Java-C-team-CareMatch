package application;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class AdoptionCard extends HBox {
	
	private ComboBox<String> statusComboBox;
	private Runnable onStatusChanged; 
	
    public AdoptionCard(Adoption adoption) {
    	// this.onStatusChanged = onStatusChanged;
    	
    	this.getStyleClass().add("animal-card");
        this.setSpacing(20);
        
        // 이미지 자리 (더미 or 실제 이미지)
        ImageView imageView = new ImageView("file:resources/img_placeholder.png");
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
        imageView.setStyle("-fx-background-color: #ddd; -fx-border-radius: 8;");
        
        // 동물 이름 + 신청인 이름
        VBox infoBox = new VBox(5);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        
        VBox imageBox = new VBox(imageView);
        imageBox.setAlignment(Pos.CENTER_LEFT);
        
        Label animalNameLabel = new Label(adoption.getAnimalName());
        animalNameLabel.getStyleClass().add("card-title");
        
        Label applicantLabel = new Label("신청인: " + adoption.getApplicantName());
        applicantLabel.getStyleClass().add("card-info");
        
        Label applyDateLabel = new Label("신청일자: " + adoption.getApplyDate());
        applyDateLabel.getStyleClass().add("card-info");

        Label visitDateLabel = new Label("방문일자: " + adoption.getVisitDate());
        visitDateLabel.getStyleClass().add("card-info");
        
        infoBox.getChildren().addAll(animalNameLabel, applicantLabel, applyDateLabel, visitDateLabel);
        
        // 상태 콤보박스
        statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("승인 미완료", "입양 승인", "방문 예약", "입양 완료");
        statusComboBox.setValue(adoption.getStatus());
        statusComboBox.setPrefWidth(120);

        statusComboBox.setOnAction(e -> {
        	adoption.setStatus(statusComboBox.getValue());
            if (this.onStatusChanged != null) {
                this.onStatusChanged.run();
            }
        });
        
        // 버튼 박스
        Button checkApplicationBtn = new Button("입양 신청서 확인");
        checkApplicationBtn.getStyleClass().add("Orange-Btn");
        checkApplicationBtn.setPrefWidth(120);

        Button scheduleVisitBtn = new Button("방문 일정 예약");
        scheduleVisitBtn.getStyleClass().add("Brown-Btn");
        scheduleVisitBtn.setPrefWidth(120);
        
        checkApplicationBtn.setOnAction(e -> {
            ManagerAdoptionApplicationForm form = new ManagerAdoptionApplicationForm();
            form.show(
            	adoption.getAnimalName(),    // 동물 이름
            	adoption.getApplicantName(),     // 신청인
            	adoption.getApplyDate()      // 신청일 (String)
            );
        });
        
        scheduleVisitBtn.setOnAction(e -> {
        	ManagerVisitApplicationForm form = new ManagerVisitApplicationForm();
            form.show(
            	adoption.getAnimalName(),    
            	adoption.getApplicantName(),   
            	adoption.getApplyDate(), // 임의 값(나중에 DB 연동)
            	adoption.getVisitDate()   // 임의 값(나중에 DB 연동)
            );
        });
        
        VBox rightBox = new VBox(10);
        rightBox.setAlignment(Pos.TOP_RIGHT);
        
     // 상태 콤보박스를 상단에, 버튼은 하단에
        Region spacer = new Region(); // 중간 공간
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        VBox buttonBox = new VBox(8, checkApplicationBtn, scheduleVisitBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        rightBox.getChildren().addAll(statusComboBox, spacer, buttonBox);
        rightBox.setPrefWidth(150);

        this.getChildren().addAll(imageBox, infoBox, rightBox);
        this.setHgrow(rightBox, Priority.ALWAYS);
    }
}