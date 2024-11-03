import com.mongodb.client.MongoDatabase;
import database.MongoDBConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestMongoDBConnection {

    @BeforeAll
    public static void setUp() {
        // Set environment variables for testing
        System.setProperty("MONGODB_CONNECTION_STRING", "mongodb://localhost:27017");
        System.setProperty("MONGODB_DATABASE_NAME", "testDatabase");
    }

    @Test
    public void testGetDatabase() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        assertNotNull(database, "Database should not be null");
    }

    @AfterAll
    public static void tearDown() {
        MongoDBConnection.close();
    }
}