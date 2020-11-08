public class AuthenticationResult {

    public AuthenticationMessages result;
    public String customMessage;
    public boolean authenticated;

    public AuthenticationResult(AuthenticationMessages result, String customMessage) {
        this.result = result;
        this.customMessage = customMessage;
    }

    public void fail() {
        this.result = AuthenticationMessages.Auth_Fail;
        this.authenticated = false;
    }
}
