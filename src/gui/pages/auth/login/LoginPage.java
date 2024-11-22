package src.gui.pages.auth.login;

import src.gui.components.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

public class LoginPage extends JComponent implements Runnable {

    private Image image; // Canvas
    private Graphics2D graphics2D; // Enables drawing
    private Sprite macBookSprite;
    private Image spriteImage;

    Color backgroundColor = new Color(33, 36, 45);


    public LoginPage() {
        // Load the image
        this.macBookSprite = new Sprite("/src/gui/assets/macbook.png", 300, 300);

        // Add mouse listeners (if needed)
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
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        Container content = frame.getContentPane();
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
        if (macBookSprite.getImage() != null) {
            int componentWidth = getWidth();
            int componentHeight = getHeight();

            // Calculate scaling factors
            int imageWidth = macBookSprite.getImage().getWidth(null);
            int imageHeight = macBookSprite.getImage().getHeight(null);

            double scaleX = (double) componentWidth / imageWidth;
            double scaleY = (double) componentHeight / imageHeight;
            double scale = Math.min(scaleX, scaleY); // Maintain aspect ratio

            // Calculate centered position
            int newWidth = (int) (imageWidth * scale);
            int newHeight = (int) (imageHeight * scale);
            int x = (componentWidth - newWidth) / 2;
            int y = (componentHeight - newHeight) / 2;

            // Draw scaled image
            g2d.drawImage(macBookSprite.getImage(), x, y, newWidth, newHeight, this);
        }
    }
}
