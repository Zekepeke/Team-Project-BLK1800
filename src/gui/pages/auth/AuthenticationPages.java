package src.gui.pages.auth;

import src.client.ClientSide;
import src.gui.pages.auth.login.LoginPage;
import src.gui.pages.auth.signup.SignUpPage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
/**
 * This class represents a container for authentication-related pages,
 * including login and sign-up pages. It manages navigation between pages.
 */
public class AuthenticationPages extends JPanel implements Runnable {
    /** Login page component. */
    LoginPage loginPage;
    /** Sign-up page component. */
    SignUpPage signUpPage;


    /** Height of the authentication panel. */
    private int height = 600;
    /** Width of the authentication panel. */
    private int width = 800;

    /** Client-side connection for handling authentication requests. */
    ClientSide client;

    public AuthenticationPages () throws IOException {

        signUpPage = new SignUpPage(width, height, client);
        loginPage = new LoginPage(width, height, client);

        add(signUpPage);
        add(loginPage);


        // Set up navigation between login and sign-up pages
        loginPage.getLoginUI().getSignUpButton().addActionListener(e -> {
            loginPage.setVisible(false);
            signUpPage.getSignUpUI().setErrorVisible(false);
            signUpPage.setVisible(true);

        });
        signUpPage.getSignUpUI().getLoginButton().addActionListener(e -> {
            signUpPage.setVisible(false);
            loginPage.getLoginUI().setErrorVisible(false);
            loginPage.setVisible(true);
        });
    }

    public AuthenticationPages (int width, int height, ClientSide client) throws IOException {
        this.width = width;
        this.height = height;

        signUpPage = new SignUpPage(width, height, client);
        loginPage = new LoginPage(width, height, client);

        add(signUpPage);
        add(loginPage);


        // Set up navigation between login and sign-up pages
        loginPage.getLoginUI().getSignUpButton().addActionListener(e -> {
            loginPage.setVisible(false);
            signUpPage.getSignUpUI().setErrorVisible(false);
            signUpPage.setVisible(true);

        });
        signUpPage.getSignUpUI().getLoginButton().addActionListener(e -> {
            signUpPage.setVisible(false);
            loginPage.getLoginUI().setErrorVisible(false);
            loginPage.setVisible(true);
        });
    }

    /**
     * Runs the authentication pages within a JFrame.
     */
    @Override
    public void run() {
        JFrame frame = new JFrame("Login");
        frame.setSize(width, height);
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        content.setLayout(new BorderLayout());
        content.add(this, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    /**
     * Overrides the default paint method to ensure components are resized
     * dynamically based on the panel's dimensions.
     *
     * @param g the {@code Graphics} object to protect.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        width = getWidth();
        height = getHeight();

        loginPage.setHeight(height);
        loginPage.setWidth(width);
        loginPage.setPreferredSize(new Dimension(width, height));

        signUpPage.setHeight(height);
        signUpPage.setWidth(width);
        signUpPage.setPreferredSize(new Dimension(width, height));



    }

}
