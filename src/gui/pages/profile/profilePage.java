package src.gui.pages.profile;

import interfaces.ProfilePageInterface;
import interfaces.gui.CustomColors;
import interfaces.gui.ProfileInterface;
import src.SocketIO;
import src.User;
import src.client.ClientSide;
import src.gui.MessagingPage;
import src.gui.pages.auth.AuthenticationPages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class profilePage extends JPanel implements CustomColors, ProfilePageInterface, ProfileInterface {

    private ClientSide client;
    private String username;
    private String bio;
    private int width;
    private int height;
    private String[] friends;
    private String[] friendRequests;
    private String[] blocked;

    private JTextArea bioTextArea; // For editing the bio
    private JButton saveBioButton; // Save button for bio changes

    Color backgroundColor = BACKGROUND;

    public profilePage(int width, int height, ClientSide client) {
        this.width = width;
        this.height = height;

        // Initialize fields
        this.username = ClientSide.username;
        this.bio = ClientSide.bio;

        // Set layout and background
        setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());
        setBackground(backgroundColor);
        setOpaque(true);

        // Profile Section
        JPanel profileSection = new JPanel();
        profileSection.setLayout(new BoxLayout(profileSection, BoxLayout.Y_AXIS));
        profileSection.setOpaque(false);

        // Username
        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 30));
        usernameLabel.setForeground(BLUE_150);
        usernameLabel.setAlignmentX(CENTER_ALIGNMENT);
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        // Editable Bio Section
        bioTextArea = new JTextArea(bio);
        bioTextArea.setFont(new Font("Arial", Font.ITALIC, 18));
        bioTextArea.setForeground(new Color(44, 62, 80));
        bioTextArea.setLineWrap(true);
        bioTextArea.setWrapStyleWord(true);
        bioTextArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        bioTextArea.setAlignmentX(CENTER_ALIGNMENT);
        bioTextArea.setBackground(Color.WHITE);

        // Save Button
        saveBioButton = new JButton("Save Bio");
        saveBioButton.setFont(new Font("Arial", Font.PLAIN, 16));
        saveBioButton.setBackground(GREEN_100);
        saveBioButton.setForeground(Color.WHITE);
        saveBioButton.setAlignmentX(CENTER_ALIGNMENT);
        saveBioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveBio();
            }
        });

        // Add components to the profile section
        profileSection.add(usernameLabel);
        profileSection.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        profileSection.add(bioTextArea);
        profileSection.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        profileSection.add(saveBioButton);

        // Add profile section to the top
        add(profileSection, BorderLayout.NORTH);

        // Additional Buttons Section
        add(createButtonPanel(), BorderLayout.CENTER);
    }

    // Method to save the updated bio
    private void saveBio() {
        String updatedBio = bioTextArea.getText().trim();
        if (!updatedBio.isEmpty() && !updatedBio.equals(bio)) {
            bio = updatedBio;
            ClientSide.command(bio, SocketIO.TYPE_UPDATE_USER_BIO); // Update the local User object
            JOptionPane.showMessageDialog(this, "Bio updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No changes made to the bio.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Create the button panel
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5, 10, 0));
        buttonPanel.setOpaque(false);

        // Friends Button
        JButton friendsButton = createNavButton("Friends", BLUE_150, e -> navigateToFriendsPage());

        // Friend Requests Button
        JButton friendRequestsButton = createNavButton("Friend Requests", GREEN_100, e -> navigateToFriendRequestsPage());

        // Blocked Users Button
        JButton blockedButton = createNavButton("Blocked Users", RED_150, e -> navigateToBlockedUsersPage());

        // Search Users Button
        JButton searchButton = createNavButton("Search Users", Color.CYAN, e -> navigateToSearchUsersPage());

        JButton logoutButton = createNavButton("Logout", Color.BLACK, e -> logout());


        JButton chatButton = createNavButton("Chat! :D", Color.LIGHT_GRAY, e -> chat());

        buttonPanel.add(friendsButton);
        buttonPanel.add(friendRequestsButton);
        buttonPanel.add(blockedButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(chatButton);



        return buttonPanel;
    }

    // Helper method to create navigation buttons
    private JButton createNavButton(String text, Color bgColor, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.addActionListener(actionListener);
        return button;
    }



public void navigateToFriendsPage() {
        System.out.println("Navigating to Friends Page...");
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new FriendsPage(width, height, client));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    public void navigateToFriendRequestsPage() {
        System.out.println("Navigating to Friend Requests Page...");
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new FriendRequestsPage(width, height, client));
        parentFrame.revalidate();
        parentFrame.repaint();

    }

    public void navigateToBlockedUsersPage() {
        System.out.println("Navigating to Blocked Users Page...");
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new BlockedUsersPage(width, height, client));
        parentFrame.revalidate();
        parentFrame.repaint();
    }
    public void navigateToSearchUsersPage() {
        System.out.println("Navigating to Search Users Page...");
        // Implement navigation logic (e.g., switch to another JPanel or load a new page)
    }
    public void logout()  {
        try {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            parentFrame.getContentPane().removeAll();
            parentFrame.add(new AuthenticationPages(width, height, client));
            parentFrame.revalidate();
            parentFrame.repaint();
        } catch (Exception e) {
            System.out.println("OH NO!");
        }
    }

    public void chat() {
        try {
            System.out.println("Navigating to Blocked Users Page...");
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            parentFrame.getContentPane().removeAll();
            parentFrame.add(new MessagingPage(width, height, new User("alice.txt")));
            parentFrame.revalidate();
            parentFrame.repaint();
        } catch (Exception e) {
            System.out.println("OH NO!");
        }
    }

}
