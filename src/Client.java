import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Client implements Runnable{
    private ServerSocket server;
    private Socket guest;
    private BufferedReader in;//traffic coming in
    private PrintWriter out;
    private boolean terminate = false;

    public Client(int port) throws IOException {
        server = new ServerSocket(port);
        System.out.println("server established");
    }

    public void recieve() throws IOException {
        System.out.println("server listening " + server.getInetAddress() + " at port " + server.getLocalPort());
        guest = server.accept();
        in = new BufferedReader(new InputStreamReader(guest.getInputStream()));
        out = new PrintWriter(guest.getOutputStream(), true);
        run();
        out.println("connection established");
    }

    public void connect(String address, int port) throws IOException {
        guest = new Socket(address, port);
        in = new BufferedReader(new InputStreamReader(guest.getInputStream()));
        out = new PrintWriter(guest.getOutputStream(), true);
        String confirm = in.readLine();
        run();
        out.println("connection established");
    }

    public void terminate() throws IOException {
        terminate = true;
        out.println("connection terminated");
        server.close();
        guest.close();
        in.close();
        out.close();
    }

    public void send(String text){
        out.println(text);
    }

    @Override
    public void run() {
        try {
            while (!terminate) {
                System.out.println(in.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
