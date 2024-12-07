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

/**
 * Represents the page displaying the user's incoming friend requests.
 * The user can view, accept, remove, or block friend requests.
 * This class provides a graphical user interface for managing friend requests.
 *
 * It implements {@link FriendRequestsPageInterface} and {@link ProfileInterface},
 * and extends {@link JPanel} for custom layout and rendering.
 *
 * The class includes:
 * <ul>
 *   <li>Rendering friend requests as a list with buttons to accept, remove, or view profiles.</li>
 *   <li>Navigation to and from the user's profile page.</li>
 *   <li>Interaction with the user's friend requests through button actions.</li>
 * </ul>
 *
 * The layout consists of:
 * <ul>
 *   <li>A title section for the page.</li>
 *   <li>A scrollable list of incoming friend requests.</li>
 *   <li>Buttons for each friend request with actions for removal, addition, or profile viewing.</li>
 * </ul>
 */
public class FriendRequestsPage extends JPanel implements CustomColors, FriendRequestsPageInterface, ProfileInterface {

    private ClientSide client;
    private String[] friends;
    private Color backgroundColor = BACKGROUND;
    private User user;
    private int width;
    private int height;

    /**
     * Constructs a {@code FriendRequestsPage} to display incoming friend requests.
     *
     * @param width the width of the panel
     * @param height the height of the panel
     * @param client the client-side application instance to manage user data and interactions
     */
    public FriendRequestsPage(int width, int height, ClientSide client) {

        this.width = width;
        this.height = height;
        this.user = client.getUser();
        this.client = client;

        // Initialize friend requests from the user
        if (client.getUser().getFriendRequestsIn() == null){
            this.friends = new String[0];
        } else {
            this.friends = client.getUser().getFriendRequestsIn().toArray(new String[0]);
        }
        setPreferredSize(new Dimension(width, height));

        // Layout configuration
        setLayout(new BorderLayout());
        setBackground(backgroundColor);

        // Title section
        JLabel titleLabel = new JLabel("Friend Requests List");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(new Color(52, 152, 219)); // Blue color for the title
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        add(titleLabel, BorderLayout.NORTH);

            // Panel for holding friend list and buttons
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

        // Iterate over friend requests and create UI components for each
        for (String friend : friends) {
            JPanel friendPanel = new JPanel();
            friendPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            // Friend name label
            JLabel friendLabel = new JLabel(friend);
            friendLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            friendLabel.setForeground(GRAY_100);
            friendLabel.setPreferredSize(new Dimension(200, 30));

            // Action buttons for Remove, Add (block), and View Profile
            JButton removeButton = createButton("Remove", Color.RED);
            JButton addButton = createButton("Add", new Color(231, 76, 60));
            JButton viewProfileButton = createButton("View Profile", new Color(52, 152, 219));

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

            // Add buttons and friend label to the friend panel
            friendPanel.add(friendLabel);
            friendPanel.add(addButton);
            friendPanel.add(removeButton);
            friendPanel.add(viewProfileButton);

            // Add the friend panel to the list
            friendsListPanel.add(friendPanel);
        }

        // Add the scrollable list of friends to the main panel
        JScrollPane scrollPane = new JScrollPane(friendsListPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Creates a styled button with specified text and background color.
     *
     * @param text the text to display on the button
     * @param backgroundColor the background color of the button
     * @return a newly created {@link JButton}
     */
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

    /**
     * Navigates back to the user's profile page.
     * This removes the current content and adds the profile page to the frame.
     */
    public void returnHome() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new profilePage(width, height, client));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    /**
     * Removes a friend request from the user's incoming friend requests.
     * The user is then redirected to the updated friends page.
     *
     * @param friend the name of the friend to be removed
     */
    public void removeFriend(String friend) {
        System.out.println("Removing friend: " + friend);
        try {
            user.getFriendRequestsIn().remove(friend);
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.pushToDatabase();
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new FriendsPage(width, height, client));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    /**
     * Accepts a friend request by adding the friend to the user's friend list.
     * The user is then redirected to the updated friends page.
     *
     * @param friend the name of the friend to be added
     */
    public void addFriend(String friend) {
        System.out.println("Blocking friend: " + friend);
        try {
            user.getFriendRequestsIn().remove(friend);
            user.getFriends().add(friend);
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.pushToDatabase();
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new FriendsPage(width, height, client));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    /**
     * Displays the profile of a friend.
     * Currently, this method only prints the friend's name to the console.
     * Further functionality can be implemented to navigate to a friend's profile page.
     *
     * @param friend the name of the friend whose profile is to be viewed
     */
    public void viewProfile(String friend) {
        System.out.println("Viewing profile of: " + friend);
        // Implement view profile logic (e.g., open friend's profile page)
    }
}
