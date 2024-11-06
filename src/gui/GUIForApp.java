package src.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GUIForApp serves as the main graphical interface for our social media app.
 * This class sets up the main app window with a panel for displaying various
 * social media features, such as user profiles, messages, and interactive components.
 * It also supports adding graphical `Sprite` components to represent user avatars or icons.
 */
public class GUIForApp {
    private JFrame frame;
    private JPanel mainPanel;
    private JLabel headerLabel;
    private boolean exitPressed = false;


    /**
     * Constructs main GUI for the social media app.
     * Initializes primary frame, main panel, and header label.
     * Additional UI elements, such as avatars, user profile buttons, and
     * other interactive components, can be added to the main panel.
     */
    public GUIForApp() {
        // Set up the main application frame
        frame = new JFrame("Social Media App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);  // Set an appropriate size for social media layout
        frame.setLayout(new BorderLayout());

        // Set up the main panel with absolute positioning for flexible layout
        mainPanel = new JPanel(null);  // Using null layout for positioning Sprites
        mainPanel.setBackground(Color.WHITE);

        // Header label to display app title or welcome message
        headerLabel = new JLabel("Welcome to Socialize!"); // Customizable header
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(headerLabel, BorderLayout.NORTH);

        // Add the main panel to the center of the frame
        frame.add(mainPanel, BorderLayout.CENTER);
        // Create an Exit button
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitPressed = true;
                frame.dispose(); // Close the frame if Exit is pressed
            }
        });
        frame.add(exitButton, BorderLayout.SOUTH);


        // Make the frame visible
        frame.setVisible(true);
    }

    /**
     * Adds a `Sprite` component from the Sprite class to the main panel.
     * Sprites can be used to represent user avatars, icons, or other
     * interactive elements within the social media app.
     *
     * @param sprite The `Sprite` component to be added.
     */
    public void addSprite(Sprite sprite) {
        mainPanel.add(sprite);
        mainPanel.revalidate(); // Refresh layout after adding
        mainPanel.repaint();
    }

    /**
     * Checks if the exit button has been pressed.
     *
     * @return true if the exit button has been pressed, false otherwise.
     */
    public boolean theGUIExitPressed() {
        return exitPressed;
    }
}
