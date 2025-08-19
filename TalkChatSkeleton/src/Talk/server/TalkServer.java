package Talk.server;

import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

public class TalkServer {
    public static final int PORT = 9192;

    public static final ConcurrentHashMap<Integer, ClientHandler> SESSIONS = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        System.out.println("[TalkServer] starting on port " + PORT);
        try (ServerSocket ss = new ServerSocket(PORT)) {
            while (true) {
                Socket s = ss.accept();
                s.setTcpNoDelay(true);
                System.out.println("[ACCEPT] client connected: " + s.getRemoteSocketAddress());
                new ClientHandler(s).start();
            }
        }
    }
}
