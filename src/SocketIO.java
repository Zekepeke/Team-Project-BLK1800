package src;
import interfaces.IO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketIO implements IO {

    private final static String IMG_TOKEN = "|img|";

    private final byte DELIMITER_START_BYTE = 0x01;
    private final byte DELIMITER_END_BYTE = 0x02;
    private final byte SPLITTER_BYTE = 0x03;

    public static final byte TYPE_BYTE_FRIEND_INFO_UPDATE = 0x04;
    public static final byte TYPE_BYTE_MESSAGE = 0x05;
    public static final byte TYPE_BYTE_SIGNUP = 0x06;
    public static final byte TYPE_BYTE_LOGIN = 0x06;
    public static final byte TYPE_BYTE_HAND_SHAKE = 0x07;

    //usersignup informatics
    public static final byte SUCCESS_BYTE_USER_SIGNUP = 0x08;
    public static final byte ERROR_BYTE_USER_EXISTS = 0x09;
    public static final byte TYPE_USER_SIGNUP_INFORMATION = 0x0A;

    //user login informatics
    public static final byte SUCCESS_BYTE_USER_LOGIN = 0x0B;
    public static final byte ERROR_BYTE_USER_DNE = 0x0C;
    public static final byte ERROR_BYTE_PASSWORD = 0x0D;
    public static final byte TYPE_USER_LOGIN_INFORMATION = 0x0E;

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

    @Override
    public boolean write(String[] stream, byte informationType) {

        if(stream.length == 0) {
            this.writer.print(DELIMITER_START_BYTE);
            this.writer.print(informationType);
            this.writer.println(DELIMITER_END_BYTE);
            return true;
        }

        this.writer.print(DELIMITER_START_BYTE);
        this.writer.print(informationType);

        for(int i = 0; i < stream.length - 1; i++) {
            this.writer.print(stream[i]);
            this.writer.print(SPLITTER_BYTE);
        }

        this.writer.print(stream[stream.length - 1]);
        
        this.writer.println(DELIMITER_END_BYTE);

        return true;
    }

    public boolean validInformationByte(byte informationByte) {
        if(informationByte != TYPE_BYTE_FRIEND_INFO_UPDATE) {
            return false;
        }
        return true;
    }

    @Override
    public String[] read() {

        String input = this.reader.readLine();

        if(input.charAt(0) != DELIMITER_START_BYTE) {return null;}
        if(input.charAt(input.length() - 1) != DELIMITER_END_BYTE) {return null;}

        byte informationType = (byte)input.charAt(1);

        if(!validInformationByte(informationType)) {return null;}

        input = input.substring(1, input.length()-1);

        String[] information = input.split(Character.toString(SPLITTER_BYTE));

        return information;
    }

    @Override
    public boolean setStreamReader(InputStream stream) {
        reader = new BufferedReader(new InputStreamReader(stream));
        return true;
    }

    @Override
    public boolean setStreamWriter(OutputStream stream)
    {
        writer = new PrintWriter(stream);
        return true;
    }

    @Override
    public InputStream getStreamReader() {
        try{
            return this.socket.getInputStream();
        } catch(IOException e) {
            return null;
        }
    }

    @Override
    public OutputStream getStreamWriter() {
        try{
            return this.socket.getOutputStream();
        } catch(IOException e) {
            return null;
        }
    }

    public boolean sendHandShake() {
        return this.write(null, TYPE_BYTE_HAND_SHAKE);
    }

    public boolean checkForHandShake(String[] input) {
        return input != null && input[0].charAt(0) == TYPE_BYTE_HAND_SHAKE;
    }

    public boolean writeCondition(byte conditionType) {
        if(validInformationByte(conditionType)) {return false;}
        writer.print(DELIMITER_START_BYTE);
        writer.print(conditionType);
        writer.println(DELIMITER_END_BYTE);
        return true;
    }

    public byte readCondition() {
        String input = reader.readLine();
        input = input.substring(1, input.length()-1);
        byte type = (byte)input.charAt(0);
        if(!validInformationByte(type)) {return 0x00;}
        return type;
    }
}