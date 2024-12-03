package interfaces.gui;

import src.gui.components.FontLoader;

import java.awt.*;

public interface CustomFonts {

    Font INSTA_BOLD_FONT = FontLoader.loadCustomFont("/src/gui/assets/fonts/SansBold.ttf", 34f);
    Font INSTA_BOLD_SMALL_FONT = FontLoader.loadCustomFont("/src/gui/assets/fonts/SansBold.ttf", 16f);
    Font INSTA_Bold_XSMALL_FONT = FontLoader.loadCustomFont("/src/gui/assets/fonts/SansBold.ttf", 14f);

    Font INSTA_MEDIUM_FONT = FontLoader.loadCustomFont("/src/gui/assets/fonts/InstagramSansMedium.ttf", 16f);
}
