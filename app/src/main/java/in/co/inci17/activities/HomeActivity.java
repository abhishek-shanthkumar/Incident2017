package in.co.inci17.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.co.inci17.R;
import in.co.inci17.adapters.EventListAdapter;
import in.co.inci17.adapters.SlamDunkMatchesAdapter;
import in.co.inci17.adapters.ViewPagerAdapterLeaderboard;
import in.co.inci17.auxiliary.BottomFadeEdgeRV;
import in.co.inci17.auxiliary.Constants;
import in.co.inci17.auxiliary.CustomTypefaceSpan;
import in.co.inci17.auxiliary.Event;
import in.co.inci17.auxiliary.EventsManager;
import in.co.inci17.auxiliary.SlamdunkMatch;

public class HomeActivity extends BaseActivity {

    private SlidingUpPanelLayout mSlidingLayout;

    RecyclerView rvEvents;
    BottomFadeEdgeRV mSlamDunkMatches;

    EventListAdapter mEventListAdapter;

    LinearLayoutManager mLayoutManager;

    ViewPagerAdapterLeaderboard mViewPagerAdapterLeaderboard;
    ViewPager leaderboardPager;
    CharSequence Titles[] = {"Leaderboard_1", "Leaderboard_2"};

    private boolean onlyShowMyEvents = false;

    private List<Event> allEvents;
    private List<Event> adapterEvents;

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Event, LiveEventsViewHolder>
            mFirebaseAdapter;
    private FirebaseRecyclerAdapter<SlamdunkMatch, SlamDunkMatchesAdapter.SlamDunkMatchViewHolder> mSlamDunkFirebaseAdapter;

    TextView tvSlamDunk;

    Typeface tf_RobotoSlabBold, tf_RobotoLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //Font declarations
        tf_RobotoSlabBold = Typeface.createFromAsset(getApplicationContext().getAssets(), "RobotoSlab_Bold.ttf");
        tf_RobotoLight = Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto_Light.ttf");

        /*if ((User.getCurrentUser(this)) == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        }*/

        onlyShowMyEvents = getIntent().getBooleanExtra(Constants.SHOW_ONLY_MY_EVENTS, false);

        //NavigationDrawer
        super.onCreateDrawer();
        if(!onlyShowMyEvents) {
            navigationView.getMenu().getItem(0).setChecked(true);
            at_home = true;
        }
        else {
            //getSupportActionBar().setTitle("My Events");
            ((TextView) findViewById(R.id.tv_title)).setText("Bookmarks");
            navigationView.getMenu().getItem(2).setChecked(true);
        }

        EventsManager.getAllEvents(this, new Response.Listener<List<Event>>() {
            @Override
            public void onResponse(List<Event> response) {
                allEvents = new ArrayList<>(response);
                adapterEvents = new ArrayList<>();
                if(onlyShowMyEvents) {
                    for(Event event : allEvents)
                        if(event.hasRegistered() || event.hasBookmarked())
                            adapterEvents.add(event);
                }
                else
                    adapterEvents.addAll(allEvents);

                //mEventListAdapter.notifyDataSetChanged();
                setupRecyclerView();
            }
        });
        setupMatchesRecyclerView();

        tvSlamDunk = (TextView) findViewById(R.id.tv_slamdunk_title);
        mSlidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        //Viewpager set-up
        leaderboardPager = (ViewPager) findViewById(R.id.pager_leaderboard);
        mViewPagerAdapterLeaderboard = new ViewPagerAdapterLeaderboard(getSupportFragmentManager(), Titles, 2, this);
        leaderboardPager.setAdapter(mViewPagerAdapterLeaderboard);
        leaderboardPager.setCurrentItem(0);

