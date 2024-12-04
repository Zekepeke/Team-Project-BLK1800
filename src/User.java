package src;

import interfaces.UserBased;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The User class represents a user of the app with properties like name, bio, password,
 * and lists of friends and blocked users. This class provides methods to manage friendships,
 * block/unblock users, and update user details.
 */
public class User implements UserBased {
    private static ArrayList<String> usernames = new ArrayList<>();
    private String name;
    private ArrayList<String> friends;
    private ArrayList<String> friendRequestsIn;
    private ArrayList<String> friendRequestsOut;
    private ArrayList<String> blocked;
    private String bio;
    private String password;
    /**
     * Reads in a user object from a database
     * Reads in the name, password, and all relevant information for the user
     * @param filename     The user's name.
     */
    public User(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new FileReader("USER_DATABASE/" + filename));) {
            this.name = br.readLine();
            this.password = br.readLine();
            this.bio = br.readLine();
            if (br.readLine() == null) {
                this.friends = null;
            } else {
                this.friends = new ArrayList<>(Arrays.asList(br.readLine().split(" ")));
            }
            if (br.readLine() == null) {
                this.friendRequestsIn = null;
            } else {
                this.friendRequestsIn = new ArrayList<>(Arrays.asList(br.readLine().split(" ")));
            }
            if (br.readLine() == null) {
                this.friendRequestsOut = null;
            } else {
                this.friendRequestsOut = new ArrayList<>(Arrays.asList(br.readLine().split(" ")));
            }
            if (br.readLine() == null) {
                this.blocked = null;
            } else {
                this.blocked = new ArrayList<>(Arrays.asList(br.readLine().split(" ")));
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }
    /**
     * Constructs a User with the specified name, bio, and password.
     * Initializes empty lists for friends and blocked users.
     *
     * @param name     The user's name.
     * @param bio      The user's bio
     * @param password The user's password.
     */
    public User(String name, String bio, String password) {
        this.name = name;
        usernames.add(name);
        try {
            File f = new File(name + ".txt");
            f.createNewFile();
            f.delete();
        } catch (Exception e) {
            this.name = "";
        }
        this.bio = bio;
        this.password = password;
        this.friends = new ArrayList<>();
        this.friendRequestsIn = new ArrayList<>();
        this.friendRequestsOut = new ArrayList<>();
        this.blocked = new ArrayList<>();
        usernames.add(name);
    }


    /**
     * Constructs a User with the specified name and password, and an empty bio.
     * Initializes empty lists for friends and blocked users.
     *
     * @param name     The user's name.
     * @param password The user's password.
     */
    public User(String name, String password) {
        this.name = name;
        try {
            File f = new File("USER_DATABASE/" + name + ".txt");
            f.createNewFile();
            PrintWriter pw = new PrintWriter(f);
            pw.println(name);
            pw.println(password);
            pw.println("Bio of " + name);
            pw.close();
        } catch (Exception e) {
            this.name = "";
        }
        this.bio = "";
        this.password = password;
        this.friends = new ArrayList<>();
        this.friendRequestsIn = new ArrayList<>();
        this.friendRequestsOut = new ArrayList<>();
        this.blocked = new ArrayList<>();
    }

    /**
     * Retrieves the user's name.
     *
     * @return The user's name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the user's name.
     *
     * @param name The new name for the user.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the list of friends for this user.
     *
     * @return An ArrayList of friends.
     */
    @Override
    public ArrayList<String> getFriends() {
        return friends;
    }

    /**
     * Sets the user's list of friends.
     *
     * @param friends An ArrayList of users who are friends with this user.
     */
    @Override
    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    /**
     * Retrieves the list of incoming friend requests.
     *
     * @return an ArrayList of {@code User} objects representing incoming friend requests.
     */
    @Override
    public ArrayList<String> getFriendRequestsIn() {
        return friendRequestsIn;
    }

    /**
     * Sets the list of incoming friend requests.
     *
     * @param friendRequestsIn an ArrayList of {@code User} objects to set as incoming friend requests.
     */
    @Override
    public void setFriendRequestsIn(ArrayList<String> friendRequestsIn) {
        this.friendRequestsIn = friendRequestsIn;
    }

    /**
     * Retrieves the list of outgoing friend requests.
     *
     * @return an ArrayList of {@code User} objects representing outgoing friend requests.
     */
    @Override
    public ArrayList<String> getFriendRequestsOut() {
        return friendRequestsOut;
    }

    /**
     * Sets the list of outgoing friend requests.
     *
     * @param friendRequestsOut an ArrayList of {@code User} objects to set as outgoing friend requests.
     */
    @Override
    public void setFriendRequestsOut(ArrayList<String> friendRequestsOut) {
        this.friendRequestsOut = friendRequestsOut;
    }

    /**
     * Retrieves the list of blocked users for this user.
     *
     * @return An ArrayList of blocked users.
     */
    @Override
    public ArrayList<String> getBlocked() {
        return blocked;
    }

    /**
     * Sets the user's list of blocked users.
     *
     * @param blocked An ArrayList of users who are blocked by this user.
     */
    @Override
    public void setBlocked(ArrayList<String> blocked) {
        this.blocked = blocked;
    }

    /**
     * Blocks the specified user by adding them to the blocked list.
     *
     * @param blockedUser The user to be blocked.
     */
    @Override
    public void block(User blockedUser) {
        blocked.add(blockedUser.getName());
    }

    /**
     * Unblocks the specified user if they are in the blocked list.
     *
     * @param unblocked The user to be unblocked.
     * @return true if the user was successfully unblocked, false otherwise.
     */
    @Override
    public boolean unblock(User unblocked) {
        if (blocked.contains(unblocked.getName())) {
            blocked.remove(unblocked.getName());
            return true;
        }
        return false;
    }

    /**
     * Retrieves the user's bio.
     *
     * @return The bio of the user.
     */
    @Override
    public String getBio() {
        return bio;
    }

    /**
     * Sets the user's bio.
     *
     * @param bio The new bio for the user, limited to 150 characters.
     */
    @Override
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * Retrieves the user's password.
     *
     * @return The user's password.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password The new password for the user.
     */
    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Accepts a friend request from the specified user if they are not already a friend or blocked.
     *
     * @param potentialFriend The user who is sending the friend request.
     * @return true if the friend request was accepted, false otherwise.
     */
    @Override
    public boolean acceptFriendRequest(User potentialFriend) {
        if (!friends.contains(potentialFriend.getName()) && !blocked.contains(potentialFriend.getName())
                && friendRequestsIn.contains(potentialFriend.getName())) {
            friends.add(potentialFriend.getName());
            return true;
        }
        return false;
    }

    /**
     * Sends a friend request to the specified user, adding them to the friend list if they are not already a friend
     * or blocked.
     *
     * @param potentialFriend The user to whom the friend request is sent.
     * @return true if the friend request was successfully sent, false if the user is already a friend or blocked.
     */
    @Override
    public boolean sendFriendRequest(User potentialFriend) {
        if (!friends.contains(potentialFriend.getName()) && !blocked.contains(potentialFriend.getName())) {
            friendRequestsOut.add(potentialFriend.getName());
            return true;
        }
        return false;
    }

    /**
     * Retrieves the number of friends this user has.
     *
     * @return The number of friends.
     */
    @Override
    public int getNumberOfFriends() {
        return friends.size();
    }

    /**
     * Removes a friend from the list
     *
     * @return true if the friend is removed, false if the friend isn't in the list
     */
    @Override
    public boolean removeFriend(User friend) {
        if (friends.contains(friend.getName())) {
            friends.remove(friend.getName());
            return true;
        }
        return false;
    }

    /**
     * Retrieves the number of blocked users for this user.
     *
     * @return The number of blocked users.
     */
    @Override
    public int getNumberOfBlocked() {
        return blocked.size();
    }

    @Override
    public String toString() {
        return name + "\n" +
                bio + "\n" +
                friends + "\n" +
                friendRequestsIn + "\n" +
                friendRequestsOut + "\n" +
                blocked;
    }

    /**
     * Pushes user.toString() to the database
     *
     * @return true if push was successful, false otherwise
     */
    @Override
    public boolean pushToDatabase() {
        try (PrintWriter p = new PrintWriter(new FileWriter(name + ".txt")); PrintWriter a = new PrintWriter(new FileWriter(USERS_LIST_PATH))) {
            synchronized (usernames) {
                p.println(this);
                a.println(this.name);
            }
        } catch (Exception e) {
            System.out.println("Bad IO Exception");
            return false;
        }
        return true;
    }

    /**
     * Retrieves all the stored users names within the database
     */
    public static ArrayList<String> storedUsers() {
        ArrayList<String> users = new ArrayList<String>();
        try(BufferedReader a = new BufferedReader(new FileReader(USERS_LIST_PATH))) {
            String input = null;
            while((input = a.readLine()) != null) {
                users.add(input);
            }
            return users;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Retrieves the number of blocked users for this user.
     *
     * @return The number of blocked users.
     */
    public static int getNumberUsers() {
        return usernames.size();
    }

    /**
     * Retrieves the number of blocked users for this user.
     *
     * @return The number of blocked users.
     */
    public static ArrayList<String> getUsernames() {
        return usernames;
    }

    public static boolean usernameExists(String username){
        return usernames.contains(username);
    }
}
