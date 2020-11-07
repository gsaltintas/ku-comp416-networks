
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 1) Confirm its authenticity with the server 🗸
 * 2) Be able to submit requests to the server. 🗸
 * 3) Be able to receive data in form of JSON/image files from the server over the Data
 * socket.
 * 4) Verify the file based on its digital signature.
 * 5) Display the JSON data in tabular form or display the image on the client side
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Client-Server Interaction
 * The client side must take care of the parameters to be passed once requesting any metric.
 * The process at the client side will follow the given steps:
 * 1) Initiate connection with the server over Command Socket
 * 2) Authenticate based on the server requirements.
 * 3) Receive the parameters for Data Socket and connect to that after authentication.
 * 4) Pass the requests to the server
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
public class ConnectionToServer {

    public static final String DEFAULT_SERVER_ADDRESS = "localhost";
    public static final int DEFAULT_SERVER_PORT = 4444;
    public static final int DEFAULT_DATA_PORT = 4443;
    private boolean serverClosedSocket;
    //    public static final
    private static long TIMEOUT = 10000;
    private Socket s;
    //private BufferedReader br;
    protected BufferedReader is;
    protected PrintWriter os;
    protected String token;
    Authentication auth;

    protected String serverAddress;
    protected int serverPort;


    /**
     * @param address IP address of the server, if you are running the server on the same computer as client, put the address as "localhost"
     * @param port    port number of the server
     */
    public ConnectionToServer(String address, int port) {
        serverAddress = address;
        serverPort = port;
    }

    /**
     * Establishes a socket connection to the server that is identified by the serverAddress and the serverPort
     */
    public void Connect() {
        try {
            s = new Socket(serverAddress, serverPort);
            //br= new BufferedReader(new InputStreamReader(System.in));
            /*
            Read and write buffers on the socket
             */
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = new PrintWriter(s.getOutputStream());

            this.serverClosedSocket = false;
            System.out.println("Successfully connected to " + serverAddress + " on port " + serverPort);
        } catch (EOFException e) {
            this.serverClosedSocket = true;
        } catch (IOException e) {
            //e.printStackTrace();
            System.err.println("Error: no server has been found on " + serverAddress + "/" + serverPort);
        }
        auth = Authentication.getInstance();
    }

    public boolean authenticate(Scanner scanner) {
        this.token = this.auth.authenticate(scanner, this);
        return this.token != "";
    }

    /**
     * sends the message String to the server and retrives the answer
     *
     * @param message input message string to the server
     * @return the received server answer
     */
    public String SendForAnswer(String message) {
        String response = new String();
        try {
            /*
            Sends the message to the server via PrintWriter
             */
            os.println(message);
            os.flush();
            /*
            Reads a line from the server via Buffer Reader
             */

            response = is.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ConnectionToServer. SendForAnswer. Socket read Error");
        }
        return response;
    }


    /**
     * Disconnects the socket and closes the buffers
     */
    public void Disconnect() {
        try {
            serverClosedSocket = true;
            is.close();
            os.close();
            //br.close();
            s.close();
            System.out.println(String.format("ConnectionToServer. SendForAnswer. Connection Closed at %s port: %s", s.getInetAddress(), s.getLocalSocketAddress()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** checks if the connection is still active */
    public boolean isConnectionActive() {
        return this.s != null && this.s.isConnected() && !this.s.isClosed() && !this.serverClosedSocket;
    }
}