        SpannableStringBuilder slamdunk_title = new SpannableStringBuilder("SLAMDUNK SCORES");
        slamdunk_title.setSpan (new CustomTypefaceSpan("",tf_RobotoSlabBold), 0, 8, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        slamdunk_title.setSpan (new CustomTypefaceSpan("", tf_RobotoLight), 9, 15,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tvSlamDunk.setText(slamdunk_title);

        mSlidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }
            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            }
        });
        mSlidingLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

    }

    private void setupRecyclerView() {
        //RecyclerView set-up
        setupLiveEvents();

        rvEvents = (RecyclerView)findViewById(R.id.rv_events);
        mEventListAdapter = new EventListAdapter(this, adapterEvents, mFirebaseAdapter);
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        rvEvents.setItemAnimator(new DefaultItemAnimator());
        rvEvents.setAdapter(mEventListAdapter);

    }

    private void setupMatchesRecyclerView() {
        //RecyclerView set-up
//        setupLiveEvents();
        mSlamDunkFirebaseAdapter = new FirebaseRecyclerAdapter<SlamdunkMatch, SlamDunkMatchesAdapter.SlamDunkMatchViewHolder>(
                SlamdunkMatch.class,
                R.layout.card_layout_slamdunk_scorecard,
                SlamDunkMatchesAdapter.SlamDunkMatchViewHolder.class,
                mFirebaseDatabaseReference.child(Constants.SLAM_DUNK_CHILD)
        ) {
            @Override
            protected void populateViewHolder(SlamDunkMatchesAdapter.SlamDunkMatchViewHolder viewHolder, SlamdunkMatch match, int position) {
                viewHolder.team_name_1.setText(match.getTeamA());
                viewHolder.team_name_2.setText(match.getTeamB());
                viewHolder.team_score_1.setText(match.getScoreA());
                viewHolder.team_score_2.setText(match.getScoreB());
                viewHolder.quarter.setText("QTR\n"+match.getQuarter());
                if(!match.getStarted().equals(Constants.YES))
                    viewHolder.match_review.setText(match.getWinner());
                else if(match.getWinner().equals(Constants.WINNER_UNDECIDED))
                    viewHolder.match_review.setText(Constants.IN_PROGRESS);
                else
                    viewHolder.match_review.setText("Winner: "+match.getWinner());
            }
        };
        mSlamDunkMatches = (BottomFadeEdgeRV) findViewById(R.id.rv_slamdunk);

        mLayoutManager = new LinearLayoutManager(this);
        //mSlamDunkMatchesAdapter = new SlamDunkMatchesAdapter(getApplicationContext());
        mSlamDunkMatches.setLayoutManager(mLayoutManager);
        mSlamDunkMatches.setItemAnimator(new DefaultItemAnimator());
        mSlamDunkMatches.setAdapter(mSlamDunkFirebaseAdapter);
    }

    private void setupLiveEvents() {
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Event, LiveEventsViewHolder>(
                Event.class,
                R.layout.card_layout_live,
                LiveEventsViewHolder.class,
                mFirebaseDatabaseReference.child(Constants.LIVE_EVENTS_CHILD)
        ) {
            @Override
            protected void populateViewHolder(LiveEventsViewHolder viewHolder, Event event, int position) {
                int index = allEvents.indexOf(event);
                if(index == -1)
                    return;
                final Event thisEvent = allEvents.get(index);
                viewHolder.liveEventTitle.setText(thisEvent.getTitle());
                Picasso.with(HomeActivity.this).load(thisEvent.getIconUrl()).into(viewHolder.icon);
                viewHolder.eventID = event.getId();
                final Context context = HomeActivity.this;
                viewHolder.cardType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent_to_event_desc = new Intent(context, InEventActivity.class);
                        intent_to_event_desc.putExtra("id", thisEvent.getId());
                        EventsManager.currentEvents = allEvents;
                        context.startActivity(intent_to_event_desc);
                    }
                });
            }
        };
    }

    @Override
    public void onBackPressed() {
        if (mSlidingLayout != null &&
                (mSlidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mSlidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
    protected void onResume() {
        super.onResume();
        if(mEventListAdapter != null)
            mEventListAdapter.notifyDataSetChanged();
    }

    public static class LiveEventsViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/{

        CardView cardType;
        public TextView liveEventTitle;
        public ImageView icon;
        public String eventID;
        private Context context;

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
            //context = HomeActivity.this;
            //cardType.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View view) {
            Intent intent_to_event_desc = new Intent(context, InEventActivity.class);
            intent_to_event_desc.putExtra("id", eventID);
            EventsManager.currentEvents = allEvents;
            context.startActivity(intent_to_event_desc);
        }*/
    }

    //Functon for getting searched Events
    /*protected List<Event> getSearchedEvents(String keyword){
        keyword=keyword.toLowerCase();
        List<Event> matchedEvents=null;
        for(Event event : adapterEvents){
            if(event.getTitle().contains(keyword) || (event.getSubtitle().contains(keyword) || (event.getDescription().contains(keyword))))
            {
                matchedEvents.add(event);
            }
        }
        return matchedEvents;
    }*/
}
