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

    private String id;
    private String displayName;
    private String email;
    private String imageUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static User getCurrentUser(Context context) {
        if (user == null && !createUser(context))
            return null;
        return user;
    }

    public static void updateUser(User updatedUser, Context context) {
        user = updatedUser;
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(
                Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USER_KEY, gson.toJson(user));
        editor.apply();
    }

    private static boolean createUser(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        user = gson.fromJson(sharedPref.getString(USER_KEY, null), User.class);
        return user != null;
    }
}
