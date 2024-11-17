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
 * Manages the low-level socket communication between the client and server, providing methods for reading
 * and writing data streams, validating information, and managing handshake operations.
 *
 * Implements the IO interface for input-output stream management.
 */
public class SocketIO implements IO {

    private final static String IMG_TOKEN = "|img|";

    class InnerClass {
        // Inner class members
    }

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public SocketIO(Socket socket) {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            writer.flush();
            this.socket = socket;
        } catch (IOException e) {
            reader = null;
            writer = null;
            e.printStackTrace();
        }
    }

    /**
     * Writes a data stream to the server with a specified information type.
     *
     * @param stream          an array of strings representing the data to be sent.
     * @param informationType the type of information being sent.
     * @return {@code true} if the data was successfully written;
     * {@code false} otherwise.
     */
    @Override
    public boolean write(String[] stream, String informationType) {

        if(stream.length == 0) {
            this.writer.print(DELIMITER_START);
            this.writer.print(informationType);
            this.writer.println(DELIMITER_END);
            return true;
        }

        this.writer.print(DELIMITER_START);
        this.writer.print(informationType);

        for(int i = 0; i < stream.length - 1; i++) {
            this.writer.print(stream[i]);
            this.writer.print(SPLITTER);
        }

        this.writer.print(stream[stream.length - 1]);
        
        this.writer.println(DELIMITER_END);

        return true;
    }

    /**
     * Validates the type of information received.
     *
     * @param information the information type to validate.
     * @return {@code true} if the information type is valid;
     * {@code false} otherwise.
     */
    public boolean validInformation(String information) {
        if(!information.equals(TYPE_HAND_SHAKE)) {
            return false;
        }
        return true;
    }

    /**
     * Reads a data stream from the server and parses it into an array of strings.
     *
     * @return an array of strings containing the parsed data, or {@code null} if an error occurs.
     */
    @Override
    public String[] read() {
        try {
            String input = this.reader.readLine();

            String[] information = input.split(SPLITTER);

            if(information[0].indexOf(DELIMITER_START) == -1) {return null;}
            if(information[information.length-1].indexOf(DELIMITER_END) == -1) {return null;}


            if(!validInformation(information[1])) {return null;}

            input = input.substring(DELIMITER_START.length(), input.length()-DELIMITER_END.length());

            information = input.split(SPLITTER);

            return information;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Sets the input stream for reading data from the server.
     *
     * @param stream the input stream to use.
     * @return {@code true} if the stream is successfully set; {@code false} otherwise.
     */
    @Override
    public boolean setStreamReader(InputStream stream) {
        reader = new BufferedReader(new InputStreamReader(stream));
        return true;
    }

    /**
     * Sets the output stream for writing data to the server.
     *
     * @param stream the output stream to use.
     * @return {@code true} if the stream is successfully set; {@code false} otherwise.
     */
    @Override
    public boolean setStreamWriter(OutputStream stream)
    {
        writer = new PrintWriter(stream);
        return true;
    }

    /**
     * Retrieves the input stream associated with the client socket.
     *
     * @return the {@code InputStream} for reading data, or {@code null} if an error occurs.
     */
    @Override
    public InputStream getStreamReader() {
        try{
            return this.socket.getInputStream();
        } catch(IOException e) {
            return null;
        }
    }

    /**
     * Retrieves the output stream associated with the client socket.
     *
     * @return the {@code OutputStream} for writing data, or {@code null} if an error occurs.
     */
    @Override
    public OutputStream getStreamWriter() {
        try{
            return this.socket.getOutputStream();
        } catch(IOException e) {
            return null;
        }
    }

    /**
     * Sends a handshake message to the server to establish a connection.
     *
     * @return {@code true} if the handshake message is successfully sent; {@code false} otherwise.
     */
    public boolean sendHandShake() {
        return this.write(null, TYPE_HAND_SHAKE);
    }

    /**
     * Checks if a handshake message is received from the server.
     *
     * @return {@code true} if the received message is a valid handshake; {@code false} otherwise.
     */
    public boolean checkForHandShake() {
        return readCondition().equals(TYPE_HAND_SHAKE);
    }

    /**
     * Checks if the given input represents a handshake message.
     *
     * @param input an array of strings representing the received data.
     * @return {@code true} if the input is a valid handshake message; {@code false} otherwise.
     */
    public boolean checkForHandShake(String[] input) {
        return input[0].equals(TYPE_HAND_SHAKE);
    }

    /**
     * Writes a specific condition type to the server.
     *
     * @param conditionType the type of condition to send.
     * @return {@code true} if the condition type is successfully written; {@code false} otherwise.
     */
    public boolean writeCondition(String conditionType) {
        if(validInformation(conditionType)) {return false;}
        writer.print(DELIMITER_START);
        writer.print(conditionType);
        writer.println(DELIMITER_END);
        return true;
    }

    /**
     * Reads a condition type from the server.
     *
     * @return the condition type as a {@code String}, or {@code null} if an error occurs or the condition is invalid.
     */
    public String readCondition() {
        try {
            String input = read()[0];

            if (!validInformation(input)) {
                return null;
            }
            return input;
        } catch (Exception e) {
            return null;
        }
    }
}