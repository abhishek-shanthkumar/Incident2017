package in.co.inci17.auxiliary;
/*
 * Created by Abhishek on 21-01-2017.
 */

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventsManager {

    private static List<Response.Listener<List<Event>>> listeners;
    private static List<Event> events;
    private static boolean fetchingEvents = false;
    private static User user;

    public synchronized static void getAllEvents(Context context, Response.Listener<List<Event>> listener) {
        if(user == null)
            user = User.getCurrentUser(context);
        /*if(events != null)
            listener.onResponse(events);
        else {
            if(listeners == null)
                listeners = new ArrayList<>();
            listeners.add(listener);
        }
        */

        if(listeners == null)
            listeners = new ArrayList<>();
        listeners.add(listener);

        if(!fetchingEvents)
            fetchEvents(context);
    }

    private synchronized static void fetchEvents(Context context) {
        fetchingEvents = true;
        events = new ArrayList<>();
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.Keys.ACCOUNT_ID, user.getId());

        CustomRequest request = new CustomRequest(Constants.URLs.GET_ALL_EVENTS, params, true,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject object;
                            for(int i=0; i<array.length(); i++) {
                                object = array.getJSONObject(i);
                                events.add(new Event(
                                        object.getString(Constants.Keys.EVENT_ID),
                                        object.getString(Constants.Keys.EVENT_TITLE),
                                        object.getString(Constants.Keys.EVENT_SUBTITLE),
                                        object.getString(Constants.Keys.EVENT_CATEGORY),
                                        object.getString(Constants.Keys.EVENT_DESCRIPTION),
                                        object.getString(Constants.Keys.EVENT_HAS_REGISTERED).equals("1")
                                ));
                            }
                        } catch (JSONException e) {
                            Log.e("JSON Parse", e.getLocalizedMessage());
                        }
                        for(Response.Listener<List<Event>> listener : listeners)
                            listener.onResponse(events);
                        fetchingEvents = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Fetch events", error.getLocalizedMessage());
                        fetchingEvents = false;
                    }
                });
        mRequestQueue.add(request);
    }
}
