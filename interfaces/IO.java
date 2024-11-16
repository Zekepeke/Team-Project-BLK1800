package interfaces;
import java.io.InputStream;
import java.io.OutputStream;

public interface IO {
    String DELIMITER_START = "DELIMITER_START";
    String DELIMITER_END = "DELIMITER_END";
    String SPLITTER = "SPLITTER";
    String MESSAGE_SPLITTER = "MESSAGE_SPLITTER";

    public static final String TYPE_FRIEND_INFO_UPDATE = "TYPE_BYTE_FRIEND_INFO_UPDATE";
    String TYPE_MESSAGE = "TYPE_MESSAGE";
    String TYPE_SIGNUP = "TYPE_SIGNUP";
    String TYPE_LOGIN = "TYPE_LOGIN";
    String TYPE_HAND_SHAKE = "TYPE_HAND_SHAKE";
    String TYPE_LIST_FRIENDS = "TYPE_LIST_FRIENDS";

    String ERROR_USER_ALREADY_ACTIVE = "ERROR_USER_ALREADY_ACTIVE";
    //usersignup informatics
    String SUCCESS_USER_SIGNUP = "SUCCESS_USER_SIGNUP";
    String ERROR_USER_EXISTS = "ERROR_USER_EXISTS";
    String TYPE_USER_SIGNUP_INFORMATION = "TYPE_USER_SIGNUP_INFORMATION";

    //user login informatics
    String SUCCESS_USER_LOGIN = "SUCCESS_USER_LOGIN";
    String ERROR_USER_DNE = "ERROR_USER_DNE";
    String ERROR_PASSWORD = "ERROR_PASSWORD";
    String TYPE_CHANGE_RECIPIENT = "TYPE_CHANGE_RECIPIENT";

    String TYPE_FRIEND_LIST  = "TYPE_FRIEND_LIST";
    String TYPE_USER_LIST_SEARCH  = "TYPE_USER_LIST_SEARCH";
    String TYPE_FRIEND_CONVERSATION_HISTORY  = "TYPE_FRIEND_CONVERSATION_HISTORY";
    String TYPE_USER_INFORMATION  = "TYPE_USER_INFORMATION";

    
    boolean write(String[] stream, String informationType);

    String[] read();

    boolean setStreamReader(InputStream stream);
    boolean setStreamWriter(OutputStream stream);
    OutputStream getStreamWriter();
    InputStream getStreamReader();
}
