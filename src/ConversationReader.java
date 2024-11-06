package src;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import interfaces.Messagable;
import interfaces.UserBased;

import static interfaces.Messagable.CONVO_END;
import static interfaces.Messagable.MESSAGE_SEP;


public class ConversationReader implements src.interfaces.ConversationReaderInterface {
    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
    private ArrayList<Message> messages;
    public ConversationReader(String user1, String user2) {
        boolean endConvo = false;
        messages = new ArrayList<>();
        String fileName;
        if (user1.compareTo(user2) < 0) {
            fileName = user1 + "-" + user2;
        } else {
            fileName = user2 + "-" + user1;
        }
        fileName = "MESSAGE_DATABASE" + "/" + fileName + ".txt";
        try (BufferedReader b = new BufferedReader(new FileReader(fileName));){
            User sender = new User(user1 + ".txt");
            User receiver = new User(user2 + ".txt");
            b.readLine();
            while (true) {
                String s = b.readLine();
                if (s == null) {
                    s = "";
                }
                if (s.equals(CONVO_END) || endConvo) {
                    break;
                }
                Date date = formatter.parse(s);
                s = b.readLine();
                if (s.equals(user1)) {
                    sender = new User(user1 + ".txt");
                    receiver = new User(user2 + ".txt");
                } else if (s.equals(user2)) {
                    receiver = new User(user1 + ".txt");
                    sender = new User(user2 + ".txt");
                }
                String content = "";
                while (true) {
                    s = b.readLine();
                    if (s == null) {
                        s = "";
                    }
                    System.out.println("Thing: " + s);
                    if (s.equals(CONVO_END)) {
                        endConvo = true;
                        messages.add(new Message(sender, receiver, date, content));
                        break;
                    } else if (s.equals(MESSAGE_SEP)) {
                        messages.add(new Message(sender, receiver, date, content));
                        break;
                    }
                    if (content.equals("")){
                        content = s;
                    } else{
                        content += "\n" + s;
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Bad Parse!");
        }
    }



    public ArrayList<Message> getMessages() {
        return messages;
    }
    public void addMessage(Message message) {
        messages.add(message);
    }
    public boolean removeMessage(Message message) {
        return messages.remove(message);
    }
    public static void main(String[] args){
        ConversationReader conv = new ConversationReader("Alice", "Bob");
        System.out.println(conv.getMessages().get(0));
        System.out.println(conv.getMessages().get(1));
    }
}


