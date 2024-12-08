package src.gui.pages.profile;

import interfaces.BlockedUsersPageInterface;
import interfaces.gui.CustomColors;
import interfaces.gui.ProfileInterface;
import src.SocketIO;
import src.User;
import src.client.ClientSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The {@code BlockedUsersPage} class represents a graphical user interface (GUI) panel
 * for displaying and managing the list of users blocked by the current user.
 * It provides options to unblock users and view their profiles.
 *
 * <h2>Features:</h2>
 * <ul>
 *   <li>Displays a list of blocked users with options for interaction.</li>
 *   <li>Allows unblocking a user and refreshes the panel dynamically.</li>
 *   <li>Includes navigation to return to the profile page.</li>
 * </ul>
 *
 * <h2>Usage:</h2>
 * This class is used as part of a larger GUI application where users can manage
 * their blocked user list. It integrates with the {@code ClientSide} and {@code User} classes
 * to fetch and update data.
 *
 * @see JPanel
 * @see ClientSide
 * @see User
 */
public class BlockedUsersPage extends JPanel implements CustomColors, BlockedUsersPageInterface, ProfileInterface {

    private ClientSide client;
    private String[] blockedUsers;
    private Color backgroundColor = BACKGROUND;
    private User user;
    private int width;
    private int height;

    /**
     * Constructs a new {@code BlockedUsersPage}.
     *
     * @param width  the width of the panel.
     * @param height the height of the panel.
     * @param client the {@code ClientSide} instance for user data interactions.
     */
    public BlockedUsersPage(int width, int height, ClientSide client) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));

        // Layout setup
        setLayout(new BorderLayout());
        setBackground(backgroundColor);
        setOpaque(true);

        // Title section
        JLabel titleLabel = new JLabel("Blocked Users");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(new Color(192, 57, 43)); // Red color for the title
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Add title to the top of the panel
        add(titleLabel, BorderLayout.NORTH);

        // Panel for blocked users list
        JPanel blockedUsersPanel = new JPanel();
        blockedUsersPanel.setLayout(new BoxLayout(blockedUsersPanel, BoxLayout.Y_AXIS));
        JButton backToProfile = createButton("Back To Profile", Color.BLUE);
        backToProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnHome();
            }
        });
        add(backToProfile, BorderLayout.NORTH);

        // Populate blocked users list
        for (String blockedUser : ClientSide.blockedUsers) {
            JPanel userPanel = new JPanel();
            userPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            JLabel userLabel = new JLabel(blockedUser);
            userLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            userLabel.setForeground(GRAY_100);
            userLabel.setPreferredSize(new Dimension(200, 30));

            JButton unblockButton = createButton("Unblock", new Color(39, 174, 96)); // Green for unblock
            JButton viewProfileButton = createButton("View Profile", new Color(52, 152, 219)); // Blue for view profile

            unblockButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    unblockUser(blockedUser);
                }
            });

            viewProfileButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    viewProfile(blockedUser);
                }
            });

            userPanel.add(userLabel);
            userPanel.add(unblockButton);
            userPanel.add(viewProfileButton);

            blockedUsersPanel.add(userPanel);
        }

        JScrollPane scrollPane = new JScrollPane(blockedUsersPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Navigates back to the profile page.
     */
    public void returnHome() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new profilePage(width, height, client));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    /**
     * Creates a styled button with the specified text and background color.
     *
     * @param text            the text to display on the button.
     * @param backgroundColor the background color of the button.
     * @return the created {@code JButton}.
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
     * Unblocks the specified user and refreshes the panel.
     *
     * @param blockedUser the username of the user to unblock.
     */
    public void unblockUser(String blockedUser) {
        System.out.println("Unblocking user: " + blockedUser);
        try {
            ClientSide.command(blockedUser, SocketIO.TYPE_UNBLOCK_USER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(blockedUser);
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new BlockedUsersPage(width, height, client));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    /**
     * Displays the profile of the specified user.
     *
     * @param blockedUser the username of the user whose profile to display.
     */
    public void viewProfile(String blockedUser) {
        System.out.println("Viewing profile of: " + blockedUser);
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "User Information", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new BorderLayout());
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        JLabel nameLabel = new JLabel("Name: " + user, SwingConstants.CENTER);
        JLabel bioLabel = new JLabel("<html><body style='text-align: center;'>" + "TEST" + "</body></html>", SwingConstants.CENTER);
        infoPanel.add(nameLabel);
        infoPanel.add(bioLabel);
        dialog.add(infoPanel, BorderLayout.CENTER);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        // Implement view profile logic (e.g., open a new panel)
    }
}
