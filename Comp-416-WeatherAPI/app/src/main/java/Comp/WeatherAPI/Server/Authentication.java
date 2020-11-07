package Comp.WeatherAPI.Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Authentication {
    private static Authentication authentication = null;

    private Authentication() {
    }

    public static Authentication getInstance() {
        if (authentication == null)
            authentication = new Authentication();
        return authentication;
    }

    public boolean validateUser(String username) {
        username += ".txt";
        ClassLoader classLoader = getClass().getClassLoader();
        File userFile = new File(classLoader.getResource(username).getFile());
        boolean validUser = userFile.exists();

        return validUser;
    }

    private HashMap<String, String> loadUserDetails(String username) {
        /*** given the username, looksup and loads the contents of the authentication details related to this user to a HashMap **/
        ClassLoader classLoader = getClass().getClassLoader();
        File userFile = new File(classLoader.getResource(username).getFile());
        Scanner reader = null;
        try {
            reader = new Scanner(userFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HashMap<String, String> userDetails = new HashMap<String, String>();
        int i = 0;
        String prevQuestion = "";
        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            if (i % 2 == 0) {
                prevQuestion = data;
            } else {
                userDetails.put(prevQuestion, data);
            }
        }
        return userDetails;
    }

    public boolean authorize(String username, ServerThread st) {
        Random rand = new Random();
        HashMap<String, String> userDetails = loadUserDetails(username);
        int numQuestions = rand.nextInt(userDetails.size());
        List<String> questions = ((List<String>) userDetails.keySet()).subList(0, numQuestions);

        for (String question : questions) {
            String correctAnswer=userDetails.get(question);
            try {
                st.outputStream.print(question);
                String givenAnswer=st.inputStream.readLine();

                if (givenAnswer!=correctAnswer) {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
