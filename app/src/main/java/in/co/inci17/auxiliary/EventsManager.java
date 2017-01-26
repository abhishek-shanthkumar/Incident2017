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
        if(events != null)
            listener.onResponse(events);
        else {
            if(listeners == null)
                listeners = new ArrayList<>();
            listeners.add(listener);
        }
        if(!fetchingEvents)
            fetchEvents(context);
    }

    private synchronized static void fetchEvents(Context context) {
        /*
        events = new ArrayList<>();
        events.add(new Event("Bandish", "Thermal and Quarter. Raghu Dixit Project. Thaikkudam Bridge. Let it be remembered in the pages of history that nobody does rock better than our desi boyz! Adorn your heart with the tricolour at Bandish, the Eastern rock phenomenon. Because who says only the Westerners should have all the fun? "));
        events.add(new Event("Dhwanik", "This Incident, allow your soul to marvel at the wonders of Eastern music. Let the whimsical notes of the flute sweep you off your feet. Let the cheeky twang of the sitar snap at the strings of your heart. Let the lively beat of the tabla incite fire in your veins. Let Dhwanik, Incident's Eastern acoustic sensation, leave you craving more.ore."));
        events.add(new Event("Pulse", "Have you ever dreamt of selling out Madison Square Garden? Ever wondered how it would feel to witness a full-house crowd chant your name? Team Incident provides just the experience and invites you to rule the stage at Pulse, the Western battle of bands. We promise you a crowd unlike anything you have ever seen and will ever see! What are you waiting for? Register today!"));
        events.add(new Event("Unplugged", "A starry night. Gentle winds. A crackling bonfire. Waves on a beach. The strum of a guitar. Make music with your mouth. Mix melodies with your mind. Enthral your senses and expand your horizons. Experience contentment and enlightenment at Unplugged. Be it Western acoustic spectacle or an Acapella wonder, unplug your heart and let the music take over!"));
        events.add(new Event("Raagalaya", "Ever dreamt of seeing your name among the upper echelons of maestros such as Mohd. Raffi, A.R. Rahman, Shankar Mahadevan and the likes? Every prodigy started as a rookie. Team Incident invites all bathroom singers and maestros to participate in Raagalaya, the Eastern solo competition."));
        events.add(new Event("Center Stage", "A bright spotlight. A screaming crowd. Show time. Belt out with drums, or tone it down with the simple, effluent notes of a guitar! The theory is to prove that music means more than vocal cords, instruments are the equipment needed and the stage is your laboratory. Participate in \"Centre Stage\", the solo instrumental competition and prove your mettle!"));
        */

        fetchingEvents = true;

        events = new ArrayList<>();
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.Keys.ACCOUNT_ID, user.getId());

        CustomRequest request = new CustomRequest(Constants.URLs.GET_ALL_EVENTS, params,
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
