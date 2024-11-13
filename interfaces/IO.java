package interfaces;
import java.io.InputStream;
import java.io.OutputStream;

public interface IO {
    public boolean write(String[] stream, byte informationType);
    public String[] read();

    public boolean setStreamReader(InputStream stream);
    public boolean setStreamWriter(OutputStream stream);
    public OutputStream getStreamWriter();
    public InputStream getStreamReader();
}
