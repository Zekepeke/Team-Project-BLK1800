package src.gui.pages.auth.login;
import src.gui.components.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends JComponent implements Runnable {


    private Image image; // the canvas
    private Graphics2D graphics2D;  // this will enable drawing
    private Sprite sprite;
    private Image spritImage;


    Color presentColor;
    Color backgroundColor;

    Paint paint; // variable of the type Paint


    /* action listener for buttons */
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {


        }

    };





    public LoginPage() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                /* set oldX and oldY coordinates to beginning mouse press*/
            }
        });


        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                /* set current coordinates to where mouse is being dragged*/

            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new LoginPage());
    }

    public void run() {
        /* set up JFrame */
        JFrame frame = new JFrame("Login");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());

        java.net.URL imgURL = getClass().getResource("/src/gui/assets/macbook.png");
        if (imgURL == null) {
            System.err.println("Error: Resource not found!");
            return;
        }

        // Load the image icon
        ImageIcon originalIcon = new ImageIcon(imgURL);

        // Scale down the image
        Image originalImage = originalIcon.getImage();
        int newWidth = 200;  // Desired width
        int newHeight = 150; // Desired height
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        // Set the scaled image in the JLabel
        JLabel label = new JLabel(new ImageIcon(scaledImage));
        content.add(label, BorderLayout.NORTH);


        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    protected void paintComponent(Graphics g) {
        if (image == null) {
            image = createImage(getSize().width, getSize().height);
            /* this lets us draw on the image (ie. the canvas)*/
            graphics2D = (Graphics2D) image.getGraphics();
            /* gives us better rendering quality for the drawing lines */

            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            /* set canvas to white with default paint color */
            graphics2D.setPaint(Color.white);
            backgroundColor = graphics2D.getColor();
            graphics2D.fillRect(0, 0, getSize().width, getSize().height);
            graphics2D.setPaint(Color.black);
            repaint();
        }
        g.drawImage(image, 0, 0, null);

    }
}
