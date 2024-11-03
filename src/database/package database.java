package database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

/**
 * Manages the MongoDB connection for the application.
 * Provides methods to establish and close the connection.
 */
public class MongoDBConnection {
    // MongoDB client instance used to connect to the database.
    private static MongoClient mongoClient;
    
    // MongoDB database instance used to access the database.
    private static MongoDatabase database;

    /**
     * Establishes a connection to MongoDB and returns the specified database instance.
     * Uses a singleton pattern to create only one instance of the database.
     * @return the MongoDB database instance.
     */
    public static synchronized MongoDatabase getDatabase() {
        if (mongoClient == null) {
            // Retrieve connection string and database name from environment variables
            String connectionString = System.getenv("MONGODB_CONNECTION_STRING");
            String databaseName = System.getenv("MONGODB_DATABASE_NAME");
            
            // Check if environment variables are set
            if (connectionString == null || databaseName == null) {
                throw new IllegalStateException("Environment variables MONGODB_CONNECTION_STRING and MONGODB_DATABASE_NAME must be set.");
            }
            
            // Create MongoClient using the connection string
            mongoClient = MongoClients.create(connectionString);
            // Access the specified database
            database = mongoClient.getDatabase(databaseName);
        }
        return database;
    }

    /**
     * Closes the MongoDB connection if it is open.
     * Ensures proper resource management by closing the client connection when done.
     */
    public static synchronized void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
        }
    }
}

/**
 * Manages user information in the 'user' collection.
 * Allows adding, retrieving, and deleting user information in MongoDB.
 */
public class UserDatabase {
    // MongoDB collection instance representing the "user" collection.
    private final MongoCollection<Document> userCollection;

    /**
     * Constructor that initializes the 'user' collection from the MongoDB database.
     */
    public UserDatabase() {
        // Retrieves the MongoDB database instance through MongoDBConnection.
        MongoDatabase database = MongoDBConnection.getDatabase();
        // Accesses the "user" collection and assigns it to userCollection.
        userCollection = database.getCollection("user"); // Collection for storing user information
    }

    /**
     * Adds a new user to the MongoDB database.
     * Stores user information such as name, bio, password, friends list, and blocked list.
     *
     * @param name User's name
     * @param bio User's bio, describing their profile
     * @param password User's password for authentication
     */
    public synchronized void addUser(String name, String bio, String password) {
        // Creates a document with user information and generates a unique ID.
        Document userDoc = new Document("userId", UUID.randomUUID().toString())
                .append("name", name)                    // User's name
                .append("bio", bio)                      // User's profile bio
                .append("password", password)            // User's password
                .append("friends", new ArrayList<String>()) // Initializes an empty friends list
                .append("blocked", new ArrayList<String>()); // Initializes an empty blocked list

        // Inserts the new user document into the MongoDB user collection.
        userCollection.insertOne(userDoc);
        System.out.println("User added to MongoDB: " + name); // Success message
    }

    /**
     * Retrieves a user's information from MongoDB based on their name.
     * Searches for a user document where the name field matches the given name.
     *
     * @param name The name of the user to retrieve
     * @return The user's information as a Document, or null if not found
     */
    public synchronized Document getUserByName(String name) {
        // Searches for a user document in MongoDB that matches the specified name.
        Document userDoc = userCollection.find(eq("name", name)).first();
        if (userDoc != null) {
            System.out.println("User information loaded for: " + name); // User data loaded message
        } else {
            System.out.println("User not found with name: " + name); // Message when user is not found
        }
        return userDoc; // Returns the user information (null if not found)
    }

    /**
     * Deletes a user from the MongoDB database based on their name.
     * @param name The name of the user to delete
     */
    public synchronized void deleteUser(String name) {
        // Deletes the user document from MongoDB that matches the specified name.
        userCollection.deleteOne(eq("name", name));
        System.out.println("User deleted with name: " + name); // Deletion success message
    }
}

/**
 * Manages conversation history in the 'convo_history' collection.
 * Allows sending, retrieving, and deleting messages between users.
 */
public class MessageDatabase {
    // MongoDB collection instance representing the "convo_history" collection.
    private final MongoCollection<Document> convoCollection;

    /**
     * Constructor that initializes the 'convo_history' collection from the MongoDB database.
     */
    public MessageDatabase() {
        // Retrieves the MongoDB database instance through MongoDBConnection.
        MongoDatabase database = MongoDBConnection.getDatabase();
        // Accesses the "convo_history" collection and assigns it to convoCollection.
        convoCollection = database.getCollection("convo_history"); // Collection for storing message data
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
        // Creates a document with message details, including a unique message ID.
        Document messageDoc = new Document("messageId", UUID.randomUUID().toString()) // Unique message ID
                .append("sender", sender)       // Message sender
                .append("receiver", receiver)   // Message receiver
                .append("date", new Date())     // Date and time of the message
                .append("content", content);    // Message content

        // Inserts the message document into the MongoDB convo_history collection.
        convoCollection.insertOne(messageDoc);
        System.out.println("Message sent from " + sender + " to " + receiver); // Success message
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
        // Retrieves messages between two users in the MongoDB collection.
        List<Document> messages = convoCollection.find(
                new Document("$or", List.of(
                        new Document("sender", user1).append("receiver", user2),
                        new Document("sender", user2).append("receiver", user1)
                ))
        ).into(new ArrayList<>());

        System.out.println("Retrieved " + messages.size() + " messages between " + user1 + " and " + user2); // Retrieval message
        return messages; // Returns the list of messages
    }

    /**
     * Deletes a specific message from MongoDB based on its unique message ID.
     *
     * @param messageId The unique ID of the message to delete
     */
    public synchronized void deleteMessage(String messageId) {
        // Deletes a message document from MongoDB that matches the specified message ID.
        convoCollection.deleteOne(eq("messageId", messageId));
        System.out.println("Message deleted with ID: " + messageId); // Deletion success message
    }
}
