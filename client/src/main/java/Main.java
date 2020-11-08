
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String args[]) {

        ConnectionToServer connectionToServer = new ConnectionToServer(ConnectionToServer.DEFAULT_SERVER_ADDRESS, ConnectionToServer.DEFAULT_SERVER_PORT);
        connectionToServer.Connect();
        Scanner scanner = new Scanner(System.in);
        boolean authenticated = connectionToServer.authenticate(scanner);
        if (authenticated) {
            try {
                int dataPort = connectionToServer.inputStream.readInt(); // get the address of the data socket
                System.out.println(dataPort);
                // establish a connection to the data port
                ConnectionToServer connectionToData = new ConnectionToServer(ConnectionToServer.DEFAULT_SERVER_ADDRESS, dataPort);
                connectionToData.Connect();

                Request requestHandler = new Request(connectionToServer, connectionToData);
                requestHandler.displayOptions();
                while (scanner.hasNext() || !scanner.next().equalsIgnoreCase("quit")) {

                    String request = scanner.nextLine();
                    requestHandler.request(request);
                    System.out.println("If you would like to quit type so.");
                }
                connectionToData.Disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        connectionToServer.Disconnect();
        
    }
}

