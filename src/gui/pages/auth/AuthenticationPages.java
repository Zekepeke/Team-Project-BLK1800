package src.gui.pages.auth;

import src.client.ClientSide;
import src.gui.pages.auth.login.LoginPage;
import src.gui.pages.auth.signup.SignUpPage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;

public class AuthenticationPages extends JPanel implements Runnable {
    LoginPage loginPage;
    SignUpPage signUpPage;
    private int height = 600;
    private int width = 800;

    ClientSide client;

    public AuthenticationPages () throws IOException {
        Socket socket = new Socket("localhost", 8282);
        client = new ClientSide(socket);

        signUpPage = new SignUpPage(width, height, client);
        loginPage = new LoginPage(width, height, client);

        add(signUpPage);
        add(loginPage);


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
