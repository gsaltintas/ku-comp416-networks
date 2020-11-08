import java.io.IOException;
import java.util.Scanner;

public class Authentication {

    private static Authentication authentication = null;
    private static int TIMEOUT = 20000;
    protected String token;

    private Authentication() {
    }

    public static Authentication getInstance() {
        if (authentication == null)
            authentication = new Authentication();
        return authentication;
    }

    public String authenticate(Scanner scanner, ConnectionToServer connection) {
        System.out.println("Enter your username");
        String message = scanner.nextLine();

        String response;

        try {
            TCP.writeAuthMessage(connection.outputStream, AuthenticationMessages.Auth_Request, message);
            AuthenticationResult authRes = TCP.readAuthMessage(connection.inputStream);

            while(authRes.result != AuthenticationMessages.Auth_Fail &&
                    authRes.result != AuthenticationMessages.Auth_Success &&
                    !message.equalsIgnoreCase("quit")) {
                System.out.println("Response from server: " + authRes.customMessage);
                message = scanner.nextLine();
                TCP.writeAuthMessage(connection.outputStream, AuthenticationMessages.Auth_Request, message);
                authRes = TCP.readAuthMessage(connection.inputStream);

            }
            if (authRes.result == AuthenticationMessages.Auth_Fail) {
                System.out.println("Response from server: " + authRes.customMessage);
                return "";
            } else if (authRes.result == AuthenticationMessages.Auth_Success) {
                System.out.println("Response from server: " + authRes.customMessage);
                return authRes.customMessage;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
