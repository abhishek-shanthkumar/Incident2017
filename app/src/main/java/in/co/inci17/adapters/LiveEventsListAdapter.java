package in.co.inci17.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.co.inci17.R;

public class LiveEventsListAdapter extends
        RecyclerView.Adapter<LiveEventsListAdapter.LiveEventsViewHolder> {

    Context context;

    public LiveEventsListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    @Override
    public void onBindViewHolder(LiveEventsViewHolder LiveEventsViewHolder, int i) {

        
    }

    @Override
    public LiveEventsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_layout_live, viewGroup, false);
        return new LiveEventsViewHolder(itemView);
    }

    public class LiveEventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cardType;

        public LiveEventsViewHolder(View v) {
            super(v);
            cardType = (CardView) v.findViewById (R.id.cv_live);

            cardType.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {

        }
    }
}
