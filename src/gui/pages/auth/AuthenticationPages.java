package src.gui.pages.auth;

import src.gui.pages.auth.login.LoginPage;
import src.gui.pages.auth.signup.SignUpPage;

import javax.swing.*;
import java.awt.*;

public class AuthenticationPages extends JPanel implements Runnable {
    LoginPage loginPage;
    SignUpPage signUpPage;
    private int height = 600;
    private int width = 800;
    public AuthenticationPages () {
        loginPage = new LoginPage(width, height);
        signUpPage = new SignUpPage(width, height);

        add(signUpPage);
        add(loginPage);
        signUpPage.setVisible(false);


    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new AuthenticationPages());
    }

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
