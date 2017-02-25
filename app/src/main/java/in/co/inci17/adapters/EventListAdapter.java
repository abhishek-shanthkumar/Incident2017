package in.co.inci17.adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import in.co.inci17.R;
import in.co.inci17.activities.InEventActivity;
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
    private User user;
    private FirebaseRecyclerAdapter mFirebaseRecyclerAdapter;

    public static final int HEADER = 0;
    public static final int LIVE = 1;
    public static final int UPCOMING = 2;

    public EventListAdapter(Context context, List<Event> events, FirebaseRecyclerAdapter firebaseRecyclerAdapter) {
        this.context = context;
        this.events = events;
        mRequestQueue = Volley.newRequestQueue(context);
        user = User.getCurrentUser(context);
        this.mFirebaseRecyclerAdapter = firebaseRecyclerAdapter;
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
                        notifyItemChanged(events.indexOf(event)+2);
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
        notifyItemChanged(events.indexOf(event)+2);
        //EventsManager.getAllEvents(context, null);
    }

    @Override
    public int getItemCount() {
        return events == null ? 2 : events.size()+2;
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
    public void onBindViewHolder(EventListViewHolder eventListViewHolder, int i) {
        if (eventListViewHolder.getItemViewType() == HEADER) {
            HeaderViewHolder mHeaderViewHolder = (HeaderViewHolder) eventListViewHolder;
        } else if (eventListViewHolder.getItemViewType() == LIVE) {
            LiveViewHolder mLiveViewHolder = (LiveViewHolder) eventListViewHolder;
        } else {
            UpcomingViewHolder mUpcomingViewHolder = (UpcomingViewHolder) eventListViewHolder;
            Event event = events.get(i-2);
            mUpcomingViewHolder.eventName.setText(event.getTitle());
            mUpcomingViewHolder.eventDescription.setText(event.getDescription());
            mUpcomingViewHolder.eventID = event.getId();
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
            if(event.hasBookmarked())
                mUpcomingViewHolder.bookmark.setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(), R.mipmap.ic_bookmark_24_white));
            else
                mUpcomingViewHolder.bookmark.setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(), R.mipmap.ic_bookmark_border_24_white));
            mUpcomingViewHolder.eventPicture.setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dj));
            Picasso.with(context).load(event.getImageUrl()).placeholder(R.drawable.dj).resize(360,180).centerCrop().into(mUpcomingViewHolder.mTarget);
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
            return new LiveViewHolder(itemView, mFirebaseRecyclerAdapter);
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
        private FirebaseRecyclerAdapter mFirebaseRecyclerAdapter;

        public LiveViewHolder(View v, FirebaseRecyclerAdapter mFirebaseRecyclerAdapter) {
            super(v);
            this.mFirebaseRecyclerAdapter = mFirebaseRecyclerAdapter;
            context = v.getContext();

            liveTitle = (TextView) v.findViewById(R.id.tv_live_title);
            liveTitle.setTypeface(null, Typeface.BOLD);

            rvLiveEvents = (RecyclerView)v.findViewById(R.id.rv_live_events);
            //mLiveEventsListAdapter = new LiveEventsListAdapter(context);
            rvLiveEvents.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            rvLiveEvents.setItemAnimator(new DefaultItemAnimator());
            rvLiveEvents.setAdapter(mFirebaseRecyclerAdapter);

        }
    }

    public class UpcomingViewHolder extends EventListViewHolder implements View.OnClickListener {

        CardView cardUpcomingEvent;
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
        Target mTarget;
        String eventID;

        public UpcomingViewHolder(View v) {
            super(v);
            cardUpcomingEvent = (CardView) v.findViewById(R.id.cv_upcoming_event);
            root_layout = (RelativeLayout) v.findViewById(R.id.root_layout_card);
            container_top = (LinearLayout) v.findViewById(R.id.container_event_title);
            eventPicture = (ImageView) v.findViewById(R.id.iv_event_pic);
            eventName = (TextView) v.findViewById(R.id.tv_event_name);
            eventTimeVenue = (TextView) v.findViewById(R.id.tv_time_venue);
            eventDescription = (TextView) v.findViewById(R.id.tv_event_description);

            /*//For loading big images (temporary)
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
            });*/

            register = (ImageButton) v.findViewById(R.id.ib_register);
            register.setOnClickListener(this);
            bookmark = (ImageButton) v.findViewById(R.id.ib_bookmark);
            bookmark.setOnClickListener(this);

            mTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    eventPicture.setImageBitmap(bitmap);
                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            Palette.Swatch vibrantSwatch = palette.getDominantSwatch();

                            if(vibrantSwatch!=null){
                                container_top.setBackgroundColor((vibrantSwatch.getRgb() & 0x00FFFFFF)| 0x99000000);
                                root_layout.setBackgroundColor(vibrantSwatch.getRgb());
                            }

                        }
                    });
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            cardUpcomingEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context bContext=view.getContext();
                    Intent intent_to_event_desc = new Intent(bContext, InEventActivity.class);
                    intent_to_event_desc.putExtra("id", eventID);
                    Gson gson = new Gson();
                    Type listOfEvents = new TypeToken<List<Event>>(){}.getType();
                    intent_to_event_desc.putExtra("events", gson.toJson(events, listOfEvents));
                    bContext.startActivity(intent_to_event_desc);
                }
            });
        }

        @Override
        public void onClick(View v) {
            int index = events.indexOf(new Event(eventID));
            if(index == -1)
                return;
            Event event = events.get(index);

            switch(v.getId()) {

                case R.id.cv_upcoming_event:

                    break;

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

    }


}
