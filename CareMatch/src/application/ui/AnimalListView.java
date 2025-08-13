package application.ui;

import javafx.scene.layout.VBox;
import java.util.List;
import application.Animal;
import application.AnimalCard;

public class AnimalListView extends VBox {
    
    public AnimalListView(List<Animal> animals) {
        super(10);  // 자식들 간 간격 10px
        this.getStyleClass().add("animal-list");
        updateList(animals);
    }
    
    // 리스트 새로고침 메서드
    public void updateList(List<Animal> animals) {
        this.getChildren().clear();
        for (Animal animal : animals) {
            this.getChildren().add(new AnimalCard(animal));
        }
    }
}