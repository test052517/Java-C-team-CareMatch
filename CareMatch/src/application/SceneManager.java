package application;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.function.Supplier;

public class SceneManager {
    private static Stage primaryStage;
    private static final HashMap<String, Supplier<Pane>> viewSuppliers = new HashMap<>();
    private static Scene scene;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void registerView(String name, Supplier<Pane> supplier) {
        viewSuppliers.put(name, supplier);
    }

    public static void switchTo(String name) {
        if (!viewSuppliers.containsKey(name)) {
            throw new IllegalArgumentException("View not registered: " + name);
        }

        Pane rootPane = viewSuppliers.get(name).get();

        if (scene == null) {
            scene = new Scene(rootPane, 700, 850);
            scene.getStylesheets().add(SceneManager.class.getResource("style.css").toExternalForm());
            primaryStage.setScene(scene);
        } else {
            scene.setRoot(rootPane);
        }
    }
}