import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

/**
 * Manages conversation history in the 'convo_history' collection.
 * Allows sending, retrieving, and deleting messages between users.
 */
public class MessageDatabase {
    private static final Logger logger = Logger.getLogger(MessageDatabase.class.getName());
    private final MongoCollection<Document> convoCollection;

    /**
     * Constructor that initializes the 'convo_history' collection from the MongoDB database.
     */
    public MessageDatabase() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        convoCollection = database.getCollection("convo_history");
    }

    /**
     * Sends a message between two users and stores it in MongoDB.
     * Stores message details such as sender, receiver, date, and content.
     *
     * @param sender   The name of the user sending the message
     * @param receiver The name of the user receiving the message
     * @param content  The content of the message to be stored
     */
    public synchronized void sendMessage(String sender, String receiver, String content) {
        try {
            Document messageDoc = new Document("messageId", UUID.randomUUID().toString())
                    .append("sender", sender)
                    .append("receiver", receiver)
                    .append("date", new Date())
                    .append("content", content);

            convoCollection.insertOne(messageDoc);
            logger.info("Message sent from " + sender + " to " + receiver);
        } catch (Exception e) {
            logger.severe("Failed to send message: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves the conversation history between two users from MongoDB.
     * Searches for messages where one user is the sender and the other is the receiver.
     *
     * @param user1 The first user's name
     * @param user2 The second user's name
     * @return A list of Documents representing the messages between the two users
     */
    public synchronized List<Document> getMessagesBetweenUsers(String user1, String user2) {
        try {
            List<Document> messages = convoCollection.find(
                    new Document("$or", List.of(
                            new Document("sender", user1).append("receiver", user2),
                            new Document("sender", user2).append("receiver", user1)
                    ))
            ).into(new ArrayList<>());

            logger.info("Retrieved " + messages.size() + " messages between " + user1 + " and " + user2);
            return messages;
        } catch (Exception e) {
            logger.severe("Failed to retrieve messages: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Deletes a specific message from MongoDB based on its unique message ID.
     *
     * @param messageId The unique ID of the message to delete
     */
    public synchronized void deleteMessage(String messageId) {
        try {
            convoCollection.deleteOne(eq("messageId", messageId));
            logger.info("Message deleted with ID: " + messageId);
        } catch (Exception e) {
            logger.severe("Failed to delete message: " + e.getMessage());
            throw e;
        }
    }
}