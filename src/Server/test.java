package src.Server;

public class test {
    public static void main(String[] args) {
        ServerImplementation server = new ServerImplementation(8282);
        server.startup();
    }
}
