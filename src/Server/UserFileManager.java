package src.Server;

import java.io.*;
import java.util.ArrayList;

public class UserFileManager {
    private static final String fileName = "USER_DATABASE/stored_users.txt";
    private static BufferedReader reader;
    private static PrintWriter writer;

    public static ArrayList<String> usernames;

    public static boolean initialize() {
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String input = "";

            try {
                while ((input = reader.readLine()) != null) {
                    if (!usernames.contains(input)) {
                        usernames.add(input);
                    }
                }
                reader.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public static boolean writeNewUser(String name) {
        try {
            writer = new PrintWriter(new FileWriter(fileName, true));
            if(usernames.contains(name)) {return false;}
            usernames.add(name);
            writer.println(name);
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
