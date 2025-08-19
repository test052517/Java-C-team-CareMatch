package Talk.common;

public final class Protocol {
    // Auth
    public static final String LOGIN = "LOGIN";
    public static final String LOGIN_OK = "LOGIN_OK";
    public static final String LOGIN_FAIL = "LOGIN_FAIL";

    // Rooms
    public static final String ENTER = "ENTER";
    public static final String ENTER_OK = "ENTER_OK";

    // Chat
    public static final String SAY = "SAY";            // SAY|room_id|text
    public static final String IMAGE = "IMAGE";        // IMAGE|room_id|filename|base64
    public static final String MSG = "MSG";            // MSG|msg_id|room_id|sender_id|sender_name|content_type|payload|created_at
    public static final String HISTORY = "HISTORY";    // HISTORY|room_id|after_message_id
    public static final String BYE = "BYE";

    // Generic error line (some handlers use it)
    public static final String ERROR = "ERROR";

    // ---- Legacy/compat (compile-only) ----
    public static final String SAY_IMAGE = "SAY_IMAGE";
    public static final String READ = "READ";
    public static final String LIST_ROOMS = "LIST_ROOMS";
    public static final String ROOMS = "ROOMS";
    public static final String HISTORY_BEGIN = "HISTORY_BEGIN";
    public static final String HISTORY_END = "HISTORY_END";
    public static final String MSG_IMAGE = "MSG_IMAGE";
    public static final String READ_ACK = "READ_ACK";
}
