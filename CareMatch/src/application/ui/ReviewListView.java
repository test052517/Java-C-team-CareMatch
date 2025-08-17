package application.ui;

import application.Review;
import application.ReviewCard;
import javafx.scene.layout.VBox;

import java.util.List;

public class ReviewListView extends VBox {
    public ReviewListView(List<Review> reviews) {
        super(10);
        this.getStyleClass().add("review-list");
        updateList(reviews);
    }

    public void updateList(List<Review> reviews) {
        this.getChildren().clear();
        for (Review review : reviews) {
            this.getChildren().add(new ReviewCard(review));
        }
    }
}