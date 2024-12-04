package interfaces;

import javax.swing.*;
import java.awt.*;

/**
 * Interface defining the contract for FriendRequestsPage functionality.
 * This interface encapsulates methods for handling friend requests,
 * user interactions, and page navigation.
 */
public interface FriendRequestsPageInterface {

    /**
     * Creates a styled JButton with specified text and background color.
     *
     * @param text           The text to be displayed on the button.
     * @param backgroundColor The background color of the button.
     * @return A styled JButton instance.
     */
    JButton createButton(String text, Color backgroundColor);

    /**
     * Navigates the user back to the profile page.
     * This method replaces the current panel with the profile page.
     */
    void returnHome();

    /**
     * Handles the removal of a friend request.
     * Removes the specified friend from the list of incoming friend requests,
     * updates the database, and refreshes the page.
     *
     * @param friend The name of the friend to be removed.
     */
    void removeFriend(String friend);

    /**
     * Accepts a friend request by adding the user to the friend list,
     * removing them from incoming friend requests, and updating the database.
     *
     * @param friend The name of the friend to be added.
     */
    void addFriend(String friend);

    /**
     * Opens the profile page of the specified friend.
     * Implement the logic for viewing the friend's profile.
     *
     * @param friend The name of the friend whose profile will be viewed.
     */
    void viewProfile(String friend);
}
