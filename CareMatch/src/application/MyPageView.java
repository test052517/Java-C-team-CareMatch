package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MyPageView {

    public static VBox getContent() {
        VBox content = new VBox(30);
        content.setPadding(new Insets(40, 60, 40, 60));
        content.getStyleClass().add("mypage-container");

        // Title
        Label title = new Label("마이 페이지");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.getStyleClass().add("page-title");

        Separator separator = new Separator();

        VBox form = new VBox(35);
        VBox id = new VBox(10);
        id.getChildren().addAll(createField("ID"), createField( "Value (수정 불가능)", false, null));
        
        VBox pw = new VBox(10);
        pw.getChildren().addAll(createField("PW"), createField( "********", true,"비밀번호 변경"));
        
        VBox phone = new VBox(10);
        phone.getChildren().addAll(createField("Phone"), createField( "010-1234-5678", false, "번호 변경"));
        
        VBox email = new VBox(10);
        email.getChildren().addAll( createField("E-mail"), createField( "example@email.com", false, "이메일 변경"));
        
        form.getChildren().addAll(
        		id,
        		pw,
                phone,
                email
                
        );

        content.getChildren().addAll(title, separator, form);
        return content;
    }
    
    private static HBox createField(String labelText) {
        Label label = new Label(labelText);
        label.setPrefWidth(100);
        label.setFont(Font.font("Arial", 16));
    	
    	HBox box = new HBox(10);
    	box.setAlignment(Pos.CENTER_LEFT);
    	box.setSpacing(15);
    	box.getChildren().addAll(label);
    	return box;
    }

    private static HBox createField(/*String labelText,*/ String value, boolean isPassword, String buttonLabel) {
      /*  Label label = new Label(labelText);
        label.setPrefWidth(100);
        label.setFont(Font.font("Arial", 16));*/

        TextField field = isPassword ? new PasswordField() : new TextField();
        field.setText(value);
        if(buttonLabel==null) {
        	field.setEditable(false);
        }else {
        	field.setEditable(true);
        }
        field.setPrefWidth(480);
        field.setPrefHeight(200);
        field.getStyleClass().add(isPassword ? "password-field" : "text-field");

        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setSpacing(15);
        box.getChildren().addAll(field);

        if (buttonLabel != null) {
            Button changeBtn = new Button(buttonLabel);
            changeBtn.setPrefWidth(200);
            changeBtn.getStyleClass().add("change-btn");
            box.getChildren().add(changeBtn);
        }

        return box;
    }
}