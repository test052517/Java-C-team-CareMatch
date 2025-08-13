package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdoptionForm {

    public void showModal(Stage owner) {
    	
        // 새 Stage (모달)
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("입양 신청");

        // 상단 제목
        Label titleLabel = new Label("입양 신청");
        titleLabel.getStyleClass().add("title-label");

        // 입양할 동물
        Label animalLabel = new Label("입양할 동물: xx (자동표기)");
        animalLabel.getStyleClass().add("animal-label");
        

        // 신청자 정보
        Label applicantLabel = new Label("📋 신청자 정보");
        applicantLabel.getStyleClass().add("section-label");
        
        TextField nameField = new TextField("홍길동(수정 불가능)");
       
       

        TextField phoneField = new TextField("010-8888-8888");
        TextField emailField = new TextField("ehddmleogkrry@naver.com");
       

        TextField addressField = new TextField("부산시 부산역 (수정 불가능)");
        addressField.setEditable(false);
        
        Label introLabel = new Label("자기소개");
        Label reasonLabel = new Label("입양 사유");
        
        HBox introBox = new HBox(introLabel);
        HBox reasonBox = new HBox(reasonLabel);
        introBox.setAlignment(Pos.CENTER_LEFT);
        reasonBox.setAlignment(Pos.CENTER_LEFT);
        introBox.setSpacing(20);
        reasonBox.setSpacing(20);
       

        TextArea introArea = new TextArea();
        introArea.setPromptText("안녕하세요~");
        introArea.requestFocus();
        introArea.setPrefRowCount(3);

        TextArea reasonArea = new TextArea();
        reasonArea.setPromptText("외로움을 많이 타서...");
        reasonArea.setPrefRowCount(3);

        // 동의 체크박스 + 안내 문구
        CheckBox agreeCheck = new CheckBox("위 내용을 읽고 동의합니다.");
        agreeCheck.getStyleClass().add("agree-check");

        Label agreeNote = new Label("\"입양은 한 생명의 남은 삶을 함께하는 약속입니다\"");
        agreeNote.getStyleClass().add("agree-note");

        // 버튼
        Button submitBtn = new Button("신청 완료");
        submitBtn.getStyleClass().add("submit-button");
        
        submitBtn.setOnAction(e->{
        	VisitReservationForm vrf = new VisitReservationForm();
        	vrf.showAndWait();
        });

        // 폼 레이아웃
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col1.setPercentWidth(30);
        col2.setPercentWidth(70);
        formGrid.getColumnConstraints().addAll(col1,col2);
        formGrid.add(new Label("이름:"), 0, 0); formGrid.add(nameField, 1, 0);
        formGrid.add(new Label("연락처:"), 0, 1); formGrid.add(phoneField, 1, 1);
        formGrid.add(new Label("이메일:"), 0, 2); formGrid.add(emailField, 1, 2);
        formGrid.add(new Label("주소:"), 0, 3); formGrid.add(addressField, 1, 3);
        


        VBox root = new VBox(15,
                titleLabel,
                animalLabel,
                applicantLabel,
                formGrid,
                introBox,
                introArea,
                reasonBox,
                reasonArea,
                agreeCheck,
                agreeNote,
                submitBtn
        );
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setPrefSize(600, 800);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("AdoptionForm.css").toExternalForm());

        dialog.setScene(scene);
        introArea.requestFocus();
        dialog.showAndWait();
    }
}