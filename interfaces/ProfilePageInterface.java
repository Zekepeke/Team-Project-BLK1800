package interfaces;

import javax.swing.*;

/**
 * Interface defining the essential behaviors and navigation methods for a profile page.
 * This ensures consistent functionality and reusability across different profile-related implementations.
 */
public interface ProfilePageInterface {

    /**
     * Navigates the user to the Friends page.
     * This method should replace the current panel with the Friends page.
     */
    void navigateToFriendsPage();

    /**
     * Navigates the user to the Friend Requests page.
     * This method should replace the current panel with the Friend Requests page.
     */
    void navigateToFriendRequestsPage();

    /**
     * Navigates the user to the Blocked Users page.
     * This method should replace the current panel with the Blocked Users page.
     */
    void navigateToBlockedUsersPage();

    /**
     * Navigates the user to the Search Users page.
     * This method should replace the current panel with the Search Users page.
     */
    void navigateToSearchUsersPage();



}
