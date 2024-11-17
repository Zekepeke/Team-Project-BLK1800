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

    public boolean validInformation(String information) {
        if(!information.equals(TYPE_HAND_SHAKE)) {
            return false;
        }
        return true;
    }

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
        return this.write(null, TYPE_HAND_SHAKE);
    }

    public boolean checkForHandShake() {
        return readCondition().equals(TYPE_HAND_SHAKE);
    }

    public boolean checkForHandShake(String[] input) {
        return input[0].equals(TYPE_HAND_SHAKE);
    }

    public boolean writeCondition(String conditionType) {
        if(validInformation(conditionType)) {return false;}
        writer.print(DELIMITER_START);
        writer.print(conditionType);
        writer.println(DELIMITER_END);
        return true;
    }

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