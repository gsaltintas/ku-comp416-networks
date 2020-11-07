
import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        ConnectionToServer connectionToServer = new ConnectionToServer(ConnectionToServer.DEFAULT_SERVER_ADDRESS, ConnectionToServer.DEFAULT_SERVER_PORT);
        connectionToServer.Connect();
        Scanner scanner = new Scanner(System.in);
        boolean authenticated = connectionToServer.authenticate(scanner);
        if (authenticated) {
            ConnectionToServer connectionToData = new ConnectionToServer(ConnectionToServer.DEFAULT_SERVER_ADDRESS, ConnectionToServer.DEFAULT_DATA_PORT);
            connectionToData.Connect();

        }

        connectionToServer.Disconnect();

//        if (token != "") {
//            System.out.println("Authentication successfull");
//        }
//        String response = connectionToServer.SendForAnswer(AuthenticationMessages.Auth_Request + AuthenticationMessages.DELIMITER + message);
//        while (!response.startsWith(AuthenticationMessages.Auth_Fail.toString()) &&
//                !response.startsWith(AuthenticationMessages.Auth_Success.toString()) &&
//                !message.equalsIgnoreCase("quit")) {
//            System.out.println("Response from server: " + response);
//
//            message = scanner.nextLine();
//            response = connectionToServer.SendForAnswer(AuthenticationMessages.Auth_Request + AuthenticationMessages.DELIMITER + message);
//        }
//        if (response.startsWith(AuthenticationMessages.Auth_Fail.toString()) ||
//                response.startsWith(AuthenticationMessages.Auth_Success.toString())) {
//            System.out.println("Response from server: " + response);
//        }

//        System.out.println("Response from server: " + response);
//        String message=scanner.nextLine();
//        while (!message.equals("QUIT")) {
//            if (message.split(AuthenticationMessages.DELIMITER)[0] == AuthenticationMessages.Auth_Challenge.toString()) {
//                System.out.println("");
//
////            System.out.println("Response from server: " + connectionToServer.SendForAnswer(message));
//            }
//            message = scanner.nextLine();
//        }
//        connectionToServer.Disconnect();
    }
}
