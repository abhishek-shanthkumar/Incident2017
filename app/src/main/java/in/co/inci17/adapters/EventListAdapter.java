package in.co.inci17.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import in.co.inci17.R;
import in.co.inci17.auxiliary.Event;

/**
 * Created by RK on 03/11/2016.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder> {

    Context context;
    private List<Event> events;

    public static final int HEADER = 0;
    public static final int LIVE = 1;
    public static final int UPCOMING = 2;

    public EventListAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
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

    public class UpcomingViewHolder extends EventListViewHolder {

        CardView cardType;
        RelativeLayout root_layout;
        LinearLayout container_top;
        TextView eventName;
        TextView eventDescription;
        TextView eventTimeVenue;
        ImageView eventPicture;
        int dominantColor;

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
        }
    }
}
