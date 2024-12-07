package src;
import Exceptions.DisconnectException;
import interfaces.IO;
import jdk.jshell.Diag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Handles socket-based input/output communication with a client.
 * Manages the low-level socket communication between the client and server, providing methods for reading
 * and writing data streams, validating information, and managing handshake operations.
 * Handles socket-based input/output communication with a client.
 * Implements the IO interface for input-output stream management.
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

    public SocketIO(String host, int port) {
        Socket socketTemp;
        try {
            socketTemp = new Socket(host, port);
            this.reader = new BufferedReader(new InputStreamReader(socketTemp.getInputStream()));
            this.writer = new PrintWriter(socketTemp.getOutputStream(), true);
        } catch (IOException e) {
            logError("Failed to initialize socket IO streams.", e);
            socketTemp = null;
        }
        this.socket = socketTemp;
    }

    /**
     * Writes data to the client socket.
     * Writes a data stream to the server with a specified information type.
     * Initializes a new SocketIO instance for client communication.
     * @param stream          The data to write as a string array.
     * @param informationType The type of information being sent.
     * @return {@code true} if the data was successfully written;
     * {@code false} otherwise.
     */
    public boolean write(String[] stream, String informationType) throws DisconnectException {
        if (writer == null) {
            throw new DisconnectException("Failed to write");
        }

        StringBuilder message = new StringBuilder(DELIMITER_START).append(informationType);
        if (stream != null && stream.length > 0) {
            for (String part : stream) {
                message.append(SPLITTER);

                if(part.contains("\n")) {
                    message.append(String.join(NEW_LINE, part.split("\n")));
                } else {
                    message.append(part);
                }
            }
        }
        message.append(DELIMITER_END);

        writer.println(message);
        return true;
    }

    public boolean write(String... data) throws DisconnectException {

        if(data.length == 1) {
            write(null, data[0]);
        }
        else if(data.length > 1){
            String[] rawData = new String[data.length - 1];
            System.arraycopy(data, 0, rawData, 0, data.length - 1);

            String informationType = data[data.length - 1];

            write(rawData, informationType);
        }
        else {
            return false;
        }

        return true;
    }

    /**
     * Reads data from the client socket.
     * Validates the type of information received.
     * Writes data to the client socket.
     *
     *
     * @return The data read as a string array, or null if reading fails.
     */
    public String[] read() throws DisconnectException {
        try {
            String input = reader.readLine();

            input = input.substring(DELIMITER_START.length(), input.length() - DELIMITER_END.length());

            if(!input.contains(SPLITTER)) {return new String[]{input};};

            String[] a = input.split(SPLITTER);
            
            for(int i = 1; i < a.length; i++) {
                if(a[i].contains(NEW_LINE)) {
                    String[] split = a[i].split(NEW_LINE);
                    a[i] = String.join("\n", split);
                }
            }
            return a;

        } catch (IOException e) {
            throw new DisconnectException("Failed to read from socket.");
        }
    }

    /**
     * Validates the information type received from the client.
     *  Writes data to the client socket.
     *
     * @param information The information type to validate.
     * @return {@code true} if the information type is valid;
     * {@code false} otherwise.
     */
    public boolean validInformation(String information) {
        return true;
    }

    /**
     * Sets a new input stream for reading from the client socket.
     * Reads a data stream from the server and parses it into an array of strings.
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
     * @return {@code true} if the stream is successfully set; {@code false} otherwise.
     */
    public boolean setStreamWriter(OutputStream stream) {
        this.writer = new PrintWriter(stream, true);
        return true;
    }

    /**
     * Gets the input stream associated with the client socket.
     *
     * @return the {@code InputStream} for reading data, or {@code null} if an error occurs.
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
     * @return the {@code OutputStream} for writing data, or {@code null} if an error occurs.
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
     * @return {@code true} if the handshake message is successfully sent; {@code false} otherwise.
     */
    public boolean sendHandShake() {
        return write(null, TYPE_HAND_SHAKE);
    }

    /**
     * Checks if a handshake message was received from the client.
     *
     * @return {@code true} if the received message is a valid handshake; {@code false} otherwise.
     */
    public boolean checkForHandShake() {
        String condition = readCondition();
        return TYPE_HAND_SHAKE.equals(condition);
    }

    /**
     * Checks if the input contains a valid handshake message.
     *
     * @param input The input to validate.
     * @return {@code true} if the input is a valid handshake message; {@code false} otherwise.
     */
    public boolean checkForHandShake(String[] input) {
        return input.length > 0 && TYPE_HAND_SHAKE.equals(input[0]);
    }

    /**
     * Writes a condition message to the client socket.
     *
     * @param conditionType The type of condition to write.
     * @return {@code true} if the condition type is successfully written; {@code false} otherwise.
     */
    public boolean writeCondition(String conditionType) throws DisconnectException {
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
    public String readCondition() throws DisconnectException {
        return read()[0];
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

    public boolean close() {
        try {
            this.socket.close();
            reader.close();
            writer.close();
            return true;
        } catch (IOException e) {
            logError("Failed to close socket stream", e);
            return false;
        }
    }
}
