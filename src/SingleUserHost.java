import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SingleUserHost implements Runnable {
    ChatServer parent;

    Socket service;
    String username;

    PrintWriter outToUser;
    BufferedReader inFromUser;

    String outputFormat = "%s@%s(%d): %s";

    public SingleUserHost(ChatServer parent, String username, Socket location) throws IOException {
        this.parent = parent;

        this.service = location;
        this.username = username;

        this.outToUser = new PrintWriter(service.getOutputStream());
        this.inFromUser = new BufferedReader(new InputStreamReader(service.getInputStream()));
    }

    public void sendTo(String message) {
        outToUser.println(message);
    }

    public void sendFor(String message) {
        String finalOutput = String.format(outputFormat, username, service.getInetAddress(), service.getPort(), message);
        parent.ripple(this, finalOutput);
    }

    @Override
    public void run() {
        try {
            sendFor(inFromUser.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
