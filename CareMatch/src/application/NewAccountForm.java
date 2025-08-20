package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NewAccountForm {

    public void showModal(Stage owner) {
    	
    	
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("CareMatch 회원가입");
        
        

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);
        root.getStyleClass().add("root-pane");

        // 로고 이미지
        ImageView logo = new ImageView(new Image("file:src/application/menu_icon/Logo1.png")); // 경로 수정
        logo.setFitHeight(100);
        logo.setPreserveRatio(true);

        // ID
        Label idl = new Label("아이디");
        TextField idField = new TextField();
        idField.getStyleClass().add("tf-background");
        idField.setPromptText("아이디 입력(6~20자)");
        idField.setPrefHeight(30);
        idField.setPrefWidth(300);
        Button checkIdBtn = new Button("중복확인");
        checkIdBtn.setPrefWidth(150);

        Label idError = new Label("아이디 사용할 수 없는 아이디입니다.");
        idError.getStyleClass().add("label-error");
        idError.setVisible(false);
        idError.setManaged(false);

        HBox idBox = new HBox(10, idField, checkIdBtn);
        idBox.setAlignment(Pos.CENTER_LEFT);
        idBox.setMaxWidth(Double.MAX_VALUE);
        VBox idvbox = new VBox(5, idl, idBox, idError);

        // 비밀번호
        Label pwl = new Label("비밀번호");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("비밀번호 입력 (문자, 숫자, 특수문자 포함 8~20자)");
        passwordField.getStyleClass().add("tf-background");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("비밀번호 재입력");
        confirmPasswordField.getStyleClass().add("tf-background");

        Label pwError = new Label("20자 이내로 비밀번호를 입력해주세요.");
        pwError.getStyleClass().add("label-error");
        pwError.setVisible(false);
        pwError.setManaged(false);

        Label pwMismatchError = new Label("비밀번호가 일치하지 않습니다.");
        pwMismatchError.getStyleClass().add("label-error");
        pwMismatchError.setVisible(false);
        pwMismatchError.setManaged(false);

        VBox pwVBox = new VBox(10, pwl, passwordField, confirmPasswordField, pwError, pwMismatchError);

        // 전화번호
        Label phonel = new Label("전화번호");
        TextField phoneField = new TextField();
        phoneField.setPromptText("휴대폰 번호 입력('-' 제외 11자리 입력)");
        phoneField.getStyleClass().add("tf-background");
        VBox phoneVBox = new VBox(10, phonel, phoneField);

        // 이메일
        Label emaill = new Label("이메일 주소");
        TextField emailField = new TextField();
        emailField.setPromptText("이메일 주소");
        emailField.getStyleClass().add("tf-background");
        emailField.setPrefWidth(300);
        Button emailAuthBtn = new Button("인증번호 받기");
        emailAuthBtn.setPrefWidth(200);

        HBox emailBox = new HBox(10, emailField, emailAuthBtn);
        emailBox.setAlignment(Pos.CENTER_LEFT);
        
        VBox emailVBox = new VBox(10, emaill, emailBox);

        // 인증번호
        Label authCode = new Label("인증번호 입력");
        TextField authCodeField = new TextField();
        authCodeField.setPromptText("인증번호 입력");
        authCodeField.getStyleClass().add("tf-background");
        VBox authVBox = new VBox(10, authCode, authCodeField);

        // 가입 버튼
        Button registerBtn = new Button("가입하기");
        registerBtn.setMaxWidth(Double.MAX_VALUE);

        // 이벤트 예시
        checkIdBtn.setOnAction(e -> {
            String id = idField.getText();
            if (id.length() < 6 || id.length() > 20) {
                idError.setManaged(true);
                idError.setVisible(true);
            } else {
                idError.setVisible(false);
                idError.setManaged(false);
            }
        });

        registerBtn.setOnAction(e -> {
            String pw = passwordField.getText();
            String confirmPw = confirmPasswordField.getText();

            if (pw.length() < 8 || pw.length() > 20) {
                pwError.setManaged(true);
                pwError.setVisible(true);
            } else {
                pwError.setVisible(false);
                pwError.setManaged(false);
            }

            if (!pw.equals(confirmPw)) {
                pwMismatchError.setManaged(true);
                pwMismatchError.setVisible(true);
            } else {
                pwMismatchError.setVisible(false);
                pwMismatchError.setManaged(false);
            }

            // 가입 처리 로직...
        });

        root.getChildren().addAll(
                logo,
                idvbox,
                pwVBox,
                phoneVBox,
                emailVBox,
                authVBox,
                registerBtn
        );

        Scene scene = new Scene(root, 500, 800);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        stage.setScene(scene);
        stage.showAndWait(); // 모달로 보여줌
    }
}