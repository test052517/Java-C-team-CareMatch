package application;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.geometry.Pos;

public class ReviewPageView {

    private ScrollPane scrollPane;

    public ReviewPageView() {
        GridPane animalGrid = new GridPane();
        animalGrid.setHgap(40);
        animalGrid.setVgap(20);
        animalGrid.setPadding(new Insets(10));
        animalGrid.setBackground(Background.fill(Color.web("fff")));
        animalGrid.setId("animalGrid");

        // 동물 카드 예시
        for (int i = 0; i < 12; i++) {
            VBox card = createAnimalCard("이름 " + (i + 1), "♀", 2);
            animalGrid.add(card, i % 4, i / 4);
        }

        scrollPane = new ScrollPane(animalGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefHeight(400);
        scrollPane.setId("scrollPane");
        
    }

    private VBox createAnimalCard(String name, String gender, int age) {
        VBox card = new VBox(10);
        card.setId("animalCard");

        Region imagePlaceholder = new Region();
        imagePlaceholder.setPrefSize(170, 170);
        imagePlaceholder.getStyleClass().add("imagePlaceholder");

        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("animalName");

        Label infoLabel = new Label(gender + " / " + age + "살");
        infoLabel.getStyleClass().add("animalInfo");

        card.getChildren().addAll(imagePlaceholder, nameLabel, infoLabel);
        card.setAlignment(Pos.CENTER);
        
        card.setOnMouseClicked(e->{
        	System.out.println("선택된 동물: "+ name);
        	ReviewDetailForm rdf = new ReviewDetailForm();
        	rdf.showModal(null);
        	
        });

        return card;
    }

    public ScrollPane getContent() {
    	
        return scrollPane;
    }
}