import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TCP {
    private static final int AUTH_PHASE = 0;
    private static final int QUERY_PHASE = 1;

    public static void writeAuthMessage(DataOutputStream outputStream, AuthenticationMessages auth, String payload) throws IOException {

        outputStream.writeByte(AUTH_PHASE); // Auth Phase
        outputStream.writeByte(auth.getValue()); // Auth Challenge
        int question_len = payload.getBytes().length; // Size of payload
        outputStream.writeInt(question_len); // write size
        outputStream.writeBytes(payload); // Write question
        outputStream.flush();
    }

    public static AuthenticationResult readAuthMessage(DataInputStream inputStream) throws IOException {
        boolean valid = true;
        if (inputStream.readByte() != AUTH_PHASE) {
            // If not auth phase not valid
            int rem = inputStream.available();
            inputStream.skipBytes(rem);
            return null;
        }
        int type = inputStream.readByte();
        if (type != 1 && type != 2 && type != 3) {
            // If not auth request not valid
            int rem = inputStream.available();
            inputStream.skipBytes(rem);
            return new AuthenticationResult(AuthenticationMessages.Auth_Fail, "Bad request");
        }
        int payload_size = inputStream.readInt();
        String payload = new String(inputStream.readNBytes(payload_size));
        return new AuthenticationResult(AuthenticationMessages.getAuthMessage(type), payload);
    }


    public static void writeQueryMessage(DataOutputStream outputStream, int token, WeatherRequest weatherRequest, String payload) throws IOException {
        outputStream.writeByte(QUERY_PHASE); // query Phase
        outputStream.writeInt(token);
        outputStream.writeByte(weatherRequest.getRequestType()); // Auth Challenge
        int question_len = payload.getBytes().length; // Size of payload
        outputStream.writeInt(question_len); // write size
        outputStream.writeBytes(payload); // Write question
        outputStream.flush();
    }

    public static byte[] readQueryDataResult(DataInputStream inputStream) throws IOException {
        /*if (inputStream.readByte() != QUERY_PHASE) {
            // If not query phase not valid
            int rem = inputStream.available();
            inputStream.skipBytes(rem);
            return null;
        }
//        int type = inputStream.readByte();
//
//        if (type != WeatherRequest.CurrentWeather &&
//                type != WeatherRequest.DailyForecastFor7Days &&
//                type != WeatherRequest.BasicWeatherMaps &&
//                type != WeatherRequest.MinuteForecast &&
//                type != WeatherRequest.HistoricalWeather) {
//            // If not auth request not valid
//            int rem = inputStream.available();
//            inputStream.skipBytes(rem);
//            return null;
//        }
        int payload_size = inputStream.readInt();
        return inputStream.readNBytes(payload_size);
         */
        return inputStream.readAllBytes();
    }

    public static String readQueryCommunicationResult(DataInputStream inputStream) throws IOException {
        if (inputStream.readByte() != QUERY_PHASE) {
            // If not query phase not valid
            int rem = inputStream.available();
            inputStream.skipBytes(rem);
            return null;
        }
//        int type = inputStream.readByte();
//
//        if (type != WeatherRequest.CurrentWeather &&
//                type != WeatherRequest.DailyForecastFor7Days &&
//                type != WeatherRequest.BasicWeatherMaps &&
//                type != WeatherRequest.MinuteForecast &&
//                type != WeatherRequest.HistoricalWeather) {
//            // If not auth request not valid
//            int rem = inputStream.available();
//            inputStream.skipBytes(rem);
//            return null;
//        }
        int payload_size = inputStream.readInt();
        String payload = new String(inputStream.readNBytes(payload_size));
        return payload;
    }
}