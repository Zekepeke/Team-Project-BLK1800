package src;
import interfaces.Messagable;

import java.util.*;


public class Message implements Messagable {
    User sender;
    User receiver;
    String fileName;
    Date date;
    public Message(User sender, User receiver, Date date) {
        this.sender = sender;
        this.receiver = receiver;
        this.date = date;

    }
    @Override
    public String getFileName() {
        return "";
    }

    @Override
    public void setFileName(String fileName) {

    }

    @Override
    public void pushToDatabase() {

    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public void setUser(User user) {

    }

    @Override
    public Date getDate() {
        return null;
    }

    @Override
    public void setDate(Date date) {

    }
}
