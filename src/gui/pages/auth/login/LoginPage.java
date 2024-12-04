package src.gui.pages.auth.login;

import interfaces.gui.CustomColors;
import src.client.ClientSide;
import src.gui.components.FontLoader;
import src.gui.components.Sprite;
import src.gui.pages.profile.profilePage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class LoginPage extends JPanel implements CustomColors {

    private ClientSide client;
    private Sprite macBookSprite;
    private Image imageOfMac;

    boolean loginWasVisible;

    Color backgroundColor = BACKGROUND;

    LoginUI loginUI;

    int width;
    int height;

    String usernameString = "Username";
    String passwordString = "Password";

    boolean SignUpButtonState = false;
    boolean validUsernameAndPassword = true;

    public LoginPage(int width, int height, ClientSide client) {
        setPreferredSize(new Dimension(width, height));
        this.width = width;
        this.height = height;
        this.client = client;

        // Load the image
        this.macBookSprite = new Sprite("/src/gui/assets/images/macbook.png", 5000, 5000);
        if (macBookSprite.getImageIcon() != null) {
            this.imageOfMac = macBookSprite.getImageIcon().getImage();
        } else {
            this.imageOfMac = null;
            System.err.println("Error: Resource not found");
        }

        loginWasVisible = false;


        loginUI = new LoginUI(300, 400);
        loginUI.setPreferredSize(new Dimension(300, 400));

        // Add the login button functionality
        loginUI.getLoginButton().addActionListener(e -> {
            usernameString = loginUI.getUsernameField().getText();
            passwordString = new String(loginUI.getPasswordField().getPassword());
            System.out.println("From Login:");
            System.out.println("Username: " + usernameString);
            System.out.println("Password: " + passwordString);
            System.out.println("Valid: " + ClientSide.validUserAndPassword(usernameString, passwordString));
            validUsernameAndPassword = ClientSide.validUserAndPassword(usernameString, passwordString);

            if (!validUsernameAndPassword) {
                System.out.println("Error: Invalid username/password");
                loginUI.setErrorVisible(true);
            } else {
                boolean working = client.searchNameAndPasswordLogin(usernameString, passwordString);
                System.out.println("Testing the server search for client: " + working);
                if (working) {
                    client.setUsername(usernameString);
                    client.setPassword(passwordString);

                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    parentFrame.getContentPane().removeAll();
                    parentFrame.add(new profilePage(width, height, client));
                    parentFrame.revalidate();
                    parentFrame.repaint();
                } else {
                    loginUI.setErrorVisible(true);
                }
            }
        });

        add(loginUI);
        setVisible(true);

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

        // Draw the image (scaled to fit proportionally)
        if (imageOfMac != null) {
            int componentWidth = width;
            int componentHeight = height;

            int imageWidth = imageOfMac.getWidth(null);
            int imageHeight = imageOfMac.getHeight(null);

            // Scale MacBook image proportionally
            double scale = Math.min((double) componentWidth / imageWidth, (double) componentHeight / imageHeight) / 2;
            int scaledWidth = (int) (imageWidth * scale);
            int scaledHeight = (int) (imageHeight * scale);

            // Position the MacBook image (left-aligned)
            int x = (componentWidth - scaledWidth) / 6;
            int y = (componentHeight - scaledHeight) / 6;

            g2d.drawImage(imageOfMac, x, y, scaledWidth, scaledHeight, this);

            // Position LoginUI panel (right-aligned)
            loginUI.setBounds(x + scaledWidth + 50, y + (scaledHeight / 2 - loginUI.getPreferredSize().height / 2),
                    loginUI.getPreferredSize().width, loginUI.getPreferredSize().height);
        }
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

    public LoginUI getLoginUI() {
        return loginUI;
    }
}
