package src.gui.pages.profile;

import interfaces.gui.CustomColors;
import src.User;
import src.client.ClientSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class profilePage extends JPanel implements CustomColors {

    private ClientSide client;
    private String username;
    private String bio;
    private int width;
    private int height;
    private String[] friends;
    private String[] friendRequests;
    private String[] blocked;
    private User user;

    Color backgroundColor = BACKGROUND;

    public profilePage(int width, int height, ClientSide client) {
        this.width = width;
        this.height = height;
        this.user = client.getUser();
        this.friends = this.user.getFriends().toArray(new String[0]);
        this.friendRequests = this.user.getFriendRequestsIn().toArray(new String[0]);
        this.blocked = this.user.getBlocked().toArray(new String[0]);
        setPreferredSize(new Dimension(width, height));
        this.client = client;

        // Fetch user details from the client
        this.username = client.getUsername();
        this.bio = this.user.getBio();
        System.out.println(this.user);
        System.out.println(this.bio);

        // Layout
        setLayout(new BorderLayout());
        setBackground(backgroundColor);

        // Profile Section
        JPanel profileSection = new JPanel();
        profileSection.setLayout(new BoxLayout(profileSection, BoxLayout.Y_AXIS));
        profileSection.setOpaque(false);

        // Username
        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 30));
        usernameLabel.setForeground(new Color(52, 152, 219)); // A blue color for the username
        usernameLabel.setAlignmentX(CENTER_ALIGNMENT);
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        // Bio
        JLabel bioLabel = new JLabel("<html><div style='text-align: center;'>" + bio + "</div></html>");
        bioLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        bioLabel.setForeground(new Color(44, 62, 80)); // A dark color for the bio
        bioLabel.setAlignmentX(CENTER_ALIGNMENT);
        bioLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // Add components to the profile section
        profileSection.add(usernameLabel);
        profileSection.add(bioLabel);

        // Add the profile section near the top
        add(profileSection, BorderLayout.NORTH);

        // Buttons Section
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 0)); // Three buttons in one row
        buttonPanel.setOpaque(false); // Make the panel transparent

        // Button 1 - Go to Friends Page
        JButton friendsButton = new JButton("Friends");
        friendsButton.setFont(new Font("Arial", Font.PLAIN, 16));
        friendsButton.setBackground(new Color(52, 152, 219)); // Blue background for the button
        friendsButton.setForeground(Color.WHITE);
        friendsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to navigate to the Friends page (example method call)
                navigateToFriendsPage();
            }
        });

        // Button 2 - Go to Friend Requests Page
        JButton friendRequestsButton = new JButton("Friend Requests");
        friendRequestsButton.setFont(new Font("Arial", Font.PLAIN, 16));
        friendRequestsButton.setBackground(new Color(46, 204, 113)); // Green background for the button
        friendRequestsButton.setForeground(Color.WHITE);
        friendRequestsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to navigate to the Friend Requests page (example method call)
                navigateToFriendRequestsPage();
            }
        });

        // Button 3 - Go to Blocked Users Page
        JButton blockedButton = new JButton("Blocked Users");
        blockedButton.setFont(new Font("Arial", Font.PLAIN, 16));
        blockedButton.setBackground(new Color(231, 76, 60)); // Red background for the button
        blockedButton.setForeground(Color.WHITE);
        blockedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to navigate to the Blocked Users page (example method call)
                navigateToBlockedUsersPage();
            }
        });

        // Button 4 - Go to Search Users Page
        JButton searchButton = new JButton("Search Users");
        searchButton.setFont(new Font("Arial", Font.PLAIN, 16));
        searchButton.setBackground(Color.CYAN); // Red background for the button
        searchButton.setForeground(Color.WHITE);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigateToSearchUsersPage();
            }
        });

        // Add buttons to the panel
        buttonPanel.add(friendsButton);
        buttonPanel.add(friendRequestsButton);
        buttonPanel.add(blockedButton);
        buttonPanel.add(searchButton);

        // Add the button panel below the profile section
        add(buttonPanel, BorderLayout.CENTER);
    }

    // Example method for navigating to the Friends page
    private void navigateToFriendsPage() {
        System.out.println("Navigating to Friends Page...");
        // Implement navigation logic (e.g., switch to another JPanel or load a new page)
    }

    // Example method for navigating to the Friend Requests page
    private void navigateToFriendRequestsPage() {
        System.out.println("Navigating to Friend Requests Page...");
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new FriendsPage(width, height, client));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    // Example method for navigating to the Blocked Users page
    private void navigateToBlockedUsersPage() {
        System.out.println("Navigating to Blocked Users Page...");
        // Implement navigation logic (e.g., switch to another JPanel or load a new page)
    }
    private void navigateToSearchUsersPage() {
        System.out.println("Navigating to Search Users Page...");
        // Implement navigation logic (e.g., switch to another JPanel or load a new page)
    }
}