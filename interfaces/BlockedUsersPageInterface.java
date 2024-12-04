package interfaces;

import javax.swing.*;
import java.awt.*;

/**
 * Interface defining the contract for BlockedUsersPage functionality.
 * Encapsulates methods for managing blocked users, user interactions,
 * and navigation.
 */
public interface BlockedUsersPageInterface {

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
     * Handles the unblocking of a user.
     * Removes the specified user from the blocked list, updates the database,
     * and refreshes the page.
     *
     * @param blockedUser The name of the user to be unblocked.
     */
    void unblockUser(String blockedUser);

    /**
     * Opens the profile page of the specified blocked user.
     * Implement the logic for viewing the user's profile.
     *
     * @param blockedUser The name of the blocked user whose profile will be viewed.
     */
    void viewProfile(String blockedUser);
}
