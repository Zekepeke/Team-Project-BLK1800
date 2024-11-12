package src;

public class TerminatedThreads implements Runnable{
    public TerminatedThreads() {
        ;
    }

    public void run() {
        while(true) {
            for (int i = 0; i < ServerImplementation.activeConversations.size(); ) {
                if (ServerImplementation.activeConversations.get(i).isAlive()) {
                    ServerImplementation.activeConversations.remove(i);
                    ServerImplementation.sockets.remove(i);
                } else {
                    i++;
                }
            }
        }
    }
}
