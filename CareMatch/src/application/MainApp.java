package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager.setStage(primaryStage);

        // 페이지 등록
        SceneManager.registerView("mainList", () -> new ManagerMainList().getView());
        SceneManager.registerView("adoptionList", () -> new ManagerAdoptionList().getView());

        // 초기 화면
        SceneManager.switchTo("mainList");

        primaryStage.setTitle("CareMatch 관리자 페이지");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}