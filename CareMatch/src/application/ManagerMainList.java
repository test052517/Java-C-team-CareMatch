package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.stage.Modality;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.ui.AnimalListView;
import application.ui.SidebarView;

public class ManagerMainList {
	
	private boolean isTypeAsc = true;
	private boolean isAgeAsc = true;
	private boolean isWeightAsc = true;
	private boolean isGenderAsc = true;
	private boolean isNeuteredAsc = true;
	private boolean isDateAsc = true;
	private String status; // 상태: "치료중", "보호중", "종료(반환)", "종료(입양)"
	
	public Pane getView() {
		BorderPane root = new BorderPane();
		root.getStyleClass().add("root");
		Font.loadFont(getClass().getResource("/Pretendard-Medium.ttf").toExternalForm(), 12);
		Font.loadFont(getClass().getResource("/Pretendard-Bold.ttf").toExternalForm(), 12);
		Font.loadFont(getClass().getResource("/Pretendard-ExtraBold.ttf").toExternalForm(), 12);
		
		// Sidebar 불러오기 (클래스 분리)
		root.setLeft(new SidebarView());
		
		 // 샘플 동물 리스트 준비
	    List<Animal> animals = sampleAnimals();
	
	    // AnimalList 불러오기 (클래스 분리)
	    AnimalListView animalListView = new AnimalListView(animals);

	    // 스크롤 패널
	    ScrollPane scrollPane = new ScrollPane();
	    scrollPane.setContent(animalListView);
	    scrollPane.setFitToWidth(true);

	    // 상단 헤더 (제목 + 등록 버튼)
	    HBox header = new HBox();
	    header.setPadding(new Insets(10));
	    header.setAlignment(Pos.CENTER_LEFT);
	    header.setSpacing(10);

	    // 제목
	    Label title = new Label("입양 동물 목록");
	    title.getStyleClass().add("title");

	    // 버튼
	    Button addBtn = new Button("동물 추가");
	    addBtn.getStyleClass().add("register-button");

	    // 제목 왼쪽, 버튼 오른쪽으로 배치
	    Region spacer = new Region();
	    HBox.setHgrow(spacer, Priority.ALWAYS);

	    header.getChildren().addAll(title, spacer, addBtn);

	    // 정렬 바 (버튼들)
	    HBox sortBar = new HBox(20);
	    sortBar.setPrefWidth(600);
	    sortBar.setAlignment(Pos.CENTER);

	    Label sortType = createSortLabel("종");
	    Label sortAge = createSortLabel("나이");
	    Label sortWeight = createSortLabel("무게");
	    Label sortGender = new Label("성별");
	    Label sortNeutered = new Label("중성화");
	    Label sortDate = new Label("입소일");

	    List<Label> allLabels = Arrays.asList(sortType, sortAge, sortWeight,
	    		sortGender, sortNeutered, sortDate);
	    
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
	    
	    // 정렬 버튼 동작 추가
	    sortType.setOnMouseClicked(e -> {
	        isTypeAsc = !isTypeAsc;
	        sortType.setText("종 " + (isTypeAsc ? "↑" : "↓"));
	        animals.sort((a, b) -> isTypeAsc ?
	            a.getType().compareTo(b.getType()) : b.getType().compareTo(a.getType()));
	        animalListView.updateList(animals);
	    });

	    sortAge.setOnMouseClicked(e -> {
	        isAgeAsc = !isAgeAsc;
	        sortAge.setText("나이 " + (isAgeAsc ? "↑" : "↓"));
	        animals.sort((a, b) -> isAgeAsc ?
	            Integer.compare(a.getAge(), b.getAge()) : Integer.compare(b.getAge(), a.getAge()));
	        animalListView.updateList(animals);
	    });

	    sortWeight.setOnMouseClicked(e -> {
	        isWeightAsc = !isWeightAsc;
	        sortWeight.setText("무게 " + (isWeightAsc ? "↑" : "↓"));
	        animals.sort((a, b) -> isWeightAsc ?
	            Double.compare(a.getWeight(), b.getWeight()) : Double.compare(b.getWeight(), a.getWeight()));
	        animalListView.updateList(animals);
	    });
	    
	    sortGender.setOnMouseClicked(e -> {
	        isGenderAsc = !isGenderAsc;
	        sortGender.setText("성별 " + (isGenderAsc ? "♂" : "♀"));
	        animals.sort((a, b) -> {
	            // 성별은 "남" 또는 "여" 같은 문자열
	            // 오름차순이면 a < b, 내림차순이면 반대로
	            int cmp = a.getGender().compareTo(b.getGender());
	            return isGenderAsc ? cmp : -cmp;
	        });
	        animalListView.updateList(animals);
	    });

	    sortNeutered.setOnMouseClicked(e -> {
	        isNeuteredAsc = !isNeuteredAsc;
	        sortNeutered.setText("중성화 " + (isNeuteredAsc ? "O" : "X"));
	        animals.sort((a, b) -> {
	            // boolean은 false < true 임.
	            // true를 1, false를 0으로 바꾸고 비교
	            int cmp = Boolean.compare(a.isNeutered(), b.isNeutered());
	            return isNeuteredAsc ? cmp : -cmp;
	        });
	        animalListView.updateList(animals);
	    });

	    sortDate.setOnMouseClicked(e -> {
	        isDateAsc = !isDateAsc;
	        sortDate.setText("입소일 " + (isDateAsc ? "↑" : "↓"));
	        animals.sort((a, b) -> isDateAsc ?
	            a.getAdmissionDate().compareTo(b.getAdmissionDate()) : b.getAdmissionDate().compareTo(a.getAdmissionDate()));
	        animalListView.updateList(animals);
	    });
	    
	    addBtn.setOnAction(e -> {
	        Stage formStage = new Stage();
	        formStage.initModality(Modality.APPLICATION_MODAL);
	        
	        ManagerDataRegistrationForm form = new ManagerDataRegistrationForm();
	        form.show(formStage);
	    }); 

	    // centerPane 생성 및 위 요소들 조립
	    VBox centerPane = new VBox(10);
	    centerPane.setPadding(new Insets(20));
	    centerPane.getChildren().addAll(header, sortBarContainer, scrollPane);

	    root.setCenter(centerPane);

		return root;
	}

	private List<Animal> sampleAnimals() {
	    List<Animal> list = new ArrayList<>();

	    list.add(new Animal("코코", "강아지", 3, 5.2, "여", true, "2023-02-15"));
	    list.add(new Animal("루비", "고양이", 2, 3.1, "남", false, "2024-01-20"));
	    list.add(new Animal("벨라", "강아지", 1, 4.0, "여", true, "2023-11-03"));
	    list.add(new Animal("마루", "고양이", 5, 3.5, "남", false, "2022-07-18"));
	    list.add(new Animal("초코", "강아지", 4, 6.0, "남", true, "2023-05-12"));
	    list.add(new Animal("미미", "고양이", 3, 2.9, "여", true, "2024-04-01"));
	    list.add(new Animal("보리", "강아지", 2, 5.7, "남", false, "2023-09-10"));
	    list.add(new Animal("하늘", "고양이", 6, 3.2, "여", true, "2022-12-30"));

	    return list;
	}
	
	private Label createSortLabel(String text) {
	    Label label = new Label(text);
	    label.getStyleClass().add("sort-label");
	    label.setStyle("-fx-cursor: hand;"); 
	    return label;
	}
	
	public static void main(String[] args) {
		
	}
}