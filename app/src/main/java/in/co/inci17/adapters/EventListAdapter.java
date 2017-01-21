package in.co.inci17.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.co.inci17.R;

/**
 * Created by RK on 03/11/2016.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder> {

    Context context;

    public static final int HEADER = 0;
    public static final int LIVE = 1;
    public static final int UPCOMING = 2;

    public EventListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return 12;
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

    public class UpcomingViewHolder extends EventListViewHolder {

        CardView cardType;
        TextView elementTitle;

        public UpcomingViewHolder(View v) {
            super(v);

        }
    }
}