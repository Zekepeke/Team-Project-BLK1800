package src.gui.pages.auth.signup;

import interfaces.gui.CustomColors;
import src.client.ClientSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class SignUpPage extends JPanel implements CustomColors {

    private ClientSide client;
    boolean isVisible;
    boolean signUpWasVisible;

    Color backgroundColor = BACKGROUND;

    SignUpUI signUpUI;

    int width;
    int height;

    String usernameString = "Username";
    String passwordString = "Password";

    boolean validUsernameAndPassword = true;

    public SignUpPage(int width, int height, ClientSide client) {
        setPreferredSize(new Dimension(width, height));
        this.width = width;
        this.height = height;
        this.client = client;

        isVisible = true;
        signUpWasVisible = false;


        signUpUI = new SignUpUI(300, 430);
        signUpUI.setPreferredSize(new Dimension(300, 450));

        // Add the login button functionality
        signUpUI.getSignUpButton().addActionListener(e -> {
            usernameString = signUpUI.getUsernameField().getText();
            passwordString = new String(signUpUI.getPasswordField().getPassword());
            System.out.println("From Sign up:");
            System.out.println("Username: " + usernameString);
            System.out.println("Password: " + passwordString);

            System.out.println("Valid: " + ClientSide.validUserAndPassword(usernameString, passwordString));
            validUsernameAndPassword = ClientSide.validUserAndPassword(usernameString, passwordString);

            if (!validUsernameAndPassword) {
                signUpUI.setErrorVisible(true);
            } else {
                boolean working = client.searchNameAndPasswordSignUp(usernameString, passwordString);
                System.out.println("Testing the server search for client: " + working);
                if (working) {
                    client.setUsername(usernameString);
                    client.setPassword(passwordString);
                } else {
                    signUpUI.setErrorVisible(true);
                }
            }

        });

        add(signUpUI);
        setVisible(false);

        // Add mouse listeners (later)
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Handle mouse press
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Handle mouse drag
            }
        });
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        width = getWidth();
        height = getHeight();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill background
        g2d.setPaint(backgroundColor);
        g2d.fillRect(0, 0, width, height);
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public String getUsername() {
        return usernameString;
    }

    public String getPassword() {
        return passwordString;
    }

    public SignUpUI getSignUpUI() {
        return signUpUI;
    }

}
