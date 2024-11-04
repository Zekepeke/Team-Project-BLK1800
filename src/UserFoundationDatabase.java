package src;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class UserFoundationDatabase {
    String username;
    String password;
    User thisUser;
    public UserFoundationDatabase(String username) {
        try{
            File f = new File(username + ".txt");
            if (!f.exists()){
                System.out.println("Bad User - User does not exist");
            } else{
                BufferedReader b = new BufferedReader(new FileReader(username + ".txt"));
                b.readLine();
                String s = b.readLine();
                if (s.equals(password)){
                    this.username = username;
                    this.thisUser = new User(username, password, b.readLine());
                    ArrayList<String> a = (ArrayList<String>) Arrays.asList(b.readLine().split(" "));
                    this.thisUser.setFriends(a);

                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
