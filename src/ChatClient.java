import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChatClient implements Runnable{
    BufferedReader toWriteToServer;

    public ChatClient() {
        toWriteToServer = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {

    }
}
