package src.gui.pages.auth.signup;

import interfaces.gui.CustomColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class SignUpPage extends JPanel implements CustomColors {

    private Image image; // Canvas
    private Graphics2D graphics2D; // Enables drawing

    boolean isVisible;

    JTextField username;
    JTextField password;

    JButton loginButton;

    Color backgroundColor = BACKGROUND;

    SignUpPage SingUpPage;
    SignUpUI singUpUI;

    int width;
    int height;

    String usernameString = "Username";
    String passwordString = "Password";

    public SignUpPage(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        this.width = width;
        this.height = height;

        isVisible = true;


        singUpUI = new SignUpUI(300, 400);
        singUpUI.setPreferredSize(new Dimension(300, 400));

        // Add the login button functionality
        singUpUI.getLoginButton().addActionListener(e -> {
            usernameString = singUpUI.getUsernameField().getText();
            passwordString = new String(singUpUI.getPasswordField().getPassword());
            System.out.println("Username: " + usernameString);
            System.out.println("Password: " + passwordString);
        });

        add(singUpUI);

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


        int componentWidth = width;
        int componentHeight = height;

        int panelWidth = singUpUI.getWidth();
        int panelHeight = singUpUI.getHeight();

        // Scale MacBook image proportionally
        double scale = Math.min((double) componentWidth / panelWidth, (double) componentHeight / panelHeight) / 2;
        int scaledWidth = (int) (panelWidth * scale);
        int scaledHeight = (int) (panelHeight * scale);

        // Position the MacBook image (left-aligned)
        int x = (componentWidth - scaledWidth) / 6;
        int y = (componentHeight - scaledHeight) / 6;

        // Position LoginUI panel (right-aligned)
        singUpUI.setPreferredSize(new Dimension(scaledWidth, scaledHeight));

    }

    public void setWidth(int width) {
        this.width = width;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public void setIsVisible(boolean visible) {
        setVisible(visible);
    }

    public String getUsername() {
        return usernameString;
    }

    public String getPassword() {
        return passwordString;
    }

}
