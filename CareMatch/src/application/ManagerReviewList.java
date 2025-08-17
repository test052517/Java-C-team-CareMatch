package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import application.ui.ReviewListView;
import application.ui.SidebarView;

public class ManagerReviewList {

    private boolean isLikesAsc = false;
    private boolean isDateAsc = true;
    private boolean isBestFirst = false;

    public Pane getView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");
        Font.loadFont(getClass().getResource("/Pretendard-Medium.ttf").toExternalForm(), 12);
		Font.loadFont(getClass().getResource("/Pretendard-Bold.ttf").toExternalForm(), 12);
		Font.loadFont(getClass().getResource("/Pretendard-ExtraBold.ttf").toExternalForm(), 12);
		
		// Sidebar 불러오기 (클래스 분리)
        root.setLeft(new SidebarView());
        
        // 샘플 리뷰 리스트 준비
	    List<Review> reviews = sampleReviews();
        
        // AnimalList 불러오기 (클래스 분리)
        ReviewListView reviewListView = new ReviewListView(reviews);

        ScrollPane scrollPane = new ScrollPane(reviewListView);
        scrollPane.setContent(reviewListView);
        scrollPane.setFitToWidth(true);
        
	    // 상단 헤더
	    HBox header = new HBox();
	    header.setPadding(new Insets(10));
	    header.setAlignment(Pos.CENTER_LEFT);
	    header.setSpacing(10);
	    
        // 제목
        Label title = new Label("후기 게시판 관리");
        title.getStyleClass().add("title");
        
        // 제목 왼쪽, 버튼 오른쪽으로 배치
	    Region spacer = new Region();
	    HBox.setHgrow(spacer, Priority.ALWAYS);

	    header.getChildren().addAll(title, spacer);
	    
	    // 정렬 바 (버튼들)
	    HBox sortBar = new HBox(20);
	    sortBar.setPrefWidth(600);
	    sortBar.setAlignment(Pos.CENTER);
	    
	    Label sortDate = createSortLabel("작성 일자");
        Label sortLikes = createSortLabel("좋아요 수");
        Label sortBest = createSortLabel("우수 후기");
	    
        List<Label> allLabels = Arrays.asList(sortDate, sortLikes, sortBest);
        
        for (Label lbl : allLabels) {
            lbl.setMaxWidth(Double.MAX_VALUE);
            lbl.setAlignment(Pos.CENTER);
            HBox.setHgrow(lbl, Priority.ALWAYS);
            lbl.getStyleClass().add("sort-label");
        }
        
        sortBar.getChildren().addAll(allLabels);
        
        // 감싸는 패딩 박스
	    HBox sortBarContainer = new HBox(sortBar);
	    sortBarContainer.setPadding(new Insets(8));
	    sortBarContainer.getStyleClass().add("sort-bar-container");
	    sortBarContainer.setPrefWidth(620);
        
	    // 정렬 이벤트
        sortDate.setOnMouseClicked(e -> {
            isDateAsc = !isDateAsc;
            sortDate.setText("작성 일자 " + (isDateAsc ? "↑" : "↓"));
            reviews.sort(Comparator.comparing(Review::getId)); // 샘플에 날짜 없으므로 ID 기준
            if (!isDateAsc) {
                reviews.sort((a, b) -> Integer.compare(b.getId(), a.getId()));
            }
            reviewListView.updateList(reviews);
        });

        sortLikes.setOnMouseClicked(e -> {
            isLikesAsc = !isLikesAsc;
            sortLikes.setText("좋아요 수 " + (isLikesAsc ? "↑" : "↓"));
            reviews.sort((a, b) -> isLikesAsc
                ? Integer.compare(a.getLikes(), b.getLikes())
                : Integer.compare(b.getLikes(), a.getLikes()));
            reviewListView.updateList(reviews);
        });

        sortBest.setOnMouseClicked(e -> {
            isBestFirst = !isBestFirst;
            sortBest.setText("우수 후기 " + (isBestFirst ? "↑" : "↓"));
            reviews.sort((a, b) -> Boolean.compare(b.isBest(), a.isBest()));
            if (!isBestFirst) {
                reviews.sort((a, b) -> Boolean.compare(a.isBest(), b.isBest()));
            }
            reviewListView.updateList(reviews);
        });

        // centerPane 생성 및 위 요소들 조립
        VBox centerPane = new VBox(10);
        centerPane.setPadding(new Insets(20));
        centerPane.getChildren().addAll(header, sortBarContainer, scrollPane);

        root.setCenter(centerPane);

        return root;
    }
    
    private List<Review> sampleReviews() {
	    List<Review> list = new ArrayList<>();
            list.add(new Review(1, "글 제목", "김OO", "동물명", "2024-01-01", "후기 내용", 123, true, null, null));
            list.add(new Review(2, "글 제목", "김OO", "동물명", "2024-01-01", "후기 내용", 99, true, null, null));
            list.add(new Review(3, "글 제목", "김OO", "동물명", "2024-01-01", "후기 내용", 9, false, null, null));
            list.add(new Review(4, "글 제목", "김OO", "동물명", "2024-01-01", "후기 내용", 25, false, null, null));
            list.add(new Review(5, "글 제목", "김OO", "동물명", "2024-01-01", "후기 내용", 17, false, null, null));
            list.add(new Review(6, "글 제목", "김OO", "동물명", "2024-01-01", "후기 내용", 39, false, null, null));
            list.add(new Review(7, "글 제목", "김OO", "동물명", "2024-01-01", "후기 내용", 14, false, null, null));
            
            return list;
    }
    
	private Label createSortLabel(String text) {
	    Label label = new Label(text);
	    label.getStyleClass().add("sort-label");
	    label.setStyle("-fx-cursor: hand;"); 
	    return label;
	}
}