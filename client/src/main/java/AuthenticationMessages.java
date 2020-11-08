public enum AuthenticationMessages {
    Auth_Request(0),
    Auth_Challenge(1),
    Auth_Fail(2),
    Auth_Success(3);

    public static final String DELIMITER = ":";
    public final int value;

    AuthenticationMessages(final int val) {
        value = val;
    }

    public int getValue() {
        return value;
    }

    public static AuthenticationMessages getAuthMessage(int type){
        for(AuthenticationMessages a : values()){
            if(a.value == type) return a;
        }
        return null;
    }
}
