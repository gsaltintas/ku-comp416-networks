import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import static java.time.LocalDateTime.now;

public class Request {
    private ConnectionToServer connectionToServer;
    private ConnectionToServer connectionToData;

    public Request(ConnectionToServer connectionToServer, ConnectionToServer connectrionToData) {
        this.connectionToServer = connectionToServer;
        this.connectionToData = connectrionToData;
    }

    /***
     * displays available query options
     */
    public static void displayOptions() {
        // todo: add proper instructions
        System.out.println("You can execute following queries through WeatNet. To execute query, enter the corresponding integer value");
        for (WeatherRequests wr : WeatherRequests.values()) {
            System.out.println(wr.getValue() + ": " + wr.getDescription());
        }
    }


    /**
     * decodes the given string to an image/JSON
     */
    public static byte[] decodeFileData(String dataString) {
        return Base64.getDecoder().decode(dataString);
    }

    public static int constructFile(String filename, byte[] dataBytes) {
        int hashValue = 0;
        try {
            // todo add proper foldering
            FileOutputStream file = new FileOutputStream(filename);
            file.write(dataBytes);
            hashValue = file.hashCode();
            file.close();
            System.out.println(String.format("Image saved at %s", filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashValue;
    }

    public static void constructImage(String filename, String imgDataString) {
        try {
            byte[] imgBytes = decodeFileData(imgDataString);
            FileOutputStream imageFile = new FileOutputStream(filename);
            imageFile.write(imgBytes);
            imageFile.close();
            System.out.println(String.format("Image saved at %s", filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * given the request sends it to the server
     *
     * @param request
     * @throws IOException
     */
    public void request(String request) throws IOException {

        try {
            int requestType = Integer.parseInt(request.split(",")[0]);
            WeatherRequest weatherRequest = new WeatherRequest(requestType, request);
            int token = connectionToServer.getIntToken();
            TCP.writeQueryMessage(connectionToServer.outputStream, token, weatherRequest, request);
            byte[] dataBytes = TCP.readQueryDataResult(connectionToData.inputStream);
            int hashValue = Integer.parseInt(TCP.readQueryCommunicationResult(connectionToServer.inputStream));
            String filename = String.format("%d-%s-%s", requestType, request, now());
            int receivedHash = Request.constructFile(filename, dataBytes);
            if (receivedHash != hashValue) {
                // request retransfer
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

//
        // Ilk byte 1 olacak,
        // Token pass writeInt
        // Type da 1 byte
        // Size -> payload size
        // Payload -> Type a gore payload handle edilecek. Delimiter: ,

    }
}
