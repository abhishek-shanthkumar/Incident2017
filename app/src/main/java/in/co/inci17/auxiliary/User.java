package in.co.inci17.auxiliary;

/*
 * Created by Abhishek on 18-01-2017.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class User {

    private static final String USER_KEY = "user";
    private static User user;

    public static User getCurrentUser(Context context) {
        if (user == null && !createUser(context))
            return null;
        return user;
    }

    private static boolean createUser(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        user = gson.fromJson(sharedPref.getString(USER_KEY, null), User.class);
        return user != null;
    }
}
