package src.gui.components;

import javax.swing.*;
import java.awt.*;

/**
 * Represents a visual component in the app with an image and position.
 * The Sprite class can be used to display a users avatars, icons, or other graphical elements.
 * It supports positioning on the screen and updating its location, allowing flexibility
 * for different UI layouts and animations.
 */
public class Sprite extends JComponent {
    private Image image;
    private int x, y;

    /**
     * Constructs new Sprite with an image and initial position.
     *
     * @param imagePath The file path to the image representing this sprite.
     * @param x The initial x-coordinate position of the sprite.
     * @param y The initial y-coordinate position of the sprite.
     */
    public Sprite(String imagePath, int x, int y) {
        this.image = new ImageIcon(imagePath).getImage();
        this.x = x;
        this.y = y;
        setBounds(x, y, image.getWidth(null), image.getHeight(null)); // Set initial bounds
    }

    /**
     * Updates position of the sprite on the screen.
     *
     * @param x The new x-coordinate position.
     * @param y The new y-coordinate position.
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        setLocation(x, y); // Update position on screen
        repaint();
    }

    /**
     * Paints the sprite's image on the screen at its current position.
     * Method is automatically called whenever the component is repainted.
     *
     * @param g The Graphics object used to paint the sprite.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this); // Draw the image at the component's position
    }

    /**
     * Get current x-coordinate position of the sprite.
     *
     * @return The x-coordinate position.
     */
    public int getXPosition() {
        return x;
    }

    /**
     * Get current y-coordinate position of the sprite.
     *
     * @return The y-coordinate position.
     */
    public int getYPosition() {
        return y;
    }
}
