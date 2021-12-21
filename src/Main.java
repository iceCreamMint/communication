import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {// java Main <function> [from port] [to ip] [to port]
        try {
            Client client = new Client(Integer.parseInt(args[1]));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            if (args[0].equalsIgnoreCase("recieve")) {
                client.recieve();
            }else if (args[0].equalsIgnoreCase("connect")){
                client.connect(args[2], Integer.parseInt(args[3]));
            }else {
                System.out.println("unrecognized");
                return;
            }

            System.out.println("exited connection stage");

            while (true) {
                String think = stdIn.readLine();
                if (think.equalsIgnoreCase("/end")) {
                    client.terminate();
                    break;
                }else {
                    client.send(think);
                }
            }
        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
