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
        String response = connection.SendForAnswer(AuthenticationMessages.Auth_Request + AuthenticationMessages.DELIMITER + message);
        while (!response.startsWith(AuthenticationMessages.Auth_Fail.toString()) &&
                !response.startsWith(AuthenticationMessages.Auth_Success.toString()) &&
                !message.equalsIgnoreCase("quit")) {
            System.out.println("Response from server: " + response);
            message = scanner.nextLine();
            response = connection.SendForAnswer(AuthenticationMessages.Auth_Request + AuthenticationMessages.DELIMITER + message);
            // todo: handle client timeout
            /**
             long start = System.currentTimeMillis();
             long now = System.currentTimeMillis();
             while (start + TIMEOUT > System.currentTimeMillis()) {
             //
             System.out.flush();
             ;
             }
             if (scanner.hasNext()) {
             message = scanner.nextLine();
             response = SendForAnswer(AuthenticationMessages.Auth_Request + AuthenticationMessages.DELIMITER + message);

             } else {

             try {
             response = is.readLine();
             System.out.println(response);
             } catch (IOException e) {
             e.printStackTrace();
             }

             System.out.println("Exited");
             System.out.flush();
             scanner.reset();
             return "exit";
             }

             if (!scanner.hasNextLine()) {
             try {
             response=is.readLine();
             } catch (IOException e) {
             e.printStackTrace();
             }
             return  "failed";
             } else {
             message = scanner.nextLine();
             response = SendForAnswer(AuthenticationMessages.Auth_Request + AuthenticationMessages.DELIMITER + message);

             }
             try {
             this.s.setSoTimeout(TIMEOUT);
             if (scanner.hasNextLine()) {
             //todo: handle client timeout

             message = scanner.nextLine();
             response = SendForAnswer(AuthenticationMessages.Auth_Request + AuthenticationMessages.DELIMITER + message);
             }

             } catch (SocketException e) {
             e.printStackTrace();
             }

             while(!System.in.ready()) {
             Thread.sleep(TIMEOUT);
             }
             while (start + TIMEOUT < System.currentTimeMillis() && !scanner.hasNextLine()) {

             }
             if (start + TIMEOUT >= System.currentTimeMillis() && !scanner.hasNextLine()) {
             try {
             response=is.readLine();
             } catch (IOException e) {
             e.printStackTrace();
             }
             } else {
             message = scanner.nextLine();
             response = SendForAnswer(AuthenticationMessages.Auth_Request + AuthenticationMessages.DELIMITER + message);
             }
             */
        }
        if (response.startsWith(AuthenticationMessages.Auth_Fail.toString())) {
            System.out.println("Response from server: " + response);
            return "";
        } else if (response.startsWith(AuthenticationMessages.Auth_Success.toString())) {
            System.out.println("Response from server: " + response);
            token = response.split(AuthenticationMessages.DELIMITER)[1];
            return token;
        }
        return "";
    }

}
