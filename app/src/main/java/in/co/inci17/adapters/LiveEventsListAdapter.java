package in.co.inci17.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import in.co.inci17.R;
import in.co.inci17.activities.InEventActivity;

public class LiveEventsListAdapter extends
        RecyclerView.Adapter<LiveEventsListAdapter.LiveEventsViewHolder> {

    Context context;

    public LiveEventsListAdapter() {}

    public LiveEventsListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    @Override
    public void onBindViewHolder(LiveEventsViewHolder mLiveEventsViewHolder, int i) {

        mLiveEventsViewHolder.liveEventTitle.setText("Eastern Night");

    }

    @Override
    public LiveEventsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_layout_live, viewGroup, false);
        return new LiveEventsViewHolder(itemView);
    }

    public static class LiveEventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cardType;
        public TextView liveEventTitle;
        public ImageView icon;
        public String eventID;

        public LiveEventsViewHolder(View v) {
            super(v);
            cardType = (CardView) v.findViewById (R.id.cv_live);
            liveEventTitle = (TextView) v.findViewById (R.id.tv_live_event_title);
            icon = (ImageView) v.findViewById(R.id.event_icon);
            //TextView Marquee
            liveEventTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            liveEventTitle.setSingleLine(true);
            liveEventTitle.setMarqueeRepeatLimit(1);
            liveEventTitle.setSelected(true);

            cardType.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context bContext=view.getContext();
            Intent intent_to_event_desc = new Intent(bContext, InEventActivity.class);
            intent_to_event_desc.putExtra("id", Integer.parseInt(eventID));
            bContext.startActivity(intent_to_event_desc);
        }
    }
}
