package in.co.inci17.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.co.inci17.R;


public class SlamDunkMatchesAdapter extends
        RecyclerView.Adapter<SlamDunkMatchesAdapter.SlamDunkMatchViewHolder> {

    Context context;

    public SlamDunkMatchesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public void onBindViewHolder(SlamDunkMatchViewHolder slamdunkMatchViewHolder, int i) {

        
    }

    @Override
    public SlamDunkMatchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_layout_slamdunk_scorecard, viewGroup, false);
        return new SlamDunkMatchViewHolder(itemView);
    }

    public static class SlamDunkMatchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public CardView cardType;
        public TextView team_name_1, team_name_2;
        public TextView team_score_1, team_score_2;
        public TextView quarter, match_review;
        Typeface typeface_scoreboard;

        public SlamDunkMatchViewHolder(View v) {
            super(v);

            //Fonts
            typeface_scoreboard = Typeface.createFromAsset(v.getContext().getAssets(), "Scoreboard.ttf");

            cardType = (CardView) v.findViewById (R.id.cv_slamdunk);
            team_name_1 = (TextView) v.findViewById (R.id.tv_name_team1);
            team_name_2 = (TextView) v.findViewById (R.id.tv_name_team2);
            team_score_1 = (TextView) v.findViewById (R.id.tv_score_team1);
            team_score_2 = (TextView) v.findViewById (R.id.tv_score_team2);
            quarter = (TextView) v.findViewById (R.id.tv_quarter);
            match_review = (TextView) v.findViewById (R.id.tv_match_result);

            team_score_1.setTypeface(typeface_scoreboard);
            team_score_2.setTypeface(typeface_scoreboard);
            quarter.setTypeface(typeface_scoreboard);

            cardType.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            int itemPosition = getAdapterPosition();
            Intent intent=null;

//            intent = new Intent(view.getContext(),QuestionActivity.class);

            if(intent!=null)
                view.getContext().startActivity(intent);
        }
    }
}
