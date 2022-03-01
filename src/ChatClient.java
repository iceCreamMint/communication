import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient implements Runnable{
    boolean running;

    BufferedReader userInput;
    BufferedReader serverInput;
    PrintWriter toWriteToServer;
    Socket connection;

    Thread listener;

    public ChatClient(int fromPort, String connectTo, int toPort) throws IOException {
        listener = new Thread(this);
        connection = new Socket(connectTo, toPort);
        userInput = new BufferedReader(new InputStreamReader(System.in));
        serverInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        toWriteToServer = new PrintWriter(connection.getOutputStream());
        listener.start();
        running = true;

    }

    public void readUser() throws IOException {
        send(userInput.readLine());
    }

    public void send(String message) {
        toWriteToServer.println(message);
    }

    public void terminate() throws InterruptedException, IOException {
        listener.join();
        userInput.close();
        serverInput.close();
        toWriteToServer.close();
        connection.close();
    }

    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]));

        while(client.running) {
            client.readUser();
        }
    }

    @Override
    public void run() {
        try {
            String think = serverInput.readLine();
            if(think.equals("/leave")) {
                running = false;
                terminate();
            }else {
                System.out.println(think);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
