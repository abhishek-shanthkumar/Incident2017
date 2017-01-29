package in.co.inci17.adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

/*
 * Created by RK on 03/11/2016.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder> {

    private Context context;
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
        CustomRequest request = new CustomRequest(url, params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject object = response.getJSONObject(0);
                            String output = object.getString(Constants.Keys.OUTPUT);
                            if(output.equals("1")) {
                                event.setHasRegistered(true);
                            }
                            else {
                                Toast.makeText(context, object.getString(Constants.Keys.ERROR), Toast.LENGTH_SHORT).show();
                                //event.setHasRegistered(false);
                            }
                        } catch (JSONException e) {
                            Log.e("JSONResponse", e.getLocalizedMessage());
                            //event.setHasRegistered(false);
                        }
                        notifyItemChanged(eventIndex);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, Constants.Messages.NETWORK_ERROR+"\n"+error, Toast.LENGTH_SHORT).show();
                        //Log.e("Volley", error.networkResponse.toString());
                        //event.setHasRegistered(false);
                        //notifyItemChanged(eventIndex);
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
        } else if (EventListViewHolder.getItemViewType() == LIVE) {
            LiveViewHolder mLiveViewHolder = (LiveViewHolder) EventListViewHolder;
        } else {
            UpcomingViewHolder mUpcomingViewHolder = (UpcomingViewHolder) EventListViewHolder;
            Event event = events.get(i);
            mUpcomingViewHolder.eventName.setText(event.getTitle());
            mUpcomingViewHolder.eventDescription.setText(event.getDescription());

            //Time and Venue
            String time = "4:30";
            String loc = "Main Building";
            String time_venue = "STARTS IN " + time + " hrs " + "AT " + loc;
            SpannableString modified_s = new SpannableString(time_venue);
            modified_s.setSpan(new RelativeSizeSpan(1.4f), 10, 10 + time.length(), 0);
            modified_s.setSpan(new ForegroundColorSpan(Color.WHITE), 10, 10 + time.length(), 0);
            modified_s.setSpan(new ForegroundColorSpan(Color.WHITE), 11 + time.length(), 14 + time.length(), 0);
            modified_s.setSpan(new RelativeSizeSpan(1.4f), 18 + time.length(), 18 + time.length() + loc.length(), 0);
            modified_s.setSpan(new ForegroundColorSpan(Color.WHITE), 18 + time.length(), 18 + time.length() + loc.length(), 0);

            mUpcomingViewHolder.eventTimeVenue.setText(modified_s);
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
        TextView inciTitle, year, theme;

        public HeaderViewHolder(View v) {
            super(v);
            inciTitle = (TextView)v.findViewById(R.id.tv_incident);
            year = (TextView)v.findViewById(R.id.tv_17);
            theme = (TextView)v.findViewById(R.id.tv_theme);
            Typeface typeface_1 = Typeface.createFromAsset(v.getContext().getAssets(), "Roboto_Thin.ttf");
            Typeface typeface_2 = Typeface.createFromAsset(v.getContext().getAssets(), "Roboto_Light.ttf");
            Typeface typeface_3 = Typeface.createFromAsset(v.getContext().getAssets(), "DancingScrip_Regular.otf");

            inciTitle.setTypeface(typeface_1);
            year.setTypeface(typeface_2);
            theme.setTypeface(typeface_3);

        }
    }

    public class LiveViewHolder extends EventListViewHolder{

        RecyclerView rvLiveEvents;
        LiveEventsListAdapter mLiveEventsListAdapter;
        TextView liveTitle;
        Context context;

        public LiveViewHolder(View v) {
            super(v);
            context = v.getContext();

            liveTitle = (TextView) v.findViewById(R.id.tv_live_title);
            liveTitle.setTypeface(null, Typeface.BOLD);

            rvLiveEvents = (RecyclerView)v.findViewById(R.id.rv_live_events);
            mLiveEventsListAdapter = new LiveEventsListAdapter(context);
            rvLiveEvents.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            rvLiveEvents.setItemAnimator(new DefaultItemAnimator());
            rvLiveEvents.setAdapter(mLiveEventsListAdapter);

        }
    }

    public class UpcomingViewHolder extends EventListViewHolder implements View.OnClickListener {

        CardView cardType;
        RelativeLayout root_layout;
        LinearLayout container_top;
        TextView eventName;
        TextView eventDescription;
        TextView eventTimeVenue;
        ImageView eventPicture;
        int dominantColor;
        ImageButton bookmark;
        ImageButton register;
        ImageButton share;

        public UpcomingViewHolder(View v) {
            super(v);
            root_layout = (RelativeLayout) v.findViewById(R.id.root_layout_card);
            container_top = (LinearLayout) v.findViewById(R.id.container_event_title);
            eventPicture = (ImageView) v.findViewById(R.id.iv_event_pic);
            eventName = (TextView) v.findViewById(R.id.tv_event_name);
            eventTimeVenue = (TextView) v.findViewById(R.id.tv_time_venue);
            eventDescription = (TextView) v.findViewById(R.id.tv_event_description);

            //For loading big images (temporary)
            BitmapFactory.Options bm_opts = new BitmapFactory.Options();
            bm_opts.inScaled = false;
            Bitmap imageBM = BitmapFactory.decodeResource(v.getResources(), R.drawable.raghu, bm_opts);
            eventPicture.setImageBitmap(imageBM);

            //To be used for extracting the dominant color
            Palette.from(imageBM).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch vibrantSwatch = palette.getDominantSwatch();

                    if(vibrantSwatch!=null){
                        container_top.setBackgroundColor((vibrantSwatch.getRgb() & 0x00FFFFFF)| 0x99000000);
                        root_layout.setBackgroundColor(vibrantSwatch.getRgb());
                    }

                }
            });

            register = (ImageButton) v.findViewById(R.id.ib_register);
            register.setOnClickListener(this);
            bookmark = (ImageButton) v.findViewById(R.id.ib_bookmark);
            bookmark.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            Event event = events.get(index);

            switch(v.getId()) {
                case R.id.ib_register:
                    if(!event.isHasRegistered())
                        registerForEvent(getAdapterPosition());
                    break;

                case R.id.ib_bookmark:
                    bookmarkEvent(event);
                    break;
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
