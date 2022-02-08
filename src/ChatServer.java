import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer implements Runnable {
    private ServerSocket host;
    private ArrayList<SingleUserHost> lineup;

    private Socket terminator;

    BufferedReader masterReader;
    Thread masterListener;
    boolean running;

    public ChatServer(int port) throws IOException {
        host = new ServerSocket(port);
        lineup = new ArrayList<>();

        masterReader = new BufferedReader(new InputStreamReader(System.in));
        masterListener = new Thread(this);
        masterListener.start();
        running = true;

        System.out.println("server running");
    }

    public void welcome() throws IOException {
        Socket hold = host.accept();
        lineup.add(new SingleUserHost(this, hold));
    }

    public void ripple(SingleUserHost user, String message) {
        for(SingleUserHost r: lineup) {
            if(r != user) {
                r.sendTo(message);
            }
        }
    }

    public void end() throws IOException, InterruptedException {
        running = false;
        for(SingleUserHost r: lineup) {
            r.terminate();
        }
        terminator = new Socket(host.getInetAddress(), host.getLocalPort());
        lineup.get(0).terminate();
        host.close();
    }

    public static void main(String[] args) throws IOException {
        ChatServer master = new ChatServer(Integer.parseInt(args[0]));
        while(master.running) {
            master.welcome();
        }
    }

    @Override
    public void run() {
        try {
            String think = masterReader.readLine();
            if(think.equals("/end")) {
                end();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
