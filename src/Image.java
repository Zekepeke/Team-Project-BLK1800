package src;

public class Image {

    private byte[] imageData;
    private int dataLength;

    public Image(byte[] data, int length) {
        imageData = data;
        dataLength = length;
    }
}
