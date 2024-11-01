package interfaces;

import java.util.ArrayList;
/**
 * UserBased
 *
 * This is a placeholder for the users of the app.
 * Each user has a name, list of friends/blocked, bio,
 * password, accepted or declined friend request, send friend request,
 * and number of friends.
 *
 *
 * @author Esequiel Linares
 *
 * @version 10/31/24
 *
 */
public interface UserBased {

    // Users name for the app should be lowercase letters or numbers and without specials charcters
    public String getName();
    public void setName(String name);

    // A list of friends of this user
    public ArrayList<String> getFriends();
    public void setFriends(ArrayList<String> friends);


    // An ArrayList of blocked users made by this user
    public ArrayList<String> getBlocked();
    public void setBlocked(ArrayList<String> blocked);

    // Bio's should be less than or equal to 150 characters
    public String getBio();
    public void setBio(String bio);

    // Can be any password no restrictions
    public String getPassword();
    public void setPassword(String password);

    // This is for accepting or declining a friendRequestMade from another user
    public boolean acceptFriendRequest();

    // Sends a friend request to a potential new friend
    public boolean sendFriendRequest(String potentialFriend);


    // Should return the number of friends
    public int getNumberOfFriends();

    public void setNumberOfFriends(int numberOfFriends);






}