import java.io.IOException;
import java.io.*;
import java.net.Socket;

public class Sender implements Runnable {
    PrintWriter pw;
    BufferedReader br;

    public Sender(BufferedReader userin, PrintWriter sendout) {
        this.pw = sendout;
        this.br = userin;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String userin = br.readLine();
                if(userin == "/exit") {
                    pw.println("user has exited communication");
                    break;
                }else {
                    pw.println(userin);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
