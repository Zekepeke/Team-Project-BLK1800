package src.gui; //package src.gui.pages.messaging;

import interfaces.MessagingPageable;
import src.Message;
import src.SocketIO;
import src.client.ClientSide;
import src.gui.pages.profile.profilePage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MessagingPage extends JPanel implements MessagingPageable {
    private String chatPartner;
    private ClientSide client;
    private JTextArea chatDisplayArea;
    private JTextField messageInputField;
    private JButton sendButton;
    private JButton switchChatButton;
    private JButton backToProfileButton;
    private JComboBox<String> filterComboBox;
    private ArrayList<Message> currentMessages;
    private int width;
    private int height;
    private static final String[] FILTER_OPTIONS = {"All Messages", "Sent by Me", "Received by Me"};

    public MessagingPage(int width, int height, String chatPartner) {
        this.width = width;
        this.height = height;
        this.chatPartner = chatPartner;

        // Set preferred size for the panel
        setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());

        // Chat Display Panel
        chatDisplayArea = new JTextArea();
        chatDisplayArea.setEditable(false);
        chatDisplayArea.setLineWrap(true);
        chatDisplayArea.setWrapStyleWord(true);
        JScrollPane chatScrollPane = new JScrollPane(chatDisplayArea);

        // Message Input Panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        messageInputField = new JTextField();
        sendButton = new JButton("Send");
        sendButton.addActionListener(new SendButtonListener());
        inputPanel.add(messageInputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Filter and Switch Chat Panel
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterComboBox = new JComboBox<>(FILTER_OPTIONS);
        filterComboBox.addActionListener(new FilterMessagesListener());
        switchChatButton = new JButton("Switch Chat");
        switchChatButton.addActionListener(new SwitchChatListener());
        backToProfileButton = new JButton("Back to Home");
        backToProfileButton.addActionListener(new backToProfileListener());
        filterPanel.add(filterComboBox, BorderLayout.CENTER);
        filterPanel.add(switchChatButton, BorderLayout.EAST);
        filterPanel.add(backToProfileButton, BorderLayout.WEST);
        // Adding Components to the Panel
        add(chatScrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        add(filterPanel, BorderLayout.NORTH);

        // Load initial chat history
        loadChatHistory();
    }

    /**
     * Loads chat history using ConversationReader and displays it in the chatDisplayArea.
     */
    private void loadChatHistory() {
        chatDisplayArea.setText("");
        ClientSide.command(chatPartner, SocketIO.TYPE_GET_MESSAGES_FROM_FRIEND);
        for (String message : ClientSide.messageHistory) {
            appendMessageToChatDisplay(message);
        }
    }

    /**
     * Filters messages based on the selected filter and updates the chat display.
     */
    private void filterMessages(String filterOption) {
        chatDisplayArea.setText("");
        for (String message : ClientSide.messageHistory) {
            String[] data = message.split("\n");
            String sender = data[2];
            if (filterOption.equals("All Messages")) {
                appendMessageToChatDisplay(message);
            } else if (filterOption.equals("Sent by Me") && sender.equals(ClientSide.username)) {
                appendMessageToChatDisplay(message);
            } else if (filterOption.equals("Received by Me") && !sender.equals(chatPartner)) {
                appendMessageToChatDisplay(message);
            }
        }
    }



    private void appendMessageToChatDisplay(String message) {
        String[] messageData = message.split("\n");
        String date = messageData[0];
        String name = messageData[1];
        String content = messageData[2];
        chatDisplayArea.append(date + ": " + name + "\t" + content + "\n");
        chatDisplayArea.setCaretPosition(chatDisplayArea.getDocument().getLength());
    }

    /**
     * Sends a new message and updates the chat display.
     */
    private class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String content = messageInputField.getText().trim();
            if (!content.isEmpty()) {
                String newMessage = Message.dataToString(ClientSide.username, chatPartner, content);
                ClientSide.command(chatPartner, content, SocketIO.TYPE_MESSAGE);
                ClientSide.command(chatPartner, SocketIO.TYPE_FRIEND_CONVERSATION_HISTORY);
                appendMessageToChatDisplay(newMessage);
                messageInputField.setText("");
            }
        }
    }

    /**
     * Switches to a new chat by prompting the user for a new chat partner.
     */
    private class SwitchChatListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String newPartnerName = JOptionPane.showInputDialog("Enter the name of the new chat partner:");
            try {
                if (newPartnerName != null && !newPartnerName.trim().isEmpty() && (!ClientSide.blockedUsers.isEmpty() || !ClientSide.blockedUsers.contains(newPartnerName))) {
                    ClientSide.command(SocketIO.TYPE_CHECK_EXCLUSIVE, ClientSide.username);

                    ClientSide.command(ClientSide.username, SocketIO.TYPE_CHECK_EXCLUSIVE);

                    if (!ClientSide.friendExclusive.get() || ClientSide.friends.contains(newPartnerName)) {

                        ClientSide.command(chatPartner, SocketIO.TYPE_GET_MESSAGES_FROM_FRIEND);
                        loadChatHistory();
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "You can't message this person due to your settings or they're not your friend! :(",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "You can't message this person due to your settings or they're not your friend! :(",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "The User Does Not Exist!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class backToProfileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Navigating to Profile Page");

            // Assuming you're working within a JFrame and you want to switch the content to the Profile Page.
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(MessagingPage.this);

            if (parentFrame != null) {
                // Remove current content
                parentFrame.getContentPane().removeAll();

                // Assuming ProfilePage is a JPanel and needs current user and dimensions
                profilePage profilePage = new profilePage(width, height, client); // Adjust constructor as necessary
                parentFrame.add(profilePage);

                // Revalidate and repaint to apply changes
                parentFrame.revalidate();
                parentFrame.repaint();
            } else {
                System.out.println("Parent frame not found.");
            }
        }
    }
    /**
     * Filters messages based on the selected filter option in the filterComboBox.
     */
    private class FilterMessagesListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedFilter = (String) filterComboBox.getSelectedItem();
            if (selectedFilter != null) {
                filterMessages(selectedFilter);
            }
        }
    }
}
