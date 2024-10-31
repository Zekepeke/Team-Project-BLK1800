package interfaces;
import src.User;

import java.util.Date;
public interface Messagable {
    String MESSAGE_SEP = "Hyvp1VlujMNPHY9nySd25fxaluLUrI";
    String CONVO_END = "NIIztzeaegR12UzH1ra01BhKjQbHMH";
    // will have fields of user
    String toString();
    String getFileName();
    void setFileName(String fileName);
    void pushToDatabase();
    User getSender();
    void setSender(User user);
    User getReceiver();
    void setReceiver(User user);
    Date getDate();
    void setDate(Date date);

}
