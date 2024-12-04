package src.gui.pages.profile;

import interfaces.BlockedUsersPageInterface;
import interfaces.gui.CustomColors;
import src.User;
import src.client.ClientSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BlockedUsersPage extends JPanel implements CustomColors, BlockedUsersPageInterface {

    private ClientSide client;
    private String[] blockedUsers;
    private Color backgroundColor = BACKGROUND;
    private User user;
    private int width;
    private int height;

    public BlockedUsersPage(int width, int height, ClientSide client) {
        this.width = width;
        this.height = height;
        this.user = client.getUser();
        this.client = client;
        if (client.getUser().getBlocked() == null) {
            this.blockedUsers = new String[0];
        } else {
            this.blockedUsers = client.getUser().getBlocked().toArray(new String[0]);
        }
        setPreferredSize(new Dimension(width, height));

        // Layout
        setLayout(new BorderLayout());
        setBackground(backgroundColor);

        // Title section
        JLabel titleLabel = new JLabel("Blocked Users");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(new Color(192, 57, 43)); // Red color for the title
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Add title to the top of the panel
        add(titleLabel, BorderLayout.NORTH);

        // Panel to hold blocked users list with buttons
        JPanel blockedUsersPanel = new JPanel();
        blockedUsersPanel.setLayout(new BoxLayout(blockedUsersPanel, BoxLayout.Y_AXIS));
        blockedUsersPanel.setOpaque(false);
        JButton backToProfile = createButton("Back To Profile", Color.BLUE);
        backToProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnHome();
            }
        });
        add(backToProfile, BorderLayout.NORTH);
        // Loop through the blockedUsers array and create a panel for each user
        for (String blockedUser : blockedUsers) {
            JPanel userPanel = new JPanel();
            userPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            // Blocked user name label
            JLabel userLabel = new JLabel(blockedUser);
            userLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            userLabel.setPreferredSize(new Dimension(200, 30));

            // Buttons for Unblock and View Profile
            JButton unblockButton = createButton("Unblock", new Color(39, 174, 96)); // Green for unblock
            JButton viewProfileButton = createButton("View Profile", new Color(52, 152, 219)); // Blue for view profile

            // Action listeners for each button
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

            // Add components to the user panel
            userPanel.add(userLabel);
            userPanel.add(unblockButton);
            userPanel.add(viewProfileButton);

            // Add user panel to the blocked users list panel
            blockedUsersPanel.add(userPanel);
        }

        // Add the blocked users panel to the center of the main panel
        JScrollPane scrollPane = new JScrollPane(blockedUsersPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);

        add(scrollPane, BorderLayout.CENTER);
    }
    public void returnHome() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new profilePage(width, height, client));
        parentFrame.revalidate();
        parentFrame.repaint();
    }
    // Helper method to create buttons with consistent styling
    public JButton createButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    public void unblockUser(String blockedUser) {
        System.out.println("Unblocking user: " + blockedUser);
        try {
            user.getBlocked().remove(blockedUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(user);
        user.pushToDatabase();
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new BlockedUsersPage(width, height, client));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    public void viewProfile(String blockedUser) {
        System.out.println("Viewing profile of: " + blockedUser);
        // Implement view profile logic (e.g., open the user's profile page)
    }
}
