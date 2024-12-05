package src.gui.pages.profile;

import interfaces.FriendRequestsPageInterface;
import interfaces.gui.CustomColors;
import interfaces.gui.ProfileInterface;
import src.User;
import src.client.ClientSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FriendRequestsPage extends JPanel implements CustomColors, FriendRequestsPageInterface, ProfileInterface {

    private ClientSide client;
    private String[] friends;
    private Color backgroundColor = BACKGROUND;
    private User user;
    private int width;
    private int height;

    public FriendRequestsPage(int width, int height, ClientSide client) {
        this.width = width;
        this.height = height;
        this.user = client.getUser();
        this.client = client;
        if (client.getUser().getFriendRequestsIn() == null){
            this.friends = new String[0];
        } else {
            this.friends = client.getUser().getFriendRequestsIn().toArray(new String[0]);
        }
        setPreferredSize(new Dimension(width, height));

        // Layout
        setLayout(new BorderLayout());
        setBackground(backgroundColor);

        // Title section
        JLabel titleLabel = new JLabel("Friend Requests List");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(new Color(52, 152, 219)); // Blue color for the title
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
        for (String friend : friends) {
            JPanel friendPanel = new JPanel();
            friendPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            // Friend name label
            JLabel friendLabel = new JLabel(friend);
            friendLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            friendLabel.setForeground(GRAY_100);
            friendLabel.setPreferredSize(new Dimension(200, 30));

            // Buttons for Remove, Block, and View Profile
            JButton removeButton = createButton("Remove", Color.RED);
            JButton addButton = createButton("Add", new Color(231, 76, 60)); // Block button in red
            JButton viewProfileButton = createButton("View Profile", new Color(52, 152, 219)); // View profile in blue

            // Action listeners for each button
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeFriend(friend);
                }
            });

            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addFriend(friend);
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
            friendPanel.add(addButton);
            friendPanel.add(removeButton);
            friendPanel.add(viewProfileButton);

            // Add friend panel to the friends list panel
            friendsListPanel.add(friendPanel);
        }

        // Add the friends list panel to the center of the main panel
        JScrollPane scrollPane = new JScrollPane(friendsListPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Helper method to create buttons with consistent styling
    public JButton createButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(backgroundColor);
        button.setOpaque(true);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    public void returnHome() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new profilePage(width, height, client));
        parentFrame.revalidate();
        parentFrame.repaint();
    }
    public void removeFriend(String friend) {
        System.out.println("Removing friend: " + friend);
        try {
            user.getFriendRequestsIn().remove(friend);
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
    public void addFriend(String friend) {
        System.out.println("Blocking friend: " + friend);
        try {
            user.getFriendRequestsIn().remove(friend);
            user.getFriends().add(friend);
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

    public void viewProfile(String friend) {
        System.out.println("Viewing profile of: " + friend);
        // Implement view profile logic (e.g., open friend's profile page)
    }
}
