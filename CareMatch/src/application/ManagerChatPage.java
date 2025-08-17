package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import application.ui.ChatListView;
import application.ui.SidebarView;

public class ManagerChatPage {

    private boolean isDateAsc = true;

    private ChatListView chatListView;
    private List<Chat> chats;

    public Pane getView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        Font.loadFont(getClass().getResource("/Pretendard-Medium.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/Pretendard-Bold.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/Pretendard-ExtraBold.ttf").toExternalForm(), 12);

        root.setLeft(new SidebarView());

        chats = sampleChats();
        chatListView = new ChatListView(chats);

        ScrollPane scrollPane = new ScrollPane(chatListView);
        scrollPane.setFitToWidth(true);

        VBox centerPane = new VBox(10);
        centerPane.setPadding(new Insets(20));
        centerPane.getChildren().addAll(createHeader(), createSortBar(), scrollPane);
        
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        root.setCenter(centerPane);

        return root;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(10));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(10);

        Label title = new Label("문의 관리");
        title.getStyleClass().add("title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(title, spacer);
        return header;
    }

    private HBox createSortBar() {
        HBox sortBar = new HBox(20);
        sortBar.setPrefWidth(600);
        sortBar.setAlignment(Pos.CENTER);

        Label sortDateLabel = new Label("문의 일자");
        sortDateLabel.getStyleClass().add("sort-label");
        sortBar.getChildren().add(sortDateLabel);

        // 클릭 이벤트 처리
        sortDateLabel.setOnMouseClicked(e -> {
            isDateAsc = !isDateAsc;
            sortDateLabel.setText("문의 일자 " + (isDateAsc ? "↑" : "↓"));
            sortChatsByDate();
            chatListView.updateList(chats);
        });

        HBox container = new HBox(sortBar);
        container.setPadding(new Insets(8));
        container.getStyleClass().add("sort-bar-container");
        container.setPrefWidth(620);

        return container;
    }

    private void sortChatsByDate() {
        chats.sort((a, b) -> isDateAsc
                ? a.getCreatedDate().compareTo(b.getCreatedDate())
                : b.getCreatedDate().compareTo(a.getCreatedDate()));
    }

    private List<Chat> sampleChats() {
        List<Chat> list = new ArrayList<>();
        list.add(new Chat("test1234", LocalDate.of(2025, 8, 7)));
        list.add(new Chat("user5678", LocalDate.of(2025, 8, 5)));
        list.add(new Chat("helloWorld", LocalDate.of(2025, 7, 29)));
        return list;
    }

    public static class Chat {
        private String userId;
        private LocalDate createdDate;

        public Chat(String userId, LocalDate createdDate) {
            this.userId = userId;
            this.createdDate = createdDate;
        }

        public String getUserId() {
            return userId;
        }

        public LocalDate getCreatedDate() {
            return createdDate;
        }
    }
}