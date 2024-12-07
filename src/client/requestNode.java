package src.client;

public class RequestNode {
    String type;
    String[] content;

    public RequestNode(String[] content, String type) {
        this.content = content;
        this.type = type;
    }
}