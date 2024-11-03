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

    private String name;
    private ArrayList<User> friends;
    private ArrayList<User> blocked;
    private String bio;
    private String password;

    /**
     * Constructs a User with the specified name, bio, and password.
     * Initializes empty lists for friends and blocked users.
     *
     * @param name     The user's name.
     * @param bio      The user's bio, limited to 150 characters.
     * @param password The user's password.
     */
    public User(String name, String bio, String password) {
        this.name = name;
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
        this.blocked = new ArrayList<>();
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
        this.blocked = new ArrayList<>();
    }

    /**
     * Retrieves the user's name.
     *
     * @return The user's name.
     */
    @Override
    public synchronized String getName() {
        return name;
    }

    /**
     * Sets the user's name.
     *
     * @param name The new name for the user.
     */
    @Override
    public synchronized void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the list of friends for this user.
     *
     * @return An ArrayList of friends.
     */
    @Override
    public synchronized ArrayList<User> getFriends() {
        return friends;
    }

    /**
     * Sets the user's list of friends.
     *
     * @param friends An ArrayList of users who are friends with this user.
     */
    @Override
    public synchronized void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    /**
     * Retrieves the list of blocked users for this user.
     *
     * @return An ArrayList of blocked users.
     */
    @Override
    public synchronized ArrayList<User> getBlocked() {
        return blocked;
    }

    /**
     * Sets the user's list of blocked users.
     *
     * @param blocked An ArrayList of users who are blocked by this user.
     */
    @Override
    public synchronized void setBlocked(ArrayList<User> blocked) {
        this.blocked = blocked;
    }

    /**
     * Blocks the specified user by adding them to the blocked list.
     *
     * @param blockedUser The user to be blocked.
     */
    @Override
    public synchronized void block(User blockedUser) {
        blocked.add(blockedUser);
    }

    /**
     * Unblocks the specified user if they are in the blocked list.
     *
     * @param unblocked The user to be unblocked.
     * @return true if the user was successfully unblocked, false otherwise.
     */
    @Override
    public synchronized boolean unblock(User unblocked) {
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
    public synchronized String getBio() {
        return bio;
    }

    /**
     * Sets the user's bio.
     *
     * @param bio The new bio for the user, limited to 150 characters.
     */
    @Override
    public synchronized void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * Retrieves the user's password.
     *
     * @return The user's password.
     */
    @Override
    public synchronized String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password The new password for the user.
     */
    @Override
    public synchronized void setPassword(String password) {
        this.password = password;
    }

    /**
     * Accepts a friend request from the specified user if they are not already a friend or blocked.
     *
     * @param potentialFriend The user who is sending the friend request.
     * @return true if the friend request was accepted, false otherwise.
     */
    @Override
    public synchronized boolean acceptFriendRequest(User potentialFriend) {
        if (!friends.contains(potentialFriend) && !blocked.contains(potentialFriend)) {
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
    public synchronized boolean sendFriendRequest(User potentialFriend) {
        if (!friends.contains(potentialFriend) && !blocked.contains(potentialFriend)) {
            friends.add(potentialFriend);
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
    public synchronized int getNumberOfFriends() {
        return friends.size();
    }

    /**
     * Retrieves the number of blocked users for this user.
     *
     * @return The number of blocked users.
     */
    @Override
    public synchronized int getNumberOfBlocked() {
        return blocked.size();
    }
}
