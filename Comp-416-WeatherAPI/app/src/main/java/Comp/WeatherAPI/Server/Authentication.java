package Comp.WeatherAPI.Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;

import static java.lang.Math.min;

public class Authentication {
    private static int MAX_QUESTIONS = 5;

    private static Authentication authentication = null;
    private static int TIMEOUT = 20000;

    private Authentication() {
    }

    public static Authentication getInstance() {
        if (authentication == null)
            authentication = new Authentication();
        return authentication;
    }

    /***
     * checks if given user is registered
     */
    public boolean validateUser(String username) {

        username += ".txt";
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File userFile = new File(classLoader.getResource(username).getFile());
            return userFile.exists();
        } catch (NullPointerException e) {
            return false;
        }
    }

    /*** given the username, looksup and loads the contents of the authentication details related to this user to a HashMap **/
    private HashMap<String, String> loadUserDetails(String username) {
        ClassLoader classLoader = getClass().getClassLoader();
        String filePath = username + ".txt";
        File userFile = new File(classLoader.getResource(filePath).getFile());
        Scanner reader = null;
        try {
            reader = new Scanner(userFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HashMap<String, String> userDetails = new HashMap<>();
        int i = 0;
        String prevQuestion = "";
        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            if (i % 2 == 0) {
                prevQuestion = data;
            } else {
                userDetails.put(prevQuestion, data);
            }
            i += 1;
        }
        return userDetails;
    }

    /**
     * checks if the user is registered in the database
     * loads the authorization details related to the user
     * asks random number of questions
     */
    public AuthenticationResult authorize(String username, ServerThread st) {

//        if (!validateUser(username)) {
//            System.out.println("User doesn't exist in the database.");
//            return false;
//        }
        String cause = "";
        AuthenticationResult authRes = new AuthenticationResult(AuthenticationMessages.Auth_Success, cause, true);
        Random rand = new Random();
        HashMap<String, String> userDetails = loadUserDetails(username);
        int numQuestions = min(MAX_QUESTIONS, rand.nextInt(userDetails.size()) + 1);
        List<String> allQuestions = new ArrayList<String>(userDetails.keySet());
        Collections.shuffle(allQuestions, rand);
        List<String> questions = allQuestions.subList(0, numQuestions);
        for (String question : questions) {
            String correctAnswer = userDetails.get(question);
            try {
                st.outputStream.println(AuthenticationMessages.Auth_Challenge + AuthenticationMessages.DELIMITER + question);
                st.outputStream.flush();
                st.socket.setSoTimeout(TIMEOUT);
                String givenAnswer = st.inputStream.readLine();
                String[] givenAnswers = givenAnswer.split(AuthenticationMessages.DELIMITER);
                givenAnswer = givenAnswers[givenAnswers.length - 1];
                if (!givenAnswer.equalsIgnoreCase(correctAnswer)) {
                    authRes.fail();
                    authRes.customMessage = "Wrong answer";
                    return authRes;
                }
            } catch (SocketTimeoutException e) {

                authRes.fail();
                authRes.customMessage = "Timeout";
                System.out.println("Closing connection due to timeout.");
                return authRes;
            } catch (IOException e) {
                authRes.fail();
                authRes.customMessage = e.getMessage();
                return authRes;
            }
        }
        int token = (username + rand.nextInt()).hashCode();
        while (token < Math.pow(10, 6)) {
            token *= 10;
            token += rand.nextInt(10);
        }
        authRes.customMessage = ("" + token).substring(0, 6);
        return authRes;

    }

    public class AuthenticationResult {
        public AuthenticationMessages result;
        public String customMessage;
        public boolean authenticated;

        public AuthenticationResult(AuthenticationMessages result, String customMessage, boolean authenticated) {
            this.result = result;
            this.customMessage = customMessage;
            this.authenticated = authenticated;
        }

        public void fail() {
            this.result = AuthenticationMessages.Auth_Fail;
            this.authenticated = false;
        }
    }
}
