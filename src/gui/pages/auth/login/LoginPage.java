package src.gui.pages.auth.login;

import src.gui.components.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class LoginPage extends JComponent implements Runnable {

    private Image image; // Canvas
    private Graphics2D graphics2D; // Enables drawing
    private Sprite macBookSprite;
    private Image imageOfMac;

    JTextField username;
    JTextField password;

    JButton loginButton;

    Color backgroundColor = new Color(24,24,26);

    LoginPage login;
    LoginUI loginUI;


    public LoginPage() {
        // Load the image
        this.macBookSprite = new Sprite("/src/gui/assets/images/macbook.png", 5000, 5000);
        if (macBookSprite.getImageIcon() != null) {
            this.imageOfMac = macBookSprite.getImageIcon().getImage();
        } else {
            this.imageOfMac = null;
            System.err.println("Error: Resource not found");
        }


        loginUI = new LoginUI(300, 400);
        loginUI.setPreferredSize(new Dimension(300, 400));

        // Add the login button functionality
        loginUI.getLoginButton().addActionListener(e -> {
            String username = loginUI.getUsernameField().getText();
            String password = new String(loginUI.getPasswordField().getPassword());
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
        });

        add(loginUI);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new LoginPage());
    }

    public void run() {
        JFrame frame = new JFrame("Login");
        frame.setSize(800, 600);
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LoginPage loginPage = new LoginPage();
        content.add(loginPage, BorderLayout.CENTER);



        content.setLayout(new BorderLayout());
        content.add(this, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill background
        g2d.setPaint(backgroundColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw the image (scaled to fit proportionally)
        if (imageOfMac != null) {
            int componentWidth = getWidth();
            int componentHeight = getHeight();

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
}
