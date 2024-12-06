package src.gui; //package src.gui.pages.messaging;

import src.ConversationReader;
import src.Message;
import src.User;
import src.client.ClientSide;
import src.gui.pages.profile.profilePage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class MessagingPage extends JPanel {
    private User currentUser;
    private User chatPartner;
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

    public MessagingPage(int width, int height, ClientSide client, User chatPartner) {
        this.width = width;
        this.height = height;
        this.client = client;
        this.currentUser = client.getUser();
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
        ConversationReader conversationReader = new ConversationReader(currentUser.getName(), chatPartner.getName());
        currentMessages = conversationReader.getMessages();

        for (Message message : currentMessages) {
            appendMessageToChatDisplay(message);
        }
    }

    /**
     * Filters messages based on the selected filter and updates the chat display.
     */
    private void filterMessages(String filterOption) {
        chatDisplayArea.setText("");
        for (Message message : currentMessages) {
            if (filterOption.equals("All Messages")) {
                appendMessageToChatDisplay(message);
            } else if (filterOption.equals("Sent by Me") && message.getSender().equals(currentUser)) {
                appendMessageToChatDisplay(message);
            } else if (filterOption.equals("Received by Me") && message.getReceiver().equals(currentUser)) {
                appendMessageToChatDisplay(message);
            }
        }
    }

    /**
     * Appends a formatted message to the chat display.
     */
    private void appendMessageToChatDisplay(Message message) {
        chatDisplayArea.append(message.getSender().getName() + ": " + message.getContent() + "\t" + message.getDate() + "\n");
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
                Message newMessage = new Message(currentUser, chatPartner, new java.util.Date(), content);
                newMessage.pushToDatabase(); // Save to the database
                currentMessages.add(newMessage);
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
                if (newPartnerName != null && !newPartnerName.trim().isEmpty() && (currentUser.getBlocked() == null || !currentUser.getBlocked().contains(newPartnerName))) {
                    if (!currentUser.getExclusiveToFriends() || currentUser.getFriends().contains(newPartnerName)) {
                        try {
                            chatPartner = new User(newPartnerName + ".txt"); // Assuming User constructor accepts file name
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
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
