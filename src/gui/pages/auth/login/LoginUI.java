package src.gui.pages.auth.login;

import interfaces.gui.CustomColors;
import src.gui.components.FontLoader;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JPanel implements CustomColors {
    int width;
    int height;
    private JLabel logoLabel;
    private JLabel goToSignUp;

    private JTextField usernameField; // Make usernameField accessible

    private JPasswordField passwordField; // Make passwordField accessible

    private JButton loginButton;
    private JButton signUpButton;

    private Font instaBoldFont;
    private Font instaMediumFont;
    private Font instaBoldSmallFont;

    public LoginUI(int width, int height) {
        // Set up the frame
        this.width = width;
        this.height = height;
        setSize(width, height);
        instaBoldFont = FontLoader.loadCustomFont("/src/gui/assets/fonts/SansBold.ttf", 34f);
        instaMediumFont = FontLoader.loadCustomFont("/src/gui/assets/fonts/InstagramSansMedium.ttf", 16f);
        instaBoldSmallFont = FontLoader.loadCustomFont("/src/gui/assets/fonts/SansBold.ttf", 16f);


        // Main panel with dark background
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Instagram logo text
        logoLabel = new JLabel("Demo");
        logoLabel.setFont(instaBoldFont);
        logoLabel.setForeground(GRAY_400);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username or email input field
        usernameField = new JTextField("Username");
        usernameField.setPreferredSize(new Dimension(width, height / 10));
        usernameField.setMaximumSize(new Dimension(width, height / 10));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setBackground(GRAY_100);
        usernameField.setForeground(GRAY_200);
        usernameField.setCaretColor(GRAY_200);
        usernameField.setFont(instaMediumFont);
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
        passwordField.setFont(instaMediumFont);
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
        loginButton.setFont(instaBoldFont);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Sing up button
        signUpButton = new JButton("Click to sign up");
        signUpButton.setPreferredSize(new Dimension(width, height / 10));
        signUpButton.setMaximumSize(new Dimension(width, height / 10));
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.setForeground(BLUE_300);
        signUpButton.setFont(instaBoldSmallFont);
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

        // Add the main panel to the frame
        add(mainPanel);

        setVisible(true);
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getSignUpButton() {
        return signUpButton;
    }

}