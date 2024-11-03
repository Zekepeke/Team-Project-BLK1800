import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

/**
 * Manages user information in the 'user' collection.
 * Allows adding, retrieving, and deleting user information in MongoDB.
 */
public class UserDatabase {
    private static final Logger logger = Logger.getLogger(UserDatabase.class.getName());
    private final MongoCollection<Document> userCollection;

    /**
     * Constructor that initializes the 'user' collection from the MongoDB database.
     */
    public UserDatabase() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        userCollection = database.getCollection("user");
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
        try {
            Document userDoc = new Document("userId", UUID.randomUUID().toString())
                    .append("name", name)
                    .append("bio", bio)
                    .append("password", password)
                    .append("friends", new ArrayList<String>())
                    .append("blocked", new ArrayList<String>());

            userCollection.insertOne(userDoc);
            logger.info("User added to MongoDB: " + name);
        } catch (Exception e) {
            logger.severe("Failed to add user to MongoDB: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves a user's information from MongoDB based on their name.
     * Searches for a user document where the name field matches the given name.
     *
     * @param name The name of the user to retrieve
     * @return The user's information as a Document, or null if not found
     */
    public synchronized Document getUserByName(String name) {
        try {
            Document userDoc = userCollection.find(eq("name", name)).first();
            if (userDoc != null) {
                logger.info("User information loaded for: " + name);
            } else {
                logger.warning("User not found with name: " + name);
            }
            return userDoc;
        } catch (Exception e) {
            logger.severe("Failed to retrieve user from MongoDB: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Deletes a user from the MongoDB database based on their name.
     * @param name The name of the user to delete
     */
    public synchronized void deleteUser(String name) {
        try {
            userCollection.deleteOne(eq("name", name));
            logger.info("User deleted with name: " + name);
        } catch (Exception e) {
            logger.severe("Failed to delete user from MongoDB: " + e.getMessage());
            throw e;
        }
    }
}
