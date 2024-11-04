package src;

import interfaces.UserBased;
import java.util.ArrayList;
import java.io.*;
/**
 * The User class represents a user of the app with properties like name, bio, password,
 * and lists of friends and blocked users. This class provides methods to manage friendships,
 * block/unblock users, and update user details.
 */
public class User implements UserBased {
    private static ArrayList<String> usernames = new ArrayList<>();
    private String name;
    private ArrayList<User> friends;
    private ArrayList<User> friendRequestsIn;
    private ArrayList<User> friendRequestsOut;
    private ArrayList<User> blocked;
    private String bio;
    private String password;

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
        if (usernames.contains(name)) {
            System.out.println("Username is already in use");
            this.name = null;
        }
        usernames.add(name);
        try {
            File f = new File(name + ".txt");
            f.createNewFile();
            f.delete();
        } catch (Exception e){
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
            File f = new File(name + ".txt");
            f.createNewFile();
            f.delete();
        } catch (Exception e){
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
    public ArrayList<User> getFriends() {
        return friends;
    }

    /**
     * Sets the user's list of friends.
     *
     * @param friends An ArrayList of users who are friends with this user.
     */
    @Override
    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }
    /**
     * Retrieves the list of incoming friend requests.
     *
     * @return an ArrayList of {@code User} objects representing incoming friend requests.
     */
    @Override
    public ArrayList<User> getFriendRequestsIn() {
        return friendRequestsIn;
    }

    /**
     * Sets the list of incoming friend requests.
     *
     * @param friendRequestsIn an ArrayList of {@code User} objects to set as incoming friend requests.
     */
    @Override
    public void setFriendRequestsIn(ArrayList<User> friendRequestsIn) {
        this.friendRequestsIn = friendRequestsIn;
    }

    /**
     * Retrieves the list of outgoing friend requests.
     *
     * @return an ArrayList of {@code User} objects representing outgoing friend requests.
     */
    @Override
    public ArrayList<User> getFriendRequestsOut() {
        return friendRequestsOut;
    }

    /**
     * Sets the list of outgoing friend requests.
     *
     * @param friendRequestsOut an ArrayList of {@code User} objects to set as outgoing friend requests.
     */
    @Override
    public void setFriendRequestsOut(ArrayList<User> friendRequestsOut) {
        this.friendRequestsOut = friendRequestsOut;
    }
    /**
     * Retrieves the list of blocked users for this user.
     *
     * @return An ArrayList of blocked users.
     */
    @Override
    public ArrayList<User> getBlocked() {
        return blocked;
    }

    /**
     * Sets the user's list of blocked users.
     *
     * @param blocked An ArrayList of users who are blocked by this user.
     */
    @Override
    public void setBlocked(ArrayList<User> blocked) {
        this.blocked = blocked;
    }

    /**
     * Blocks the specified user by adding them to the blocked list.
     *
     * @param blockedUser The user to be blocked.
     */
    @Override
    public void block(User blockedUser) {
        blocked.add(blockedUser);
    }

    /**
     * Unblocks the specified user if they are in the blocked list.
     *
     * @param unblocked The user to be unblocked.
     * @return true if the user was successfully unblocked, false otherwise.
     */
    @Override
    public boolean unblock(User unblocked) {
        if (blocked.contains(unblocked)) {
            blocked.remove(unblocked);
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
        if (!friends.contains(potentialFriend) && !blocked.contains(potentialFriend) && friendRequestsIn.contains(potentialFriend)) {
            friends.add(potentialFriend);
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
        if (!friends.contains(potentialFriend) && !blocked.contains(potentialFriend)) {
            friendRequestsOut.add(potentialFriend);
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
     * @return true if push was successful, false otherwise
     */
    @Override
    public boolean pushToDatabase() {
        try(PrintWriter p = new PrintWriter(new FileWriter(name + ".txt"))){
            p.println(this.toString());
        } catch (Exception e) {
            System.out.println("Bad IO Exception");
            return false;
        }
        return true;
    }

    /**
     * Retrieves the number of blocked users for this user.
     *
     * @return The number of blocked users.
     */
    @Override
    public int getNumberUsers() {
        return usernames.size();
    }


}
