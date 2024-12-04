package interfaces;
import java.io.InputStream;
import java.io.OutputStream;

public interface IO {

    String DELIMITER_START = "DELIMITER_START";
    String DELIMITER_END = "DELIMITER_END";
    String SPLITTER = "SPLITTER";
    String MESSAGE_SPLITTER = "MESSAGE_SPLITTER";

    String TYPE_FRIEND_INFO_UPDATE = "TYPE_BYTE_FRIEND_INFO_UPDATE";
    String TYPE_MESSAGE = "TYPE_MESSAGE";
    String TYPE_SIGNUP = "TYPE_SIGNUP";
    String TYPE_LOGIN = "TYPE_LOGIN";
    String TYPE_HAND_SHAKE = "TYPE_HAND_SHAKE";

    String ERROR_USER_ALREADY_ACTIVE = "ERROR_USER_ALREADY_ACTIVE";

    // user signup informatics
    String SUCCESS_USER_SIGNUP = "SUCCESS_USER_SIGNUP";
    String ERROR_USER_EXISTS = "ERROR_USER_EXISTS";
    String TYPE_USER_SIGNUP_INFORMATION = "TYPE_USER_SIGNUP_INFORMATION";

    // User login informatics
    String SUCCESS_USER_LOGIN = "SUCCESS_USER_LOGIN";
    String ERROR_USER_DNE = "ERROR_USER_DNE";
    String ERROR_PASSWORD = "ERROR_PASSWORD";
    String TYPE_CHANGE_RECIPIENT = "TYPE_CHANGE_RECIPIENT";

    String TYPE_FRIEND_LIST  = "TYPE_FRIEND_LIST";
    String TYPE_USER_LIST_SEARCH  = "TYPE_USER_LIST_SEARCH";
    String TYPE_FRIEND_CONVERSATION_HISTORY  = "TYPE_FRIEND_CONVERSATION_HISTORY";
    String TYPE_USER_INFORMATION  = "TYPE_USER_INFORMATION";

    // Inter client communication informatics
    String ERROR_GENERAL = "ERROR_GENERAL";
    String ERROR_IO_EXCEPTION = "ERROR_IO_EXCEPTION";
    String SUCCESS_GENERAL = "SUCCESS_GENERAL";
    String TYPE_UPDATE_BLOCKED_USERS = "TYPE_BLOCKED_USERS";
    String TYPE_SEND_FRIEND_REQUEST = "TYPE_SEND_FRIEND_REQUEST";
    String TYPE_ACCEPT_FRIEND_REQUEST = "TYPE_ACCEPT_FRIEND_REQUEST";
    String TYPE_GET_INCOMING_FRIEND_REQUESTS = "TYPE_GET_INCOMING_FRIEND_REQUESTS";
    String TYPE_GET_OUTGOING_FRIEND_REQUESTS = "TYPE_GET_OUTGOING_FRIEND_REQUESTS";
    String TYPE_GET_FRIEND_LIST = "TYPE_GET_OUTGOING_FRIEND_REQUESTS";

    boolean write(String[] stream, String informationType);

    String[] read();

    boolean setStreamReader(InputStream stream);
    boolean setStreamWriter(OutputStream stream);
    OutputStream getStreamWriter();
    InputStream getStreamReader();
}
