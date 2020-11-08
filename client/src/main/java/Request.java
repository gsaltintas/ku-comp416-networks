import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Scanner;

public class Request {
    public enum REQUEST {

    }

    public static String requestHeader = "Query";

    public Request() {

    }

    /***
     * displays available query options
     */
    public void displayOptions() {
        System.out.println("You can execute following queries through WeatNet. To execute query, enter the corresponding integer value");
        for (WeatherRequests wr : WeatherRequests.values()) {
            System.out.println(wr.getValue() + ": " + wr.getDescription());
        }
    }

    /**
     * given the
     */
    public String sendRequest(String reqType, Scanner scanner, ConnectionToServer connectionToServer) {
        String response = "";
        try {
            int reqValue = Integer.parseInt(reqType);
            System.out.println("Enter city: ");
            String city = scanner.nextLine();
            System.out.println(String.format("%s-%s-%s", requestHeader, reqType, city));
            response = connectionToServer.SendForAnswer(String.format("%s-%s-%s", requestHeader, reqType, city));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void getRequest() {

    }

    /**
     * decodes the given string to an image/JSON
     */
    public static byte[] decodeFileData(String dataString) {
        return Base64.getDecoder().decode(dataString);
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

}
