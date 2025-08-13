package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class FontTest extends Application {

	@Override
    public void start(Stage stage) {
        Font font = Font.loadFont(getClass().getResource("/resources/BMJUA.ttf").toExternalForm(), 30);
        System.out.println("폰트 로드 결과: " + font);

        Label label = new Label("TEST");
        label.setFont(font);

        VBox box = new VBox(label);
        box.setAlignment(Pos.CENTER);
        Scene scene = new Scene(box, 400, 200);
        stage.setScene(scene);
        stage.setTitle("폰트 테스트");
        stage.show();
    }

    public static void main(String[] args) {
    	launch(args);
    }
}
