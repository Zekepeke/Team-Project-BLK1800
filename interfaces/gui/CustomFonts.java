package interfaces.gui;

import src.gui.components.FontLoader;

import java.awt.*;

/**
 * This interface defines a collection of custom fonts for GUI components.
 * Fonts are categorized by their type and size.
 */
public interface CustomFonts {

    /** A bold font with a large size. */
    Font INSTA_BOLD_FONT = FontLoader.loadCustomFont("/src/gui/assets/fonts/SansBold.ttf", 34f);
    /** A bold font with a smaller size. */
    Font INSTA_BOLD_SMALL_FONT = FontLoader.loadCustomFont("/src/gui/assets/fonts/SansBold.ttf", 16f);
    /** A bold font with the smallest size. */
    Font INSTA_Bold_XSMALL_FONT = FontLoader.loadCustomFont("/src/gui/assets/fonts/SansBold.ttf", 14f);

    /** A medium-weight font with a moderate size. */
    Font INSTA_MEDIUM_FONT = FontLoader.loadCustomFont("/src/gui/assets/fonts/InstagramSansMedium.ttf", 16f);
}
