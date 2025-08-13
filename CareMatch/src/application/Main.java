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
        root.setStyle("-fx-background-color: white;");

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
        idLabel.setTextFill(Color.web("#5c3222"));
        TextField idField = new TextField();
        idField.setPromptText("Enter your ID");
        idField.setStyle("-fx-background-color: #fef9f2; -fx-border-radius: 8; -fx-background-radius: 8;");
    	idField.setText("1234");
    	

        // Password 필드
        Label pwLabel = new Label("Password");
        pwLabel.setTextFill(Color.web("#5c3222"));
        PasswordField pwField = new PasswordField();
        pwField.setPromptText("Enter your password");
        pwField.setStyle("-fx-background-color: #fef9f2; -fx-border-radius: 8; -fx-background-radius: 8;");
        pwField.setText("5678");
        // 로그인 버튼
        Button loginBtn = new Button("로그인");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle("-fx-background-color: #F27D16; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
        
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
        signupBtn.setStyle("-fx-background-color: #5a3222; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
        
        //회원가입 버튼 이벤트
        signupBtn.setOnAction(event ->{
        //	Stage newWindow = new Stage();
        	NewAccountForm na = new NewAccountForm();
        	na.showModal(primaryStage);
        	
        	
        });

        // 비밀번호 찾기
        Label forgotPw = new Label("비밀번호를 잊으셨나요?");
        forgotPw.setFont(Font.font(12));
        forgotPw.setTextFill(Color.web("#a48e84"));
        
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
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}