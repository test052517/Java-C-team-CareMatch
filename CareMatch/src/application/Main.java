package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
    	//메인 컬러 주황 F27D16
    	final String trademarkColor1="F27D16";
    	final String trademarkColor2="5a3222";
    	
    	
        primaryStage.setTitle("CareMatch 로그인");

        VBox root = new VBox(10);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);
        root.getStyleClass().add("root-pane");

        // 🐱 로고 이미지
        ImageView logo = new ImageView(new Image("file:src/application/menu_icon/Logo1.png"));
        logo.setFitWidth(100);
        logo.setPreserveRatio(true);

        // CareMatch 텍스트
        /*Label title = new Label("CareMatch");
        title.setFont(Font.font("Arial", 20));
        title.setTextFill(Color.web("#5c3222"));*/

        // ID 필드
        Label idLabel = new Label("ID");
        idLabel.getStyleClass().add("text-label");
        TextField idField = new TextField();
        idField.setPromptText("Enter your ID");
        idField.getStyleClass().add("input-field");
    	idField.setText("1234");
    	

        // Password 필드
        Label pwLabel = new Label("Password");
        pwLabel.getStyleClass().add("text-label");
        PasswordField pwField = new PasswordField();
        pwField.setPromptText("Enter your password");
        pwField.getStyleClass().add("input-field");
        pwField.setText("5678");
        // 로그인 버튼
        Button loginBtn = new Button("로그인");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.getStyleClass().add("login-button");
        
        //로그인 버튼 이벤트
        loginBtn.setOnAction(event ->{
        	//String id = idField.getText();
        	//String pwd = pwField.getText();
        		MainListApp mla = new MainListApp();
        		mla.start(primaryStage);
        	
        	
        }); 
        

        // 회원가입 버튼
        Button signupBtn = new Button("회원가입");
        signupBtn.setMaxWidth(Double.MAX_VALUE);
        signupBtn.getStyleClass().add("signup-button");
        
        //회원가입 버튼 이벤트
        signupBtn.setOnAction(event ->{
        //	Stage newWindow = new Stage();
        	NewAccountForm na = new NewAccountForm();
        	na.showModal(primaryStage);
        	
        	
        });

        // 비밀번호 찾기
        Label forgotPw = new Label("비밀번호를 잊으셨나요?");
        forgotPw.getStyleClass().add("forgot-label");
        
        //라벨 마우스 클릭
        forgotPw.setOnMousePressed(arg0 ->{
        	
        });

        // 레이아웃 조립
        root.getChildren().addAll(
                logo,
               // title,
                idLabel,
                idField,
                pwLabel,
                pwField,
                loginBtn,
                signupBtn,
                forgotPw
        );
        
        

        Scene scene = new Scene(root, 350, 450);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}