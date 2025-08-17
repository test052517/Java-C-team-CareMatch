package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Window;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import javafx.scene.image.Image;
import javafx.util.StringConverter;

import application.ui.SidebarView;

public class ManagerStatsPage {

    public Pane getView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");
        Font.loadFont(getClass().getResource("/Pretendard-Medium.ttf").toExternalForm(), 12);
		Font.loadFont(getClass().getResource("/Pretendard-Bold.ttf").toExternalForm(), 12);
		Font.loadFont(getClass().getResource("/Pretendard-ExtraBold.ttf").toExternalForm(), 12);

		// Sidebar 불러오기 (클래스 분리)
        root.setLeft(new SidebarView());

        // 상단 헤더
	    HBox header = new HBox();
	    header.setPadding(new Insets(10));
	    header.setAlignment(Pos.CENTER_LEFT);
	    header.setSpacing(10);
	    
        // 제목
        Label title = new Label("통계 페이지");
        title.getStyleClass().add("title");
        
     // 제목 왼쪽, 버튼 오른쪽으로 배치
	    Region spacer = new Region();
	    HBox.setHgrow(spacer, Priority.ALWAYS);

	    header.getChildren().addAll(title, spacer);

        // 통계 차트 구역 3개
        VBox statsContainer = new VBox(15);
        statsContainer.setPadding(new Insets(10));
        statsContainer.getChildren().addAll(
                createChartBox("월별 입양률 통계"),
                createChartBox("동물 수용률 통계"),
                createChartBox("방문자 통계")
        );

        VBox center = new VBox(5);
        center.setPadding(new Insets(20));
        center.getChildren().addAll(header, statsContainer);

        root.setCenter(center);

        return root;
    }

    private VBox createChartBox(String titleText) {
        VBox chartBox = new VBox();
        chartBox.getStyleClass().add("chart-Box");
        chartBox.setSpacing(5);
        chartBox.setPadding(new Insets(0));
        chartBox.setMaxWidth(Double.MAX_VALUE);
        chartBox.setPrefWidth(Double.MAX_VALUE);
        VBox.setVgrow(chartBox, Priority.ALWAYS);

        HBox titleBar = new HBox();
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new Insets(8, 12, 8, 12));
        titleBar.getStyleClass().add("title-Bar");
        titleBar.setMaxWidth(Double.MAX_VALUE);

        final Label title = new Label();
        title.getStyleClass().add("sub-section-title");

        // 달력 아이콘 버튼 생성
        ImageView calendarIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/icon-date.png")));
        calendarIcon.setFitWidth(20);
        calendarIcon.setFitHeight(20);

        Button calendarButton = new Button();
        calendarButton.setGraphic(calendarIcon);
        calendarButton.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        calendarButton.setFocusTraversable(false);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // 차트 생성 및 참조 저장 (차트 갱신에 필요)
        LineChart<String, Number> chart = createSampleChart();
        chart.setPrefHeight(200);
        chart.setLegendVisible(false);
        chart.setMaxWidth(Double.MAX_VALUE);
        chart.setPrefWidth(Double.MAX_VALUE);

        // 날짜 선택 팝업용 DatePicker 생성 (월 단위만 선택하도록 세팅)
        DatePicker datePicker = new DatePicker(LocalDate.now());
        // DatePicker 기본은 일 단위 선택이므로, 커스텀 포맷으로 월만 보이도록 변환
        datePicker.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            
            public String toString(LocalDate date) {
                if (date == null) return "";
                return formatter.format(date);
            }
            
            public LocalDate fromString(String string) {
                if (string == null || string.isEmpty()) return null;
                return LocalDate.parse(string + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        });

        // DatePicker 팝업 띄우는 팝업 창 설정
        Popup popup = new Popup();
        popup.getContent().add(datePicker);
        popup.setAutoHide(true);

        calendarButton.setOnAction(e -> {
            Window window = calendarButton.getScene().getWindow();
            double x = window.getX() + calendarButton.localToScene(0, 0).getX() + calendarButton.getScene().getX();
            double y = window.getY() + calendarButton.localToScene(0, 0).getY() + calendarButton.getScene().getY() + calendarButton.getHeight();
            popup.show(window, x, y);
        });

        // 날짜 선택 시 차트 데이터 갱신 (가상 데이터로 예시)
        datePicker.setOnAction(e -> {
            LocalDate selectedDate = datePicker.getValue();
            popup.hide();
            updateChartData(chart, selectedDate);
            title.setText(titleText + " (" + selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM")) + ")");
        });

        LocalDate now = LocalDate.now();
        updateChartData(chart, now);
        title.setText(titleText + " (" + now.format(DateTimeFormatter.ofPattern("yyyy-MM")) + ")");

        titleBar.getChildren().addAll(title, spacer, calendarButton);
        HBox.setMargin(calendarIcon, new Insets(0, 0, 0, 10));
        chartBox.getChildren().addAll(titleBar, chart);
        return chartBox;
    }

    // 가상의 데이터로 차트 갱신하는 예시 함수
    private void updateChartData(LineChart<String, Number> chart, LocalDate selectedDate) {
        chart.getData().clear();

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("입양률");

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("비율");

        // 월에 따라 임의 데이터 생성 (실제 DB 쿼리 대신)
        int month = selectedDate.getMonthValue();

        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        for (int i = 0; i < months.length; i++) {
            double value1 = 100 + Math.sin((i + month) / 2.0) * 50 + 100;
            double value2 = 30 + Math.cos((i + month) / 3.0) * 40;
            series1.getData().add(new XYChart.Data<>(months[i], value1));
            series2.getData().add(new XYChart.Data<>(months[i], value2));
        }

        chart.getData().addAll(series1, series2);
    }
    
    private LineChart<String, Number> createSampleChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("월");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("수치");

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setAnimated(false);
        lineChart.setLegendVisible(true);

        // 초기 데이터 넣기 (빈 데이터 혹은 기본 데이터)
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("기본 데이터1");

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("기본 데이터2");

        lineChart.getData().addAll(series1, series2);

        return lineChart;
    }
}