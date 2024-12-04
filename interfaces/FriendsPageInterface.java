package interfaces;

import javax.swing.*;
import java.awt.*;

/**
 * Interface defining the contract for FriendsPage functionality.
 * Encapsulates methods for managing friends, user interactions,
 * and navigation.
 */
public interface FriendsPageInterface {

    /**
     * Creates a styled JButton with specified text and background color.
     *
     * @param text            The text to be displayed on the button.
     * @param backgroundColor The background color of the button.
     * @return A styled JButton instance.
     */
    JButton createButton(String text, Color backgroundColor);

    /**
     * Navigates the user back to the profile page.
     * Replaces the current panel with the profile page.
     */
    void returnHome();

    /**
     * Removes a friend from the user's friend list.
     * Updates the database and refreshes the page.
     *
     * @param friend The name of the friend to be removed.
     */
    void removeFriend(String friend);

    /**
     * Blocks a friend and removes them from the user's friend list.
     * Updates the database and refreshes the page.
     *
     * @param friend The name of the friend to be blocked.
     */
    void blockFriend(String friend);

    /**
     * Opens the profile page of the specified friend.
     * Implement the logic for viewing the friend's profile.
     *
     * @param friend The name of the friend whose profile will be viewed.
     */
    void viewProfile(String friend);
}
