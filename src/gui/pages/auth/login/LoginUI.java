package src.gui.pages.auth.login;

import interfaces.gui.CustomColors;
import interfaces.gui.CustomFonts;
import src.gui.components.FontLoader;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the graphical user interface for login functionality.
 */
public class LoginUI extends JPanel implements CustomColors, CustomFonts {
    int width;
    int height;
    private JLabel logoLabel;
    private JLabel error;

    private JTextField usernameField; // Make usernameField accessible

    private JPasswordField passwordField; // Make passwordField accessible

    private JButton loginButton;
    private JButton signUpButton;



    /**
     * Constructs a new LoginUI with specified dimensions.
     *
     * @param width  the width of the UI.
     * @param height the height of the UI.
     */
    public LoginUI(int width, int height) {
        // Set up the frame
        this.width = width;
        this.height = height;
        setSize(width, height);


        // Main panel with dark background
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Instagram logo text
        logoLabel = new JLabel("Demo");
        logoLabel.setFont(INSTA_BOLD_FONT);
        logoLabel.setForeground(GRAY_400);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Error
        error = new JLabel("Error, the password or username is invalid");
        error.setFont(INSTA_Bold_XSMALL_FONT);
        error.setForeground(RED_100);
        error.setAlignmentX(Component.CENTER_ALIGNMENT);
        error.setVisible(false);

        // Username or email input field
        usernameField = new JTextField("Username");
        usernameField.setPreferredSize(new Dimension(width, height / 10));
        usernameField.setMaximumSize(new Dimension(width, height / 10));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setBackground(GRAY_100);
        usernameField.setForeground(GRAY_200);
        usernameField.setCaretColor(GRAY_200);
        usernameField.setFont(INSTA_MEDIUM_FONT);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Password input field
        passwordField = new JPasswordField("Password");
        passwordField.setPreferredSize(new Dimension(width, height / 10));
        passwordField.setMaximumSize(new Dimension(width, height / 10));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setBackground(GRAY_100);
        passwordField.setForeground(GRAY_200);
        passwordField.setCaretColor(GRAY_200);
        passwordField.setFont(INSTA_MEDIUM_FONT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Login button
        loginButton = new JButton("Log in");
        loginButton.setPreferredSize(new Dimension(width, height / 10));
        loginButton.setMaximumSize(new Dimension(width, height / 10));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setBackground(BLUE_200);
        loginButton.setOpaque(true);
        loginButton.setForeground(GRAY_300);
        loginButton.setFont(INSTA_BOLD_FONT);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Sign up button
        signUpButton = new JButton("Click to sign up");
        signUpButton.setPreferredSize(new Dimension(width, height / 10));
        signUpButton.setMaximumSize(new Dimension(width, height / 10));
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.setForeground(BLUE_300);
        signUpButton.setFont(INSTA_BOLD_SMALL_FONT);
        signUpButton.setFocusPainted(false);
        signUpButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add components to the main panel
        mainPanel.add(logoLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 50))); // Spacer
        mainPanel.add(usernameField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Spacer
        mainPanel.add(passwordField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Spacer
        mainPanel.add(loginButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(signUpButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(error);


        // Add the main panel to the frame
        add(mainPanel);

        setVisible(true);
    }

    /**
     * Sets the visibility of the error message.
     *
     * @param visible true to show the error message, false to hide it.
     */
    public void setErrorVisible(boolean visible) {
        error.setVisible(visible);
    }

    /**
     * Gets the error message.
     *
     * @return error.
     */
    public JLabel getError() {
        return error;
    }

    /**
     * Gets the username input field.
     *
     * @return the username JTextField.
     */
    public JTextField getUsernameField() {
        return usernameField;
    }

    /**
     * Gets the password input field.
     *
     * @return the password JPasswordField.
     */
    public JPasswordField getPasswordField() {
        return passwordField;
    }

    /**
     * Gets the login button.
     *
     * @return the login JButton.
     */
    public JButton getLoginButton() {
        return loginButton;
    }

    /**
     * Gets the sign-up button.
     *
     * @return the sign-up JButton.
     */
    public JButton getSignUpButton() {
        return signUpButton;
    }

}
