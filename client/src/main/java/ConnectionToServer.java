import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * 1) Confirm its authenticity with the server 🗸
 * 2) Be able to submit requests to the server. 🗸
 * 3) Be able to receive data in form of JSON/image files from the server over the Data
 * socket. 🗸
 * 4) Verify the file based on its digital signature. 🗸
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
    private static long TIMEOUT = 10000;
    private Socket socket;
    protected DataInputStream inputStream;
    protected DataOutputStream outputStream;
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
            socket = new Socket(serverAddress, serverPort);
            //br= new BufferedReader(new InputStreamReader(System.in));
            /*
            Read and write buffers on the socket
             */
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
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
     * Disconnects the socket and closes the buffers
     */
    public void Disconnect() {
        try {
            serverClosedSocket = true;
            inputStream.close();
            outputStream.close();
            //br.close();
            socket.close();
            System.out.println(String.format("ConnectionToServer. SendForAnswer. Connection Closed at %s port: %s", socket.getInetAddress(), socket.getLocalSocketAddress()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * checks if the connection is still active
     */
    public boolean isConnectionActive() {
        return this.socket != null && this.socket.isConnected() && !this.socket.isClosed() && !this.serverClosedSocket;
    }

    public int getIntToken() {
        if (this.token != null) {
            return Integer.parseInt(this.token);
        }
        return 0;
    }
}
