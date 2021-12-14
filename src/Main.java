import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        try {
            Client client = new Client(6666);
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            switch (args[0]) {
                case "receive":
                    client.recieve();
                    break;
                case "connect":
                    client.connect(args[1], Integer.parseInt(args[2]));
            }

            while (true) {
                String think = stdIn.readLine();
                if (think.equalsIgnoreCase("/end")) {
                    client.terminate();
                    break;
                }else {
                    client.send(think);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
