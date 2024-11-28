package src.gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Represents a visual component in the app with an image and position.
 * The Sprite class can be used to display a users avatars, icons, or other graphical elements.
 * It supports positioning on the screen and updating its location, allowing flexibility
 * for different UI layouts and animations.
 */
public class Sprite extends JComponent {
    private ImageIcon image;
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
            Image imageTransform = originalIcon.getImage();
            Image newimg = imageTransform.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            this.image = new ImageIcon(newimg);
        } else {
            this.image = null;
            System.err.println("Error: Resource not found!");
        }
        this.x = width; // Desired width
        this.y = height; // Desired height
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
    public ImageIcon getImageIcon() {
        return image;
    }

    /**
     * Updates the image
     *
     * @param image is the new image or sprite being converted
     */
    public void setImage(ImageIcon image) {
        this.image = image;
    }


}
