package src.gui;

import interfaces.MessagingPageable;
import src.Message;
import src.SocketIO;
import src.User;
import src.client.ClientSide;
import src.gui.pages.profile.profilePage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchPage extends JPanel implements MessagingPageable {
    private JPanel userDisplayArea;
    private JButton searchButton;
    private JButton backToProfileButton;
    private JTextField searchField;
    private int width;
    private int height;

    public SearchPage(int width, int height, User chatPartner) {
        this.width = width;
        this.height = height;

        // Set preferred size for the panel
        setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());

        // User Display Area with GridLayout
        userDisplayArea = new JPanel(new GridLayout(0, 1, 10, 10)); // One column, vertical gaps
        userDisplayArea.setBackground(Color.WHITE); // Optional: Add white background for the empty space
        JScrollPane userScrollPane = new JScrollPane(userDisplayArea);
        userScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Searching Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        backToProfileButton = new JButton("Back to Home");
        backToProfileButton.addActionListener(new backToProfileListener());
        searchButton = new JButton("Search");
        searchButton.addActionListener(new SearchButtonListener());

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        searchPanel.add(backToProfileButton, BorderLayout.WEST);

        // Adding Components to the Panel
        add(searchPanel, BorderLayout.NORTH);
        add(userScrollPane, BorderLayout.CENTER);
    }

    private void addUserPane(String user) {
        // Create a custom pane for the user
        if(!ClientSide.blockedUsers.contains(user)) {
            JPanel userPane = new JPanel(new BorderLayout());
            userPane.setPreferredSize(new Dimension(width - 20, 50)); // Fixed size
            userPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            userPane.setBackground(Color.LIGHT_GRAY);

            // Add user label
            JLabel userLabel = new JLabel(user, SwingConstants.LEFT);
            userLabel.setFont(new Font("Arial", Font.BOLD, 16));
            userLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Padding for label
            userPane.add(userLabel, BorderLayout.CENTER);

            // Add "View" button with proper size and alignment
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); // Align right with padding
            JButton viewButton = new JButton("View");
            viewButton.setPreferredSize(new Dimension(80, 35)); // Increased button size for better visibility
            viewButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Adjust font size for better readability
            viewButton.addActionListener(e -> showUserDialog(user, "This is a customizable bio for " + user));
            buttonPanel.add(viewButton);
            buttonPanel.setOpaque(false); // Transparent background
            userPane.add(buttonPanel, BorderLayout.EAST);

            // Add the user pane to the display area
            userDisplayArea.add(userPane);
            userDisplayArea.revalidate();
            userDisplayArea.repaint();
        }
    }

    private void showUserDialog(String user, String bio) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "User Information", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        JLabel nameLabel = new JLabel("Name: " + user, SwingConstants.CENTER);
        JLabel bioLabel = new JLabel("<html><body style='text-align: center;'>" + bio + "</body></html>", SwingConstants.CENTER);
        infoPanel.add(nameLabel);
        infoPanel.add(bioLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add");
        JButton blockButton = new JButton("Block");
        JButton cancelButton = new JButton("Cancel");

        addButton.addActionListener(e -> {
            if(!ClientSide.outGoingFriendRequests.contains(user)) {
                ClientSide.command(user, SocketIO.TYPE_SEND_FRIEND_REQUEST);
                JOptionPane.showMessageDialog(dialog, user + " has been added!");
            } else {
                JOptionPane.showMessageDialog(dialog, user + " is already added!");
            }
            dialog.dispose();
        });

        blockButton.addActionListener(e -> {
            if(!ClientSide.blockedUsers.contains(user)) {
                ClientSide.command(user, SocketIO.TYPE_BLOCK_USER);
                JOptionPane.showMessageDialog(dialog, user + " has been blocked!");
            } else {
                JOptionPane.showMessageDialog(dialog, user + " is already blocked!");
            }
            dialog.dispose();
        });
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(addButton);
        buttonPanel.add(blockButton);
        buttonPanel.add(cancelButton);

        dialog.add(infoPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String content = searchField.getText().trim();
            userDisplayArea.removeAll();
            userDisplayArea.revalidate();
            userDisplayArea.repaint();

            if (!content.isEmpty()) {
                System.out.println("Searched for: " + content);
                String[] results = ClientSide.search(content);
                if (results != null) {
                    for (String result : results) {
                        addUserPane(result);
                    }
                }
                searchField.setText("");
            }
        }
    }

    private class backToProfileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Navigating to Profile Page");

            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(SearchPage.this);

            if (parentFrame != null) {
                parentFrame.getContentPane().removeAll();
                profilePage profilePage = new profilePage(width, height, null);
                parentFrame.add(profilePage);
                parentFrame.revalidate();
                parentFrame.repaint();
            } else {
                System.out.println("Parent frame not found.");
            }
        }
    }
}
