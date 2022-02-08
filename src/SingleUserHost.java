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

    Thread listener;

    public SingleUserHost(ChatServer parent, Socket location) throws IOException {
        this.parent = parent;

        this.service = location;

        this.outToUser = new PrintWriter(service.getOutputStream());
        this.inFromUser = new BufferedReader(new InputStreamReader(service.getInputStream()));

        sendTo("name: ");
        this.username = inFromUser.readLine();

        sendFor("joined the room");

        listener.start();
    }

    public void sendTo(String message) {
        outToUser.println(message);
    }

    public void sendFor(String message) {
        String finalOutput = String.format(outputFormat, username, service.getInetAddress(), service.getPort(), message);
        parent.ripple(this, finalOutput);
    }

    public void terminate() throws InterruptedException, IOException {
        listener.join();
        inFromUser.close();
        sendTo("closing connection");
        outToUser.close();
        service.close();
    }

    @Override
    public void run() {
        try {
            String think = inFromUser.readLine();
            if(think.equals("/leave")) {
                sendFor(username + "has left the room");
                terminate();
            }else {
                sendFor(inFromUser.readLine());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}