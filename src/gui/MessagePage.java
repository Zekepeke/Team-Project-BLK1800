package src.gui;

import src.User;
import src.client.ClientSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessagePage extends JPanel {
    private JPanel friendDisplayArea;
    private JButton backToHomeButton;
    private int width;
    private int height;

    public MessagePage(int width, int height, User currentUser) {
        this.width = width;
        this.height = height;

        // Set preferred size and layout
        setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());

        // Friend Display Area with GridLayout
        friendDisplayArea = new JPanel(new GridLayout(0, 1, 10, 10)); // Vertical layout with gaps
        friendDisplayArea.setBackground(Color.WHITE);
        JScrollPane friendScrollPane = new JScrollPane(friendDisplayArea);
        friendScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Back to Home Button
        JPanel topPanel = new JPanel(new BorderLayout());
        backToHomeButton = new JButton("Back to Home");
        backToHomeButton.addActionListener(new backToHomeListener());
        topPanel.add(backToHomeButton, BorderLayout.WEST);

        // Adding Components to the Panel
        add(topPanel, BorderLayout.NORTH);
        add(friendScrollPane, BorderLayout.CENTER);

        // Load Friends
        loadFriends();
    }

    /**
     * Loads the list of friends and creates a panel for each friend.
     */
    private void loadFriends() {
        // Simulate getting a list of friends from the client
        if (ClientSide.friends != null) {
            for (String friend : ClientSide.friends) {
                addFriendPane(friend);
            }
        } else {
            // If no friends, show a message
            JLabel noFriendsLabel = new JLabel("You have no friends to chat with.", SwingConstants.CENTER);
            friendDisplayArea.add(noFriendsLabel);
        }

        friendDisplayArea.revalidate();
        friendDisplayArea.repaint();
    }

    /**
     * Adds a panel for each friend with a "Chat" button.
     *
     * @param friend The friend's username.
     */
    private void addFriendPane(String friend) {
        // Create a panel for the friend
        JPanel friendPane = new JPanel(new BorderLayout());
        friendPane.setPreferredSize(new Dimension(width - 20, 50));
        friendPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        friendPane.setBackground(Color.LIGHT_GRAY);

        // Add friend's name
        JLabel friendLabel = new JLabel(friend, SwingConstants.LEFT);
        friendLabel.setFont(new Font("Arial", Font.BOLD, 16));
        friendLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        friendPane.add(friendLabel, BorderLayout.CENTER);

        // Add "Chat" button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton chatButton = new JButton("Chat");
        chatButton.setPreferredSize(new Dimension(80, 35));
        chatButton.setFont(new Font("Arial", Font.PLAIN, 14));
        chatButton.addActionListener(e -> openChatPane(friend));
        buttonPanel.add(chatButton);
        buttonPanel.setOpaque(false);
        friendPane.add(buttonPanel, BorderLayout.EAST);

        // Add the friend pane to the display area
        friendDisplayArea.add(friendPane);
    }

    /**
     * Opens your custom ChatPane for the selected friend.
     *
     * @param friend The friend's username.
     */
    private void openChatPane(String friend) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        if (parentFrame != null) {
            parentFrame.getContentPane().removeAll();

            // Instantiate your custom ChatPane class
            MessagingPage chatPane = new MessagingPage(width, height, friend);

            parentFrame.add(chatPane);
            parentFrame.revalidate();
            parentFrame.repaint();
        } else {
            System.out.println("Parent frame not found.");
        }
    }

    /**
     * Handles the back to home button click event.
     */
    private class backToHomeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Navigating to Home Page");

            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(MessagePage.this);

            if (parentFrame != null) {
                parentFrame.getContentPane().removeAll();
                SearchPage searchPage = new SearchPage(width, height, null); // Adjust constructor as necessary
                parentFrame.add(searchPage);
                parentFrame.revalidate();
                parentFrame.repaint();
            } else {
                System.out.println("Parent frame not found.");
            }
        }
    }
}
