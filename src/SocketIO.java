package src;

import interfaces.IO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Handles socket-based input/output communication with a client.
 */
public class SocketIO implements IO {

    private static final String IMG_TOKEN = "|img|";
    private static final String ERROR_INVALID_INFORMATION = "[ERROR] Invalid information received.";
    private static final String ERROR_SOCKET_CLOSED = "[ERROR] Socket is closed.";

    private final Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    /**
     * Initializes a new SocketIO instance for client communication.
     *
     * @param socket The client socket to communicate with.
     */
    public SocketIO(Socket socket) {
        this.socket = socket;
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            logError("Failed to initialize socket IO streams.", e);
        }
    }

    /**
     * Writes data to the client socket.
     *
     * @param stream          The data to write as a string array.
     * @param informationType The type of information being sent.
     * @return true if the message was written successfully, false otherwise.
     */
    public boolean write(String[] stream, String informationType) {
        if (writer == null) {
            logError(ERROR_SOCKET_CLOSED, null);
            return false;
        }

        StringBuilder message = new StringBuilder(DELIMITER_START).append(informationType);
        if (stream != null && stream.length > 0) {
            for (String part : stream) {
                message.append(SPLITTER).append(part);
            }
        }
        message.append(DELIMITER_END);

        writer.println(message);
        return true;
    }

    /**
     * Reads data from the client socket.
     *
     * @return The data read as a string array, or null if reading fails.
     */
    public String[] read() {
        try {
            String input = reader.readLine();

            input = input.substring(DELIMITER_START.length(), input.length() - DELIMITER_END.length());

            if(input.contains(SPLITTER)) {return new String[]{input};};

            return input.split(SPLITTER);
        } catch (IOException e) {
            logError("Failed to read from socket.", e);
            return null;
        }
    }

    /**
     * Validates the information type received from the client.
     *
     * @param information The information type to validate.
     * @return true if the information type is valid, false otherwise.
     */
    public boolean validInformation(String information) {
        return TYPE_HAND_SHAKE.equals(information);
    }

    /**
     * Sets a new input stream for reading from the client socket.
     *
     * @param stream The input stream to set.
     * @return true if the input stream was successfully set, false otherwise.
     */
    public boolean setStreamReader(InputStream stream) {
        this.reader = new BufferedReader(new InputStreamReader(stream));
        return true;
    }

    /**
     * Sets a new output stream for writing to the client socket.
     *
     * @param stream The output stream to set.
     * @return true if the output stream was successfully set, false otherwise.
     */
    public boolean setStreamWriter(OutputStream stream) {
        this.writer = new PrintWriter(stream, true);
        return true;
    }

    /**
     * Gets the input stream associated with the client socket.
     *
     * @return The input stream, or null if it cannot be retrieved.
     */
    public InputStream getStreamReader() {
        try {
            return socket.getInputStream();
        } catch (IOException e) {
            logError("Failed to retrieve input stream.", e);
            return null;
        }
    }

    /**
     * Gets the output stream associated with the client socket.
     *
     * @return The output stream, or null if it cannot be retrieved.
     */
    public OutputStream getStreamWriter() {
        try {
            return socket.getOutputStream();
        } catch (IOException e) {
            logError("Failed to retrieve output stream.", e);
            return null;
        }
    }

    /**
     * Sends a handshake message to the client.
     *
     * @return true if the handshake message was sent successfully, false otherwise.
     */
    public boolean sendHandShake() {
        return write(null, TYPE_HAND_SHAKE);
    }

    /**
     * Checks if a handshake message was received from the client.
     *
     * @return true if a valid handshake message was received, false otherwise.
     */
    public boolean checkForHandShake() {
        String condition = readCondition();
        return TYPE_HAND_SHAKE.equals(condition);
    }

    /**
     * Checks if the input contains a valid handshake message.
     *
     * @param input The input to validate.
     * @return true if the input contains a valid handshake message, false otherwise.
     */
    public boolean checkForHandShake(String[] input) {
        return input.length > 0 && TYPE_HAND_SHAKE.equals(input[0]);
    }

    /**
     * Writes a condition message to the client socket.
     *
     * @param conditionType The type of condition to write.
     * @return true if the condition message was written successfully, false otherwise.
     */
    public boolean writeCondition(String conditionType) {
        if (!validInformation(conditionType)) {
            logError(ERROR_INVALID_INFORMATION, null);
            return false;
        }

        writer.println(DELIMITER_START + conditionType + DELIMITER_END);
        return true;
    }

    /**
     * Reads a condition message from the client socket.
     *
     * @return The condition message as a string, or null if reading fails.
     */
    public String readCondition() {
        try {
            String input = reader.readLine();
            if (input == null || !validInformation(input)) {
                return null;
            }
            return input;
        } catch (IOException e) {
            logError("Failed to read condition.", e);
            return null;
        }
    }

    /**
     * Logs an error message with an optional exception.
     *
     * @param message The error message to log.
     * @param e       The exception to log (optional, can be null).
     */
    private void logError(String message, Exception e) {
        System.err.println(message);
        if (e != null) {
            e.printStackTrace();
        }
    }
}
