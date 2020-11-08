import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TCP {

    public static void writeAuthMessage(DataOutputStream outputStream, AuthenticationMessages auth, String payload) throws IOException {

        outputStream.writeByte(0); // Auth Phase
        outputStream.writeByte(auth.getValue()); // Auth Challenge
        int question_len = payload.getBytes().length; // Size of payload
        outputStream.writeInt(question_len); // write size
        outputStream.writeBytes(payload); // Write question
        outputStream.flush();
    }

    public static AuthenticationResult readAuthMessage(DataInputStream inputStream) throws IOException {
        boolean valid = true;
        if(inputStream.readByte() != 0){
            // If not auth phase not valid
            System.out.println("here");
            int rem = inputStream.available();
            inputStream.skipBytes(rem);
            return null;
        }
        int type = inputStream.readByte();
        if(type != 1 && type != 2 && type != 3){
            // If not auth request not valid
            int rem = inputStream.available();
            inputStream.skipBytes(rem);
            return new AuthenticationResult(AuthenticationMessages.Auth_Fail, "Bad request");
        }
        int payload_size = inputStream.readInt();
        String payload = new String(inputStream.readNBytes(payload_size));
        return new AuthenticationResult(AuthenticationMessages.getAuthMessage(type), payload);
    }

}