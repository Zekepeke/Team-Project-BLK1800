package interfaces;
import src.User;

import java.util.Date;
public interface Messagable {
    // will have fields of user
    String toString();
    String getFileName();
    void setFileName(String fileName);
    void pushToDatabase();
    User getUser();
    void setUser(User user);
    Date getDate();
    void setDate(Date date);

}
