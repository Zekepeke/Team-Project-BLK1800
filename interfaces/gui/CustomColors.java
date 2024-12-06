package interfaces.gui;

import java.awt.*;
/**
 * This interface defines a collection of custom colors for GUI components.
 * The colors are organized by their use case or shade group.
 */

public interface CustomColors {
    /** Background color used for the application. */
    Color BACKGROUND = new Color(0,0,0);

    Color GREEN_50 = new Color(92, 184, 92);
    Color GREEN_100 = new Color(46, 204, 113);


    Color BLUE_100 = new Color(10, 105, 173);
    Color BLUE_150 = new Color(52, 152, 219);
    Color BLUE_200 = new Color(0, 149, 246);
    Color BLUE_300 = new Color(36, 160, 237);

    Color RED_100 = new Color(237, 67, 55);
    Color RED_150 = new Color(231, 76, 60);

    Color GRAY_100 = new Color(18, 18, 18);
    Color GRAY_200 = new Color(147, 159, 164);
    Color GRAY_300 = new Color(170, 175, 179);
    Color GRAY_400 = new Color(185, 190, 190);

}
