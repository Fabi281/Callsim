import java.util.Scanner;
import java.net.Socket;
import java.io.*;

/**
 * A command line client for the date server. Requires the IP address of the
 * server as the sole argument. Exits after printing the response.
 */
public class DateClient {
    private String host = "localhost";
    private void start(){

    }
    public static void main(String[] args) throws IOException {
        String host = "localhost";
        if (args.length != 1) {
            System.out.println("No param given for host, listening on localhost");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        var socket = new Socket(host, 59090);

        String msg;
        do {
            msg = reader.readLine();
            if (msg == "") {
                continue;
            }

            var in = new Scanner(socket.getInputStream());
            System.out.println("Server response: " + in.nextLine());

        }
            while (msg != "q");
    }
}