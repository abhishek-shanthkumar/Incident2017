package in.co.inci17.auxiliary;
/*
 * Created by Abhishek on 18-01-2017.
 */

public class Constants {

    public static final String SHARED_PREFS_FILE = "InciSharedPrefs";
    public static final String PASSPHRASE = "8d71b02e54993ac4ee2351c9804552549f1bade7";

    public static class Messages {
        public static final String NETWORK_ERROR = "Connection problem. Please try later.";
        public static final String GOOGLE_SIGNIN_SUCCESS = "Google Signin Successful!";
        public static final String FACEBOOK_SIGNIN_SUCCESS = "Facebook Signin Successful!";
        public static final String EMAIL_NEEDED = "We need your email address! Please sign in again";
    }

    public static class URLs {
        private static final String HOST = "http://incident.co.in/api/";

        public static final String USER_PRESENT = HOST+"is-user-present.php";
        public static final String CREATE_USER_ACCOUNT = HOST+"create-user-account.php";
        public static final String GET_ALL_EVENTS = HOST+"get-event-details.php";
    }

    public static class Keys {
        public static final String HASH = "key";
        public static final String EMAIL = "Email";
        public static final String PRESENT = "present";
        public static final String ACCOUNT_ID = "account_id";
        public static final String ERROR = "error";
        public static final String EVENT_ID = "id";
        public static final String EVENT_TITLE = "title";
        public static final String EVENT_CATEGORY = "category";
        public static final String EVENT_SUBTITLE = "subtitle";
        public static final String EVENT_DESCRIPTION = "description";
        public static final String EVENT_HAS_REGISTERED = "has_registered";
    }
}
