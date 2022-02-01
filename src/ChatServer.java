import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    private ServerSocket host;
    private ArrayList<SingleUserHost> lineup;

    public ChatServer(int port) throws IOException {
        host = new ServerSocket(port);
        lineup = new ArrayList<>();
    }

    public void welcome() throws IOException {
        Socket hold = host.accept();

    }

    public void ripple(SingleUserHost user, String message) {
        for(SingleUserHost r: lineup) {
            if(r != user) {
                r.sendTo(message);
            }
        }
    }
}
