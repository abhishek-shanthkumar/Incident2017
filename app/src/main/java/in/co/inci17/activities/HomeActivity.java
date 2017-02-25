package in.co.inci17.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import in.co.inci17.R;
import in.co.inci17.adapters.EventListAdapter;
import in.co.inci17.adapters.LiveEventsListAdapter;
import in.co.inci17.adapters.SlamDunkMatchesAdapter;
import in.co.inci17.adapters.ViewPagerAdapterLeaderboard;
import in.co.inci17.auxiliary.BottomFadeEdgeRV;
import in.co.inci17.auxiliary.Constants;
import in.co.inci17.auxiliary.CustomTypefaceSpan;
import in.co.inci17.auxiliary.Event;
import in.co.inci17.auxiliary.EventsManager;
import in.co.inci17.auxiliary.User;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private SlidingUpPanelLayout mSlidingLayout;

    RecyclerView rvEvents;
    BottomFadeEdgeRV mSlamDunkMatches;

    EventListAdapter mEventListAdapter;
    SlamDunkMatchesAdapter mSlamDunkMatchesAdapter;

    LinearLayoutManager mLayoutManager;

    ViewPagerAdapterLeaderboard mViewPagerAdapterLeaderboard;
    ViewPager leaderboardPager;
    CharSequence Titles[] = {"Leaderboard_1", "Leaderboard_2"};

    private List<Event> events;

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Event, LiveEventsListAdapter.LiveEventsViewHolder>
            mFirebaseAdapter;

    TextView tvSlamDunk;

    Typeface tf_RobotoSlabBold, tf_RobotoLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Font declarations
        tf_RobotoSlabBold = Typeface.createFromAsset(getApplicationContext().getAssets(), "RobotoSlab_Bold.ttf");
        tf_RobotoLight = Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto_Light.ttf");

        if ((User.getCurrentUser(this)) == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        }

        //NavigationDrawer
        super.onCreateDrawer();
        navigationView.getMenu().getItem(0).setChecked(true);
        at_home=true;

        EventsManager.getAllEvents(this, new Response.Listener<List<Event>>() {
            @Override
            public void onResponse(List<Event> response) {
                events = response;
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
        mEventListAdapter = new EventListAdapter(getApplicationContext(), events, mFirebaseAdapter);
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        rvEvents.setItemAnimator(new DefaultItemAnimator());
        rvEvents.setAdapter(mEventListAdapter);

    }

    private void setupMatchesRecyclerView() {
        //RecyclerView set-up
//        setupLiveEvents();
        mSlamDunkMatches = (BottomFadeEdgeRV) findViewById(R.id.rv_slamdunk);

        mLayoutManager = new LinearLayoutManager(this);
        mSlamDunkMatchesAdapter = new SlamDunkMatchesAdapter(getApplicationContext());
        mSlamDunkMatches.setLayoutManager(mLayoutManager);
        mSlamDunkMatches.setItemAnimator(new DefaultItemAnimator());
        mSlamDunkMatches.setAdapter(mSlamDunkMatchesAdapter);
    }

    private void setupLiveEvents() {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Event, LiveEventsListAdapter.LiveEventsViewHolder>(
                Event.class,
                R.layout.card_layout_live,
                LiveEventsListAdapter.LiveEventsViewHolder.class,
                mFirebaseDatabaseReference.child(Constants.LIVE_EVENTS_CHILD)
        ) {
            @Override
            protected void populateViewHolder(LiveEventsListAdapter.LiveEventsViewHolder viewHolder, Event event, int position) {
                viewHolder.liveEventTitle.setText(events.get(events.indexOf(event)).getTitle());
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

        }
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
}
