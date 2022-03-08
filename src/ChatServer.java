import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer implements Runnable {
    private ServerSocket host;
    private ArrayList<SingleUserHost> lineup;

    private SingleUserHost terminator;

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
        System.out.println("working");
        //lineup.add(new SingleUserHost(this, hold));
        //debug
        terminator = new SingleUserHost(this, hold);
    }

    public void ripple(SingleUserHost user, String message) {
        System.out.println(message);
//        for(SingleUserHost r: lineup) {
//            if(r != user) {
//                r.sendTo(message);
//            }
//        }
        terminator.sendTo(message);
    }
    public void ripple(String message) {
        System.out.println(message);
//        for(SingleUserHost r: lineup) {
//            r.sendTo(message);
//        }
        terminator.sendTo(message);
    }

    public void end() throws IOException, InterruptedException {
        running = false;
        for(SingleUserHost r: lineup) {
            r.terminate();
        }
//        terminator = new Socket(InetAddress.getLocalHost(), host.getLocalPort());
//        lineup.get(0).terminate();
        //this now just throws an exception to end the server :trolldespair:
        //but it works ig
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
            while(running) {
                String think = masterReader.readLine();
                if (think.equals("/end")) {
                    end();
                } else {
                    ripple(think);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
