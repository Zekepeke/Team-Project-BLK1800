import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * Manages the MongoDB connection for the application.
 * Provides methods to establish and close the connection.
 */
public class MongoDBConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    /**
     * Establishes a connection to MongoDB and returns the specified database instance.
     * Uses a singleton pattern to create only one instance of the database.
     * @return the MongoDB database instance.
     */
    public static synchronized MongoDatabase getDatabase() {
        if (mongoClient == null) {
            String connectionString = System.getenv("MONGODB_CONNECTION_STRING");
            String databaseName = System.getenv("MONGODB_DATABASE_NAME");
            if (connectionString == null || databaseName == null) {
                throw new IllegalStateException("Environment variables MONGODB_CONNECTION_STRING and MONGODB_DATABASE_NAME must be set.");
            }
            mongoClient = MongoClients.create(connectionString);
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