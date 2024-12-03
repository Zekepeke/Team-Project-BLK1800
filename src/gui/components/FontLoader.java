package src.gui.components;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Component class for loading custom fonts.
 *
 * Class provides a method to load a TTF or OTF from the app's
 * resource directory. The font can be used to style components
 * like labels, buttons, etc.
 *
 *
 * <h2>Usage Example:</h2>
 * <pre>
 * // Load a custom font from the resources folder
 * Font customFont = FontLoader.loadCustomFont("/path/to/font.ttf", 24f);
 *
 * // Apply the font to a JLabel
 * JLabel label = new JLabel("Custom Font Example");
 * label.setFont(customFont);
 * </pre>
 */

public class FontLoader {
    /**
     * Loads a custom font from a specified resource path.
     *
     * @param resourcePath the path to the font file within the resource folder, Example: "/fonts/font.ttf".
     * @param size the font size to apply after loading.
     * @return a {@link Font} object derived from the specified font file and size, or a fallback font if loading fails.
     * @throws IllegalArgumentException if the resource path is null or empty.
     */
    public static Font loadCustomFont(String resourcePath, float size) {
        try (InputStream is = FontLoader.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            return font.deriveFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("Sans Serif", Font.PLAIN, 12); // Fallback font
        }
    }
}
