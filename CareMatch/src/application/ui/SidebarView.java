package application.ui;

import application.SceneManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class SidebarView extends VBox {

    public SidebarView() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));
        this.getStyleClass().add("sidebar");
        this.setPrefHeight(Double.MAX_VALUE);
        this.setFillWidth(true);

        // 로고
        Image logoImage = new Image(getClass().getResource("/Logo.png").toExternalForm());
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(100);
        logoView.setPreserveRatio(true);
        logoView.setSmooth(true);
        logoView.setCache(true);

        VBox logoBox = new VBox(logoView);
        logoBox.setAlignment(Pos.CENTER);
        logoBox.setSpacing(10);

        // "관리메뉴" 라벨
        Label menuTitle = new Label("관리메뉴");
        menuTitle.getStyleClass().add("menu-Title");
        menuTitle.setAlignment(Pos.CENTER_LEFT);

        // 메뉴 항목
        Label mainLabel = createSidebarLabel("Main List", "icon-home.png");
        Label applyLabel = createSidebarLabel("입양 신청 관리", "icon-apply.png");
        Label reviewLabel = createSidebarLabel("후기 게시판 관리", "icon-review.png");
        Label pageLabel = createSidebarLabel("통계 페이지", "icon-stats.png");
        Label qnaLabel = createSidebarLabel("문의 관리", "icon-qna.png");
        
        mainLabel.setOnMouseClicked(e -> SceneManager.switchTo("mainList"));
        applyLabel.setOnMouseClicked(e -> SceneManager.switchTo("adoptionList"));

        // 로그아웃 버튼
        Button logoutBtn = new Button("로그아웃");
        logoutBtn.getStyleClass().add("logout-button");

        VBox topSection = new VBox(10, logoBox, menuTitle, mainLabel, applyLabel, reviewLabel, pageLabel, qnaLabel);
        topSection.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(topSection, Priority.ALWAYS);

        HBox bottomSection = new HBox(logoutBtn);
        bottomSection.setAlignment(Pos.BOTTOM_LEFT);
        bottomSection.setPadding(new Insets(10, 0, 0, 0));

        this.getChildren().addAll(topSection, bottomSection);
    }

    private Label createSidebarLabel(String text, String iconFileName) {
        ImageView icon = new ImageView(new Image(getClass().getResource("/icon/" + iconFileName).toExternalForm()));
        icon.setFitWidth(16);
        icon.setFitHeight(16);

        Label label = new Label(text, icon);
        label.getStyleClass().add("menu-Label");
        label.setAlignment(Pos.CENTER_LEFT);
        label.setContentDisplay(ContentDisplay.LEFT);
        label.setMaxWidth(Double.MAX_VALUE);

        VBox.setVgrow(label, Priority.ALWAYS);
        return label;
    }
}