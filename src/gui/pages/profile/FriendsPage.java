package src.gui.pages.profile;

import interfaces.gui.CustomColors;
import src.User;
import src.client.ClientSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FriendsPage extends JPanel implements CustomColors {

    private ClientSide client;
    private String[] friends;
    private Color backgroundColor = BACKGROUND;
    private User user;
    private int width;
    private int height;

    public FriendsPage(int width, int height, ClientSide client) {
        try {
            this.width = width;
            this.height = height;
            this.user = client.getUser();
            this.client = client;
            setPreferredSize(new Dimension(width, height));
        } catch (Exception e) {
            this.width = width;
            this.height = height;
            this.client = client;
            setPreferredSize(new Dimension(width, height));
        }
        // Layout
        setLayout(new BorderLayout());
        setBackground(backgroundColor);
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Title section
        JLabel titleLabel = new JLabel("Friends List");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(BLUE_150); // Blue color for the title
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Add title to the top of the panel
        add(titleLabel, BorderLayout.NORTH);

        // Panel to hold friend list with buttons
        JPanel friendsListPanel = new JPanel();
        friendsListPanel.setLayout(new BoxLayout(friendsListPanel, BoxLayout.Y_AXIS));
        JButton backToProfile = createButton("Back To Profile", Color.BLUE);
        backToProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnHome();
            }
        });
        add(backToProfile, BorderLayout.NORTH);
        // Loop through the friends array and create a panel for each friend
        for (String friend : ClientSide.friends) {
            JPanel friendPanel = new JPanel();
            friendPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            // Friend name label
            JLabel friendLabel = new JLabel(friend);
            friendLabel.setFont(new Font("Arial", Font.PLAIN, 18));

            friendLabel.setForeground(GRAY_100);

            friendLabel.setPreferredSize(new Dimension(200, 30));

            // Buttons for Remove, Block, and View Profile
            JButton removeButton = createButton("Remove", Color.RED);
            JButton blockButton = createButton("Block", new Color(231, 76, 60)); // Block button in red
            JButton viewProfileButton = createButton("View Profile", new Color(52, 152, 219)); // View profile in blue

            // Action listeners for each button
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeFriend(friend);
                }
            });

            blockButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    blockFriend(friend);
                }
            });

            viewProfileButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    viewProfile(friend);
                }
            });

            // Add components to friend panel
            friendPanel.add(friendLabel);
            friendPanel.add(removeButton);
            friendPanel.add(blockButton);
            friendPanel.add(viewProfileButton);

            // Add friend panel to the friends list panel
            friendsListPanel.add(friendPanel);
        }

        // Add the friends list panel to the center of the main panel
        JScrollPane scrollPane = new JScrollPane(friendsListPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Helper method to create buttons with consistent styling
    private JButton createButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(backgroundColor);
        button.setOpaque(true);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }
    private void returnHome() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new profilePage(width, height, client));
        parentFrame.revalidate();
        parentFrame.repaint();
    }
    // Placeholder methods for handling button actions
    private void removeFriend(String friend) {
        System.out.println("Removing friend: " + friend);
        try {
            user.removeFriend(new User(friend + ".txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(user);
        user.pushToDatabase();
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new FriendsPage(width, height, client));
        parentFrame.revalidate();
        parentFrame.repaint();
    }
    private void blockFriend(String friend) {
        System.out.println("Blocking friend: " + friend);
        try {
            user.block(new User(friend + ".txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(user);
        user.pushToDatabase();
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new FriendsPage(width, height, client));
        parentFrame.revalidate();
        parentFrame.repaint();
    }



    private void viewProfile(String friend) {
        System.out.println("Viewing profile of: " + friend);
        // Implement view profile logic (e.g., open friend's profile page)
    }
}
