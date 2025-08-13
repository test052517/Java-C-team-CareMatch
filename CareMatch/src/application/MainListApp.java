package application;

import java.net.URL;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainListApp extends Application {

	public Stage stage;
    @Override
    public void start(Stage stage) {
    	this.stage=stage;
        // 전체 레이아웃: HBox - 좌측 메뉴, 우측 콘텐츠
        HBox root = new HBox();

        // 좌측 메뉴 VBox
        VBox sideMenu = new VBox(20);
        sideMenu.setId("sideMenu");
        sideMenu.setPadding(new Insets(30));
        sideMenu.setPrefWidth(180);
        // 메인 콘텐츠 1020*800
        sideMenu.setFillWidth(true);
        sideMenu.setPrefWidth(250);

        // 로고
        ImageView logo = new ImageView(new Image("file:src/application/menu_icon/Logo1.png"));
        logo.setFitWidth(100);
        logo.setFitHeight(105);
        logo.setPreserveRatio(true);

       // Label logoLabel = new Label("CareMatch");
     //   logoLabel.setId("logoLabel");

        VBox logoBox = new VBox(5, logo);
        logoBox.setAlignment(Pos.CENTER);

        // 메뉴 아이템들
     // 메뉴 항목 라벨 생성
        Label menu1 = new Label("입양 목록");
        menu1.setPrefWidth(20);
        Label menu2 = new Label("입양 후기");
        Label menu3 = new Label("마이 페이지");
        Label menu4 = new Label("관리자 문의하기");
        Label menu5 = new Label("채팅 문의하기");
        Label menu6 = new Label("입양신청 확인");
        Label menu7 = new Label("방문예약 하기");
        
        
        //메뉴 아이콘

        ImageView view = new ImageView(new Image("file:src/application/menu_icon/home.png"));
        view.setFitHeight(17);
        view.setFitWidth(17);
        
        ImageView view1 = new ImageView(new Image("file:src/application/menu_icon/smile.png"));
        view1.setFitHeight(17);
        view1.setFitWidth(17);
        
        ImageView view2 = new ImageView(new Image("file:src/application/menu_icon/Group3.png"));
    //    view2.setFitHeight(20);
    //    view2.setFitWidth(20);
        
        ImageView view3 = new ImageView(new Image("file:src/application/menu_icon/smile.png"));
        view3.setFitHeight(17);
        view3.setFitWidth(17);
        
        ImageView view4 = new ImageView(new Image("file:src/application/menu_icon/smile.png"));
        view4.setFitHeight(17);
        view4.setFitWidth(17);
        
        ImageView view5 = new ImageView(new Image("file:src/application/menu_icon/downright.png"));
        view5.setFitHeight(11);
        view5.setFitWidth(12);
        
        ImageView view6 = new ImageView(new Image("file:src/application/menu_icon/downright.png"));

        
        
        //라벨에 아이콘 넣기
        menu1.setGraphic(view);
        menu1.setContentDisplay(ContentDisplay.LEFT);
        
        menu2.setGraphic(view1);
        menu2.setContentDisplay(ContentDisplay.LEFT);
        
        menu3.setGraphic(view2);
        menu3.setContentDisplay(ContentDisplay.LEFT);
        
        menu4.setGraphic(view3);
        menu4.setContentDisplay(ContentDisplay.LEFT);
        
        menu5.setGraphic(view4);
        menu5.setContentDisplay(ContentDisplay.LEFT);
        
        menu6.setGraphic(view5);
        menu6.setContentDisplay(ContentDisplay.LEFT);
        
        menu7.setGraphic(view6);
        menu7.setContentDisplay(ContentDisplay.LEFT);
        
        
        
        // 공통 스타일 클래스 지정
        menu1.getStyleClass().add("menuItem");
        menu2.getStyleClass().add("menuItem");
        menu3.getStyleClass().add("menuItem");
        menu4.getStyleClass().add("menuItem");
        menu5.getStyleClass().add("menuItem");
        menu6.getStyleClass().add("menuItem");
        menu7.getStyleClass().add("menuItem");
        
        menu1.setMaxWidth(Double.MAX_VALUE);
        menu2.setMaxWidth(Double.MAX_VALUE);
        menu3.setMaxWidth(Double.MAX_VALUE);
        menu4.setMaxWidth(Double.MAX_VALUE);
        menu5.setMaxWidth(Double.MAX_VALUE);
        menu6.setMaxWidth(Double.MAX_VALUE);
        menu7.setMaxWidth(Double.MAX_VALUE);
        
        menu1.setPrefHeight(30);
        menu2.setPrefHeight(30);
        menu3.setPrefHeight(30);
        menu4.setPrefHeight(30);
        menu5.setPrefHeight(30);
        menu6.setPrefHeight(20);
        menu7.setPrefHeight(20);
        
        HBox h1 = new HBox(menu6);
        HBox h2 = new HBox(menu7);
        h1.setAlignment(Pos.CENTER_RIGHT);
        h2.setAlignment(Pos.CENTER_RIGHT);
        h1.setPrefWidth(130);
        h2.setPrefWidth(130);
        h1.setManaged(false);
        h2.setManaged(false);
        
        menu6.setVisible(false);
        menu6.setManaged(false);
        
        menu7.setVisible(false);
        menu7.setManaged(false);

        // 메뉴 라벨 리스트로 관리
        List<Label> menuLabels = List.of(menu1, menu2, menu3,menu6,menu7, menu4, menu5);
        
        
        // 선택된 라벨 스타일 유지용
        final Label[] selectedLabel = {menu1}; // 기본 선택



        // 로그아웃 버튼
        Button logoutBtn = new Button("로그아웃");
        logoutBtn.setId("logoutBtn");
        
        //로그아웃 버튼 기능
        logoutBtn.setOnAction(event->{
        	Main m = new Main();
        	m.start(stage);
        });

        // 메뉴 추가
        sideMenu.getChildren().addAll(logoBox, menu1, menu2, menu3,h1,h2, menu4, menu5, logoutBtn);

        // 우측 메인 콘텐츠 VBox
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30, 40, 30, 40));
        mainContent.setPrefWidth(1100);

        // 타이틀
        Label title = new Label("입양 가능한 동물 리스트");
        title.setId("titleLabel");
        
        Label title2 = new Label("후기 게시판");
        title2.setId("titleLabel");
       

        
        // 검색 박스 HBox
        HBox searchBox = new HBox(10);
        searchBox.setId("searchBox");
        searchBox.setAlignment(Pos.CENTER_RIGHT);
        

        TextField searchField = new TextField();
        searchField.setPromptText("분양글 검색");
        searchField.setPrefWidth(200);
        
        //동물 종류 관리
        ComboBox<String> animalType = new ComboBox<>();
        animalType.getItems().addAll("강아지", "고양이", "기타");
        animalType.getSelectionModel().selectFirst();
        
        animalType.setBackground(Background.fill(Color.web("fff")));
        
        //품종관리
        ComboBox<String> breedType = new ComboBox<>();
        breedType.getItems().addAll("품종");
        breedType.getSelectionModel().selectFirst();
        
        breedType.setBackground(Background.fill(Color.web("fff")));
        
        HBox searchBox2 = new HBox(10);
        searchBox2.setId("searchBox");
        searchBox2.setAlignment(Pos.CENTER_RIGHT);
        

        TextField searchField2 = new TextField();
        searchField2.setPromptText("리뷰 검색");
        searchField2.setPrefWidth(200);
        
        //동물 종류 관리
        ComboBox<String> animalType2 = new ComboBox<>();
        animalType2.getItems().addAll("강아지", "고양이", "기타");
        animalType2.getSelectionModel().selectFirst();
        
        animalType2.setBackground(Background.fill(Color.web("fff")));
        
        //품종관리
        ComboBox<String> breedType2 = new ComboBox<>();
        breedType2.getItems().addAll("품종");
        breedType2.getSelectionModel().selectFirst();
        
        breedType2.setBackground(Background.fill(Color.web("fff")));
        
        
        searchBox.getChildren().addAll(searchField, animalType, breedType);
        searchBox2.getChildren().addAll(searchField2, animalType2, breedType2);
        
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.BOTTOM_LEFT);
        topBar.setSpacing(10);
        topBar.setPrefWidth(Double.MAX_VALUE);
        
        HBox topBar2 = new HBox();
        topBar2.setAlignment(Pos.BOTTOM_LEFT);
        topBar2.setSpacing(10);
        topBar2.setPrefWidth(Double.MAX_VALUE);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.getChildren().addAll(title,spacer,searchBox);
        
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        topBar2.getChildren().addAll(title2,spacer2,searchBox2);
        
        
       
        // 동물 리스트 GridPane
        GridPane animalGrid = new GridPane();
        animalGrid.setHgap(40);
        animalGrid.setVgap(20);
        animalGrid.setFocusTraversable(false);//focus 테두리 없애는 메서드 작동x 거슬리면 수정 필요.
        animalGrid.setPadding(new Insets(10));
        animalGrid.setId("animalGrid");
        animalGrid.setBackground(Background.fill(Color.web("fff")));
      //  animalGrid.setBorder(Border.);

        // 동물 카드 8개 예시
        for (int i = 0; i < 12; i++) {
            VBox card = createAnimalCard("이름 " + (i+1), "♀", 2);
            animalGrid.add(card, i % 4, i / 4);
        }

        // 스크롤 가능하게
        ScrollPane scrollPane = new ScrollPane(animalGrid);
        scrollPane.setFitToWidth(true);
       
        
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setId("scrollPane");

        mainContent.getChildren().addAll( topBar, scrollPane);

        root.getChildren().addAll(sideMenu, mainContent);
        
        // 이벤트 등록
        for (Label label : menuLabels) {
            // 기본 선택 상태
            if (label == selectedLabel[0]) {
                label.getStyleClass().add("menuItem-selected");
            }

            // 클릭 이벤트
            label.setOnMouseClicked(event -> {
                // 이전 선택된 항목 스타일 제거
                selectedLabel[0].getStyleClass().remove("menuItem-selected");

                // 현재 클릭한 항목 선택 처리
                label.getStyleClass().add("menuItem-selected");
                selectedLabel[0] = label;
                
                // 👉 여기에 원하는 동작 처리 (화면 전환 등)
                System.out.println("선택된 메뉴: " + label.getText());
                if(label==menu1) {
                	mainContent.getChildren().clear();
                	mainContent.getChildren().addAll(topBar,scrollPane);
                }
                if(label==menu2) {
                    mainContent.getChildren().clear();
                    Button reviewbut = new Button("후기 작성");
                    reviewbut.setPrefWidth(150);
                    // 제목 및 검색 상단 바는 유지
                    mainContent.getChildren().add(topBar2);

                    // ReviewPageView에서 스크롤 콘텐츠만 받아서 붙이기
                    ReviewPageView rpv = new ReviewPageView();
                    ScrollPane rescrollPane = rpv.getContent();
                    rescrollPane.setId("scrollPane");
                    VBox.setVgrow(rescrollPane, Priority.ALWAYS); // 중요!
                    mainContent.getChildren().add(rescrollPane);
                    HBox butBox  = new HBox(reviewbut);
                    butBox.setAlignment(Pos.BOTTOM_RIGHT);
                    mainContent.getChildren().add(butBox);
                    
                    reviewbut.setOnAction(e ->{
                    	ReviewWriteForm rwf = new ReviewWriteForm();
                    	rwf.show(stage);
                    });
                    
                }

                if(label==menu3||label==menu6||label==menu7) {
                	//System.out.println("마이페이지");
                	if(label==menu3) {
                		//메인 콘텐츠 마이페이지 이동
                		mainContent.getChildren().clear();
                		mainContent.getChildren().add(MyPageView.getContent());
                	}
                	menu6.setManaged(true);
                	menu6.setVisible(true);
                	menu7.setManaged(true);
                	menu7.setVisible(true);
                	h1.setManaged(true);
                	h2.setManaged(true);
                	
                	if(label==menu6) {
                		AdoptionCheckForm.show(stage);
                	}
                	
                	if(label==menu7) {
                		VisitReservationForm vrf = new VisitReservationForm();
                		vrf.showAndWait();
                	}
                }else {
                	h1.setManaged(false);
                	h2.setManaged(false);
                	menu6.setVisible(false);
                	menu6.setManaged(false);
                	menu7.setVisible(false);
                	menu7.setManaged(false);
                }
                
            });

            // 마우스 오버 효과
            label.setOnMouseEntered(e -> label.setStyle("-fx-background-color: #FFFCF5;"));
            label.setOnMouseExited(e -> label.setStyle(""));
        }

        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("CareMatch - 입양 가능한 동물 리스트");
        stage.show();
    }

    // 동물 카드 만들기 메서드
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
        
        card.setOnMouseClicked(event ->{
        	System.out.println("선택된 동물: "+name);
            AnimalDetailView adv = new AnimalDetailView();
            adv.start(stage);
        });

        return card;
    }

    public static void main(String[] args) {
        launch();
    }
}
