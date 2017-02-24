package in.co.inci17.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.co.inci17.R;
import in.co.inci17.auxiliary.Constants;
import in.co.inci17.auxiliary.CustomRequest;
import in.co.inci17.auxiliary.Event;
import in.co.inci17.auxiliary.User;
import in.co.inci17.services.EventReminder;

/*
 * Created by RK on 21/02/2017.
 */
public class FragmentEvent extends Fragment implements View.OnClickListener{

    View v;
    ImageView eventImage;
    TextView eventName;
    TextView eventDescription;
    ImageView ivRegister;
    ImageView ivBookmark;

    private Context context;
    private Event event;

    private static User user;
    private static RequestQueue mRequestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_event,container,false);
        context = getContext();
        if(mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        if(user == null)
            user = User.getCurrentUser(context);

        eventImage=(ImageView)v.findViewById(R.id.iv_event_image);
        eventName = (TextView) v.findViewById(R.id.tv_event_name);
        eventDescription = (TextView) v.findViewById(R.id.tv_desc);
        ivBookmark = (ImageView) v.findViewById(R.id.ib_bookmark);
        ivRegister = (ImageView) v.findViewById(R.id.ib_register);

        Gson gson;
        gson = new Gson();

        event = gson.fromJson(getArguments().getString("event"), Event.class);

        ivBookmark.setOnClickListener(this);
        ivRegister.setOnClickListener(this);

        updateView();
        return v;
    }

    private void updateView() {
        int b=getArguments().getInt("pos");
        if((b%2)==0)
            eventImage.setImageResource(R.drawable.raghu);
        else
            eventImage.setImageResource(R.drawable.dj);

        eventName.setText(event.getTitle());
        eventDescription.setText(event.getDescription());
        Picasso.with(context).load(event.getImageUrl()).resize(350, 160).centerInside().into(eventImage);

        if(event.hasBookmarked())
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(), R.mipmap.ic_bookmark_24_white));
        else
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(), R.mipmap.ic_bookmark_border_24_white));

    }

    public void scaleImage(float scaleX)
    {
        v.setScaleY(scaleX);
        v.invalidate();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.ib_register:
                if(!event.hasRegistered())
                {
                    Toast.makeText(context, "Registering for "+event.getTitle(), Toast.LENGTH_SHORT).show();
                    registerForEvent(event);
                }
                break;

            case R.id.ib_bookmark:
                if(!event.hasBookmarked())
                    markEventAsAttending(event);
                break;
        }
    }

    private synchronized void registerForEvent(final Event event) {
        //final Event event = events.get(eventIndex);
        HashMap<String, String> params = new HashMap<>();
        String url = Constants.URLs.REGISTER_EVENT;
        assert user != null;
        params.put(Constants.Keys.ACCOUNT_ID, user.getId());
        params.put(Constants.Keys.EVENT_ID, event.getId());
        params.put(Constants.Keys.EVENT_PARTICIPANTS, user.getId());
        CustomRequest request = new CustomRequest(url, params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject object = response.getJSONObject(0);
                            String output = object.getString(Constants.Keys.OUTPUT);
                            if(output.equals("1")) {
                                event.setHasRegistered(true);
                                markEventAsAttending(event);
                            }
                            else {
                                Toast.makeText(context, object.getString(Constants.Keys.ERROR), Toast.LENGTH_SHORT).show();
                                //event.setHasRegistered(false);
                            }
                        } catch (JSONException e) {
                            Log.e("JSONResponse", e.getLocalizedMessage());
                            //event.setHasRegistered(false);
                        }
                        updateView();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, Constants.Messages.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                        //Log.e("Volley", error.networkResponse.toString());
                        //event.setHasRegistered(false);
                        //notifyItemChanged(eventIndex);
                    }
                });
        mRequestQueue.add(request);
    }

    private void markEventAsAttending(final Event event) {
        //final Event event = events.get(eventIndex);
        String url = Constants.URLs.ATTENDING_EVENT;
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.Keys.ACCOUNT_ID, user.getId());
        params.put(Constants.Keys.EVENT_ID, event.getId());
        CustomRequest request = new CustomRequest(url, params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject object = response.getJSONObject(0);
                            String output = object.getString(Constants.Keys.OUTPUT);
                            if(output.equals("1")) {
                                bookmarkEvent(event);
                            }
                            else {
                                Toast.makeText(context, object.getString(Constants.Keys.ERROR), Toast.LENGTH_SHORT).show();
                                //event.setHasRegistered(false);
                            }
                        } catch (JSONException e) {
                            Log.e("JSONError", e.getLocalizedMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, Constants.Messages.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                }
        );
        mRequestQueue.add(request);
    }

    private void bookmarkEvent(Event event) {
        //Event event = events.get(eventIndex);
        Gson gson = new Gson();
        String eventString = gson.toJson(event);
        Intent notificationIntent = new Intent(context, EventReminder.class);
        notificationIntent.putExtra(Constants.EVENT_STRING, eventString);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long delayInMillis = 5000;
        long futureInMillis = SystemClock.elapsedRealtime() + delayInMillis;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);

        event.setHasBookmarked(true);
        updateView();
        //EventsManager.getAllEvents(context, null);
    }

}


