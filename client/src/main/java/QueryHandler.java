import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class QueryHandler {
    private ConnectionToServer connectionToServer;
    private ConnectionToServer connectionToData;

    public QueryHandler(ConnectionToServer connectionToServer, ConnectionToServer connectrionToData) {
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

    public static int constructFile(String filename, byte[] dataBytes) {
        int hashValue = 0;
        try {
            // todo add proper foldering
            FileOutputStream file = new FileOutputStream(filename);
            file.write(dataBytes);
            hashValue = file.hashCode();
            file.close();
            System.out.println(String.format("File saved at %s", filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashValue;
    }


    public static String getDate() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd_hh:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate;
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
            request = request.substring(request.indexOf(',') + 1);
            WeatherRequest weatherRequest = new WeatherRequest(requestType, request);
            int token = connectionToServer.getIntToken();
            TCP.writeQueryMessage(connectionToServer.outputStream, token, weatherRequest, request);
            // int hashValue = Integer.parseInt(TCP.readQueryCommunicationResult(connectionToServer.inputStream));
            int hashValue = connectionToServer.inputStream.readInt();
            long timestamp = connectionToServer.inputStream.readLong();
            System.out.println("Hash: " + hashValue);
            byte[] dataBytes = TCP.readQueryDataResult(connectionToData.inputStream);
            String fileExtension = "json";
            if (requestType == WeatherRequests.BasicWeatherMaps.getValue())
                fileExtension = "png";
            //             (requestType==WeatherRequests.BasicWeatherMaps.getValue()) : ".json";
            String filename = String.format("%d-%s-%d.%s", requestType, request, timestamp,fileExtension).replace(",", "_");
            int receivedHash = Arrays.hashCode(dataBytes);
            QueryHandler.constructFile(filename, dataBytes);
            while (receivedHash != hashValue) {
                connectionToServer.outputStream.writeInt("No".hashCode());
                // request retransfer
                System.out.println("Hash values mismatch, re-requesting file transfer.");
                TCP.writeQueryMessage(connectionToServer.outputStream, token, weatherRequest, request);
                hashValue = connectionToServer.inputStream.readInt();
                dataBytes = TCP.readQueryDataResult(connectionToData.inputStream);
                filename = String.format("%d-%s.%s", requestType, request, fileExtension);
                receivedHash = Arrays.hashCode(dataBytes);
                QueryHandler.constructFile(filename, dataBytes);
            }
            connectionToServer.outputStream.writeInt("Yes".hashCode()); // File received
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
