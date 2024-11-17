import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import src.gui.GUIForApp;

public class GUIForAppTest {
    private GUIForApp gui;

    @Before
    public void setUp() {
        gui = new GUIForApp();
    }

    @Test
    public void testGUIForAppConstructor() {
        assertNotNull("GUIForApp instance should be created", gui);
    }

    @Test
    public void testTheGUIExitPressed() {
        assertFalse("Exit should not be pressed initially", gui.theGUIExitPressed());
    }
}
