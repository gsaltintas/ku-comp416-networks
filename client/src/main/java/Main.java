
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
                String request = scanner.nextLine();
                while (scanner.hasNext() || !scanner.next().equalsIgnoreCase("quit")) {

                    requestHandler.request(request);
                    System.out.println("If you would like to quit type so.");
                    request = scanner.nextLine();

                }
                connectionToData.Disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        connectionToServer.Disconnect();
        /**
         * Pass the requests to the server
         * 5) Receive the hash value of the file on the Command socket
         * 6) Receive the files over the Data socket
         * 7) Confirm if the hash value corresponds to the file
         * 8) Request retransmit from the server if mismatch between hash value and file or failure to
         * receive file (Relevant String should be displayed in terminal). A scenario for this step
         * may be specifically designed for demonstration purposes.)
         * 9) Display the files in the appropriate manner.
         * 10) Terminate the connection in case an appropriate file is received and no other request
         * forwarded within the timeout duration. (Appropriate tests for demonstration purposes
         * should be developed.)
         */

        //connectionToServer.Disconnect();

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

/*
 connectionToServer.outputStream.writeByte(1); // Auth Phase
         connectionToServer.outputStream.writeInt(Integer.parseInt(connectionToServer.token)); // Auth Challenge
         connectionToServer.outputStream.writeByte(1);
         String payload = "Istanbul";
         int question_len = payload.getBytes().length; // Size of payload
         connectionToServer.outputStream.writeInt(question_len); // write size
         connectionToServer.outputStream.writeBytes(payload); // Write question
         connectionToServer.outputStream.flush();*/
