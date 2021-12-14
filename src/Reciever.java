import java.io.BufferedReader;
import java.io.IOException;

public class Reciever implements Runnable{
    BufferedReader br;
    boolean terminate = false;

    public Reciever(BufferedReader input) {
        br = input;
    }

    public void terminate() {
        terminate = true;
    }

    @Override
    public void run() {
        try {
            while (!terminate) {
                    System.out.println(br.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
