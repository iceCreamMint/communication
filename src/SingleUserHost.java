import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SingleUserHost implements Runnable {
    ChatServer parent;

    Socket serviceIn;
    Socket serviceOut;
    String username;

    PrintWriter outToUser;
    BufferedReader inFromUser;

    String outputFormat = "%s@%s(%d): %s";

    Thread listener;

    public SingleUserHost(ChatServer parent, Socket location) throws IOException {
        this.parent = parent;
        this.serviceIn = location;

        this.inFromUser = new BufferedReader(new InputStreamReader(serviceIn.getInputStream()));

        serviceOut = new Socket(serviceIn.getInetAddress(), Integer.parseInt(inFromUser.readLine()));
        this.outToUser = new PrintWriter(serviceOut.getOutputStream(), true);

        outToUser.println("give name");
        this.username = inFromUser.readLine();

        sendFor("joined the room");

        listener = new Thread(this);
        listener.start();
    }

    public void sendTo(String message) {
        outToUser.println(message);
    }

    public void sendFor(String message) {
        String finalOutput = String.format(outputFormat, username, serviceIn.getInetAddress(), serviceIn.getPort(), " " + message);
        parent.ripple(this, finalOutput);
    }

    public void terminate() throws InterruptedException, IOException {
        inFromUser.close();
        sendTo("server is closing connection");
        sendTo("/leave");
        outToUser.close();
        serviceIn.close();
        serviceOut.close();
        listener.join();
    }

    @Override
    public void run() {
        try {
            while(parent.running) {
                String think = inFromUser.readLine();
                if (think.equals("/leave")) {
                    sendFor(username + "has left the room");
                    terminate();
                } else {
                    sendFor(think);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
