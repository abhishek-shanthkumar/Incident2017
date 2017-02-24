package in.co.inci17.auxiliary;
/*
 * Created by Abhishek on 19-01-2017.
 */

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Event {
    private String id;
    private String title;
    private String subtitle;
    private String category;
    private String description;
    private boolean isRegisterable;
    private boolean hasRegistered;
    private boolean hasBookmarked;
    private int attendingCount;
    private String imageUrl;
    private String iconUrl;
    private Calendar startDateTime;
    private Calendar endDateTime;

    public Event() {

    }

    public Event(String eventID) {
        this.id = eventID;
    }

    public Event(String id, String title, String subtitle, String category, String description, boolean hasRegistered) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.category = category;
        this.description = description;
        this.hasRegistered = hasRegistered;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public boolean isRegisterable() {
        return isRegisterable;
    }

    public void setRegisterable(boolean registerable) {
        isRegisterable = registerable;
    }

    public int getAttendingCount() {
        return attendingCount;
    }

    public void setAttendingCount(int attendingCount) {
        this.attendingCount = attendingCount;
    }

    public boolean hasBookmarked() {
        return hasBookmarked;
    }

    public void setHasBookmarked(boolean hasBookmarked) {
        this.hasBookmarked = hasBookmarked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean hasRegistered() {
        return hasRegistered;
    }

    public void setHasRegistered(boolean hasRegistered) {
        this.hasRegistered = hasRegistered;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Calendar getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Calendar startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Calendar getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Calendar endDateTime) {
        this.endDateTime = endDateTime;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        try {
            JSONObject object = new JSONObject(gson.toJson(this));
            object.remove(Constants.Keys.EVENT_DESCRIPTION);
            return object.toString();
        } catch (JSONException e) {
            Log.e("JSONError", e.getLocalizedMessage());
        }
        return "";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Event)
            return ((Event) obj).getId().equals(id);
        return false;
    }
}
