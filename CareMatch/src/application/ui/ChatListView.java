package application.ui;

import javafx.scene.layout.VBox;
import java.util.List;

import application.ChatCard;
import application.ManagerChatPage.Chat;

public class ChatListView extends VBox {
    public ChatListView(List<Chat> chats) {
        super(10);
        this.getStyleClass().add("chat-list");
        updateList(chats);
    }

    public void updateList(List<Chat> chats) {
        this.getChildren().clear();
        for (Chat chat : chats) {
            this.getChildren().add(new ChatCard(chat)); 
        }
    }
}