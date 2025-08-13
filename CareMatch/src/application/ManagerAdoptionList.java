package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.ui.SidebarView;

public class ManagerAdoptionList {
	
	// 정렬 상태 저장
	private boolean isApplyDateAsc = true;
	private boolean isVisitDateAsc = true;
	private int statusFilterIndex = -1; 

	private static final List<String> statusOrder = Arrays.asList(
	    "승인 미완료", "입양 승인", "방문 예약", "입양 완료"
	);

	// 실제 입양 신청 리스트
	List<Adoption> applications; // 필드 선언만
	VBox applicationListView = new VBox(10); // 리스트 뷰 박스

    public Pane getView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");
		Font.loadFont(getClass().getResource("/Pretendard-Medium.ttf").toExternalForm(), 12);
		Font.loadFont(getClass().getResource("/Pretendard-Bold.ttf").toExternalForm(), 12);
		Font.loadFont(getClass().getResource("/Pretendard-ExtraBold.ttf").toExternalForm(), 12);
		
		// Sidebar 불러오기 (클래스 분리)
		root.setLeft(new SidebarView());
	
	    // 스크롤 패널
	    ScrollPane scrollPane = new ScrollPane();
	    scrollPane.setContent(applicationListView);
	    scrollPane.setFitToWidth(true);

	    // 상단 헤더 (제목 + 등록 버튼)
	    HBox header = new HBox();
	    header.setPadding(new Insets(10));
	    header.setAlignment(Pos.CENTER_LEFT);
	    header.setSpacing(10);

	    // 제목
	    Label title = new Label("입양 신청 관리");
	    title.getStyleClass().add("title");

	    // 제목 왼쪽, 버튼 오른쪽으로 배치
	    Region spacer = new Region();
	    HBox.setHgrow(spacer, Priority.ALWAYS);

	    header.getChildren().addAll(title, spacer);
	    
	    applications = sampleApplications();

	    // 정렬 바 (버튼들)
	    HBox sortBar = new HBox(20);
	    sortBar.setPrefWidth(600);
	    sortBar.setAlignment(Pos.CENTER);

	    Label sortApplyDate = createSortLabel("신청 일자");
	    Label sortVisitDate = createSortLabel("방문 일자");
	    Label sortStatus = createSortLabel("진행 상태");

	    List<Label> allLabels = Arrays.asList(sortApplyDate, sortVisitDate, sortStatus);
	    
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
	    sortApplyDate.setOnMouseClicked(e -> {
	        isApplyDateAsc = !isApplyDateAsc;
	        sortApplyDate.setText("신청 일자 " + (isApplyDateAsc ? "↑" : "↓"));
	        applications.sort((a, b) -> {
	            // 날짜 파싱은 아래 참고
	            return isApplyDateAsc
	                ? a.getApplyDate().compareTo(b.getApplyDate())
	                : b.getApplyDate().compareTo(a.getApplyDate());
	        });
	        updateApplicationList(applications); 
	    });

	    sortVisitDate.setOnMouseClicked(e -> {
	        isVisitDateAsc = !isVisitDateAsc;
	        sortVisitDate.setText("방문 일자 " + (isVisitDateAsc ? "↑" : "↓"));
	        applications.sort((a, b) -> {
	            return isVisitDateAsc
	                ? a.getVisitDate().compareTo(b.getVisitDate())
	                : b.getVisitDate().compareTo(a.getVisitDate());
	        });
	        updateApplicationList(applications);
	    });

	    sortStatus.setOnMouseClicked(e -> {
	        statusFilterIndex++;
	        if (statusFilterIndex >= statusOrder.size()) {
	            statusFilterIndex = -1; 
	        }

	        String label = (statusFilterIndex == -1)
	            ? "전체 보기"
	            : statusOrder.get(statusFilterIndex);

	        sortStatus.setText("진행 상태: " + label);

	        updateApplicationList(applications);
	    });

	    updateApplicationList(applications); // 리스트 초기 출력

	    // centerPane 생성 및 위 요소들 조립
	    VBox centerPane = new VBox(10);
	    centerPane.setPadding(new Insets(20));
	    centerPane.getChildren().addAll(header, sortBarContainer, scrollPane);

	    root.setCenter(centerPane);

		return root;
	}

    // 샘플 데이터
    private List<Adoption> sampleApplications() {
        return new ArrayList<>(List.of(
            new Adoption("코코", "홍길동", "입양 완료", "2024-04-01", "2024-04-10"),
            new Adoption("루비", "김영희", "방문 예약", "2024-06-03", "2024-06-15"),
            new Adoption("보리", "이철수", "입양 승인", "2024-05-01", "2024-05-09"),
            new Adoption("하늘", "박민수", "승인 미완료", "2024-07-01", "2024-07-12"),
            new Adoption("test", "test", "입양완료", "2024-08-01", "2024-08-12")
        ));
    }

	private Label createSortLabel(String text) {
	    Label label = new Label(text);
	    label.getStyleClass().add("sort-label");
	    label.setStyle("-fx-cursor: hand;"); 
	    return label;
	}
	
	private void updateApplicationList(List<Adoption> allApps) {
	    applicationListView.getChildren().clear();

	    for (Adoption app : allApps) {
	        if (statusFilterIndex == -1 || app.getStatus().equals(statusOrder.get(statusFilterIndex))) {
	            applicationListView.getChildren().add(new AdoptionCard(app));
	        }
	    }
	}

    public static void main(String[] args) {
    	
    }
}