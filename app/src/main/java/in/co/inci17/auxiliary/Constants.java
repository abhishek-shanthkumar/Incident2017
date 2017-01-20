package in.co.inci17.auxiliary;
/*
 * Created by Abhishek on 18-01-2017.
 */

public class Constants {

    public static final String SHARED_PREFS_FILE = "InciSharedPrefs";
    public static final String PASSPHRASE = "8d71b02e54993ac4ee2351c9804552549f1bade7";

    public static class Messages {
        public static final String MESSAGE_NETWORK_ERROR = "Connection problem. Please try later.";
        public static final String MESSAGE_GOOGLE_SIGNIN_SUCCESS = "Google Signin Successful!";
        public static final String MESSAGE_FACEBOOK_SIGNIN_SUCCESS = "Facebook Signin Successful!";
    }

    public static class URLs {
        private static final String HOST = "http://10.50.47.143/api/";

        public static final String CHECK_EMAIL = HOST+"is-user-present.php";
    }

    public static class Keys {
        public static final String HASH = "key";
        public static final String EMAIL = "Email";
        public static final String PRESENT = "present";
        public static final String ACCOUNT_ID = "account_id";
        public static final String ERROR = "error";
    }
}
