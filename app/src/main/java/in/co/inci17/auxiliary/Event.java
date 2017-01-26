package in.co.inci17.auxiliary;
/*
 * Created by Abhishek on 19-01-2017.
 */

import java.util.Calendar;

public class Event {
    private String id;
    private String title;
    private String subtitle;
    private String category;
    private String description;
    private boolean hasRegistered;
    private String imageUrl;
    private Calendar startDateTime;
    private Calendar endDateTime;

    public Event(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Event(String id, String title, String subtitle, String category, String description, boolean hasRegistered) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.category = category;
        this.description = description;
        this.hasRegistered = hasRegistered;
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

    public boolean isHasRegistered() {
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
}
