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
     * @param width The initial x-scale of the sprite.
     * @param height The initial y-scale of the sprite.
     */
    public Sprite(String imagePath, int width, int height) {
        java.net.URL imgURL = getClass().getResource(imagePath);

        if (imgURL != null) {
            // Load the image icon
            ImageIcon originalIcon = new ImageIcon(imgURL);
            // Scale down the image
            Image originalImage = originalIcon.getImage();
            this.image = originalImage.getScaledInstance(width , height, Image.SCALE_SMOOTH);
        } else {
            this.image = null;
            System.err.println("Error: Resource not found!");
        }
        this.x = width; // Desired width
        this.y = height; // Desired height
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

    /**
     * Get current image sprite
     *
     * @return the image sprite
     */
    public Image getImage() {
        return image;
    }

    /**
     * Updates the image
     *
     * @param image is the new image or sprite being converted
     */
    public void setImage(Image image) {
        this.image = image;
    }
}
