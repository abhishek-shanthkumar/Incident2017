package in.co.inci17.adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import in.co.inci17.R;
import in.co.inci17.auxiliary.Constants;
import in.co.inci17.auxiliary.CustomRequest;
import in.co.inci17.auxiliary.Event;
import in.co.inci17.auxiliary.User;
import in.co.inci17.services.EventReminder;

/**
 * Created by RK on 03/11/2016.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder> {

    Context context;
    private List<Event> events;
    private RequestQueue mRequestQueue;

    public static final int HEADER = 0;
    public static final int LIVE = 1;
    public static final int UPCOMING = 2;

    public EventListAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    private synchronized void registerForEvent(final int eventIndex) {
        final Event event = events.get(eventIndex);
        User user = User.getCurrentUser(context);
        HashMap<String, String> params = new HashMap<>();
        String url = Constants.URLs.REGISTER_EVENT;
        assert user != null;
        params.put(Constants.Keys.ACCOUNT_ID, user.getId());
        params.put(Constants.Keys.EVENT_ID, event.getId());
        params.put(Constants.Keys.EVENT_PARTICIPANTS, user.getId());
        final CustomRequest request = new CustomRequest(url, params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject object = response.getJSONObject(0);
                            String output = object.getString(Constants.Keys.OUTPUT);
                            if(output.equals("1")) {
                                event.setHasRegistered(true);
                                notifyItemChanged(eventIndex);
                            }
                            else
                                Toast.makeText(context, object.getString(Constants.Keys.ERROR), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Log.e("JSONResponse", e.getLocalizedMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, Constants.Messages.NETWORK_ERROR+"\n"+error, Toast.LENGTH_SHORT).show();
                        //Log.e("Volley", error.networkResponse.toString());
                    }
                });
        mRequestQueue.add(request);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return HEADER;
        else if(position==1)
            return LIVE;
        else
            return UPCOMING;
    }

    @Override
    public void onBindViewHolder(EventListViewHolder EventListViewHolder, int i) {
        if (EventListViewHolder.getItemViewType() == HEADER) {
            HeaderViewHolder mHeaderViewHolder = (HeaderViewHolder) EventListViewHolder;
        }
        else if (EventListViewHolder.getItemViewType() == LIVE) {
            LiveViewHolder mLiveViewHolder = (LiveViewHolder) EventListViewHolder;
        }
        else {
            UpcomingViewHolder mUpcomingViewHolder = (UpcomingViewHolder) EventListViewHolder;
            Event event = events.get(i);
            mUpcomingViewHolder.eventName.setText(event.getTitle());
            mUpcomingViewHolder.eventDescription.setText(event.getDescription());
            if(event.isHasRegistered())
                mUpcomingViewHolder.register.setText("Registered!");
            else
                mUpcomingViewHolder.register.setText("Register");
        }
    }

    @Override
    public EventListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        if (viewType == HEADER) {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_layout_event_header, viewGroup, false);
            return new HeaderViewHolder(itemView);
        } else if(viewType == LIVE){
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_layout_event_live, viewGroup, false);
            return new LiveViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_layout_event_upcoming, viewGroup, false);
            return new UpcomingViewHolder(itemView);
        }
    }

    public class EventListViewHolder extends RecyclerView.ViewHolder{

        public EventListViewHolder(View v) {
            super(v);

        }

    }

    public class HeaderViewHolder extends EventListViewHolder{

        CardView cardType;


        public HeaderViewHolder(View v) {
            super(v);

        }
    }

    public class LiveViewHolder extends EventListViewHolder{

        RecyclerView rvLiveEvents;
        LiveEventsListAdapter mLiveEventsListAdapter;
        Context context;

        public LiveViewHolder(View v) {
            super(v);
            context = v.getContext();

            rvLiveEvents = (RecyclerView)v.findViewById(R.id.rv_live_events);
            mLiveEventsListAdapter = new LiveEventsListAdapter(context);
            rvLiveEvents.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            rvLiveEvents.setItemAnimator(new DefaultItemAnimator());
            rvLiveEvents.setAdapter(mLiveEventsListAdapter);

        }
    }

    public class UpcomingViewHolder extends EventListViewHolder implements View.OnClickListener{

        CardView cardType;
        TextView eventName;
        TextView eventDescription;
        Button register;
        Button bookmark;

        public UpcomingViewHolder(View v) {
            super(v);
            eventName = (TextView) v.findViewById(R.id.tv_event_name);
            eventDescription = (TextView) v.findViewById(R.id.tv_event_description);
            register = (Button) v.findViewById(R.id.button_register);
            register.setOnClickListener(this);
            bookmark = (Button) v.findViewById(R.id.button_bookmark);
            bookmark.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            Event event = events.get(index);

            switch(v.getId()) {
                case R.id.button_register:
                    if(!event.isHasRegistered())
                        registerForEvent(getAdapterPosition());
                    break;

                case R.id.button_bookmark:
                    bookmarkEvent(event);
            }
        }

        private void bookmarkEvent(Event event) {
            Gson gson = new Gson();
            String eventString = gson.toJson(event);
            Intent notificationIntent = new Intent(context, EventReminder.class);
            notificationIntent.putExtra(Constants.EVENT_STRING, eventString);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            long delayInMillis = 5000;
            long futureInMillis = SystemClock.elapsedRealtime() + delayInMillis;
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        }
    }
}
