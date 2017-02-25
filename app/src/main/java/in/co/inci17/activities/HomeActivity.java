package in.co.inci17.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Response;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import in.co.inci17.R;
import in.co.inci17.adapters.EventListAdapter;
import in.co.inci17.adapters.LiveEventsListAdapter;
import in.co.inci17.adapters.ViewPagerAdapterLeaderboard;
import in.co.inci17.auxiliary.Constants;
import in.co.inci17.auxiliary.Event;
import in.co.inci17.auxiliary.EventsManager;
import in.co.inci17.auxiliary.User;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView rvEvents;
    EventListAdapter mEventListAdapter;

    ViewPagerAdapterLeaderboard mViewPagerAdapterLeaderboard;
    ViewPager leaderboardPager;
    CharSequence Titles[] = {"Leaderboard_1", "Leaderboard_2"};

    private boolean onlyShowMyEvents = false;

    private List<Event> events;

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Event, LiveEventsListAdapter.LiveEventsViewHolder>
            mFirebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if ((User.getCurrentUser(this)) == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        }

        onlyShowMyEvents = getIntent().getBooleanExtra(Constants.SHOW_ONLY_MY_EVENTS, false);

        //NavigationDrawer
        super.onCreateDrawer();
        if(!onlyShowMyEvents) {
            navigationView.getMenu().getItem(0).setChecked(true);
            at_home = true;
        }
        else {
            //getSupportActionBar().setTitle("My Events");
            navigationView.getMenu().getItem(2).setChecked(true);
        }

        EventsManager.getAllEvents(this, new Response.Listener<List<Event>>() {
            @Override
            public void onResponse(List<Event> response) {
                events = new ArrayList<>(response);
                if(onlyShowMyEvents) {
                    Event event;
                    for (int i=0; i<events.size(); i++) {
                        event = events.get(i);
                        if(!event.hasBookmarked() || !event.hasRegistered())
                            events.remove(i);
                    }
                }
                //mEventListAdapter.notifyDataSetChanged();
                setupRecyclerView();
            }
        });
        //Viewpager set-up
        leaderboardPager = (ViewPager) findViewById(R.id.pager_leaderboard);
        mViewPagerAdapterLeaderboard = new ViewPagerAdapterLeaderboard(getSupportFragmentManager(), Titles, 2, this);
        leaderboardPager.setAdapter(mViewPagerAdapterLeaderboard);
        leaderboardPager.setCurrentItem(0);

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
                Event thisEvent = events.get(events.indexOf(event));
                viewHolder.liveEventTitle.setText(thisEvent.getTitle());
                Picasso.with(HomeActivity.this).load(thisEvent.getIconUrl()).into(viewHolder.icon);
                viewHolder.eventID = event.getId();
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mEventListAdapter != null)
            mEventListAdapter.notifyDataSetChanged();
    }
}
