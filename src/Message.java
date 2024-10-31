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
        if (sender.getName().compareTo(receiver.getName()) < 0) {
            fileName = sender.getName() + "@" + receiver.getName();
        } else {
            fileName = receiver.getName() + "@" + sender.getName();
        }
    }
    public Message(){

    }

    /**
     * @return The file path that the message is stored within
     */
    @Override
    public String getFileName() {
        return fileName;
    }

    /**
     * sets the filename (path) of the place where the message is stored to a new filename
     */
    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Pushes the message to the file of [filename] with the proper separators in place
     *
     */
    @Override
    public void pushToDatabase() {

    }

    /**
     * @return the User object representing the sender of the message
     */
    @Override
    public User getSender() {
        return this.sender;
    }

    /**
     * @param user
     */
    @Override
    public void setSender(User user) {
        this.sender = user;
    }

    /**
     * @return the receiver who received the message
     */
    @Override
    public User getReceiver() {
        return this.receiver;
    }

    /**
     * @param user sets the receiver to a new user
     */
    @Override
    public void setReceiver(User user) {
        this.receiver = user;
    }

    /**
     * @return date of the message
     */
    @Override
    public Date getDate() {
        return date;
    }

    /**
     * @param date sets the date that the message was sent to custom date
     */
    @Override
    public void setDate(Date date) {
        this.date = date;
    }

}