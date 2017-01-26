package in.co.inci17.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Response;

import java.util.List;

import in.co.inci17.R;
import in.co.inci17.adapters.EventListAdapter;
import in.co.inci17.adapters.ViewPagerAdapterLeaderboard;
import in.co.inci17.auxiliary.Event;
import in.co.inci17.auxiliary.EventsManager;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView rvEvents;
    EventListAdapter mEventListAdapter;

    ViewPagerAdapterLeaderboard mViewPagerAdapterLeaderboard;
    ViewPager leaderboardPager;
    CharSequence Titles[] = {"Leaderboard_1", "Leaderboard_2"};

    private List<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //NavigationDrawer
        super.onCreateDrawer();
        navigationView.getMenu().getItem(0).setChecked(true);
        at_home=true;

        EventsManager.getAllEvents(this, new Response.Listener<List<Event>>() {
            @Override
            public void onResponse(List<Event> response) {
                events = response;
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
        rvEvents = (RecyclerView)findViewById(R.id.rv_events);
        mEventListAdapter = new EventListAdapter(getApplicationContext(), events);
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        rvEvents.setItemAnimator(new DefaultItemAnimator());
        rvEvents.setAdapter(mEventListAdapter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

        }
    }
}
