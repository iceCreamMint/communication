import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatClient implements Runnable{
    boolean running;

    BufferedReader userInput;
    BufferedReader serverInput;
    PrintWriter toWriteToServer;
    Socket connection;
    ServerSocket server;

    Thread listener;

    public ChatClient(int fromPort, String connectTo, int toPort) throws IOException {
        server = new ServerSocket(fromPort);
        System.out.println("server established");
        connection = new Socket(connectTo, toPort);
        toWriteToServer = new PrintWriter(connection.getOutputStream(), true);
        toWriteToServer.println(fromPort);
        serverInput = new BufferedReader(new InputStreamReader(server.accept().getInputStream()));
        running = true;
        listener = new Thread(this);
        listener.start();
    }

//    public void connect(String connectTo, int toPort) throws IOException {
//        connection = new Socket(connectTo, toPort);
//        serverInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        toWriteToServer = new PrintWriter(connection.getOutputStream(), true);
//        toWriteToServer.println("connect");
//        listener = new Thread(this);
//        listener.start();
//    }

    public void readUser() throws IOException, InterruptedException {
        String think = userInput.readLine();
        if(think.equalsIgnoreCase("/leave")) {
            running = false;
            terminate();
        }else {
            send(think);
        }
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

    public static void main(String[] args) throws IOException, InterruptedException {
        ChatClient client = new ChatClient(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]));

        client.userInput = new BufferedReader(new InputStreamReader(System.in));
        while(client.running) {
            client.readUser();
        }
    }

    @Override
    public void run() {
        try {
            while (running) {
                String think = serverInput.readLine();
                System.out.println(think);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
