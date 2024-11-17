//package tests;
//
//import static org.junit.Assert.*;
//import org.junit.Before;
//import org.junit.Test;
//import src.gui.GUIForApp;
//import javax.swing.*;
//
//public class GUIForAppTest {
//    private GUIForApp gui;
//
//    @Before
//    public void setUp() {
//        gui = new GUIForApp();
//    }
//
//    @Test
//    public void testGUIForAppConstructor() {
//        assertNotNull("GUIForApp instance should be created", gui);
//    }
//
//    @Test
//    public void testTheGUIExitPressed() {
//        assertFalse("Exit should not be pressed initially", gui.theGUIExitPressed());
//    }
//
//    @Test
//    public void testSendButtonSendsMessageToServerWithMock() {
//        MockServer mockServer = new MockServer();
//        gui.setServer(mockServer);
//        JTextField messageField = gui.getMessageField();
//        JButton sendButton = gui.getSendButton();
//
//        messageField.setText("Test Message");
//        sendButton.doClick();
//
//        assertEquals("Message sent to server", "Test Message", mockServer.getLastReceivedMessage());
//    }
//
//    @Test
//    public void testGUIStateChangeOnConnection() {
//        gui.updateConnectionStatus(true);
//        assertTrue("Send button should be enabled", gui.getSendButton().isEnabled());
//
//        gui.updateConnectionStatus(false);
//        assertFalse("Send button should be disabled", gui.getSendButton().isEnabled());
//    }
//}
