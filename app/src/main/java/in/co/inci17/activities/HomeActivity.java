package in.co.inci17.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import in.co.inci17.R;
import in.co.inci17.adapters.EventListAdapter;
import in.co.inci17.adapters.ViewPagerAdapterLeaderboard;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rvEvents;
    EventListAdapter mEventListAdapter;

    ViewPagerAdapterLeaderboard mViewPagerAdapterLeaderboard;
    ViewPager leaderboardPager;
    CharSequence Titles[] = {"Leaderboard_1", "Leaderboard_2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //RecyclerView set-up
        rvEvents = (RecyclerView)findViewById(R.id.rv_events);
        mEventListAdapter = new EventListAdapter(getApplicationContext());
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        rvEvents.setItemAnimator(new DefaultItemAnimator());
        rvEvents.setAdapter(mEventListAdapter);

        //Viewpager set-up
        leaderboardPager = (ViewPager) findViewById(R.id.pager_leaderboard);
        mViewPagerAdapterLeaderboard = new ViewPagerAdapterLeaderboard(getSupportFragmentManager(), Titles, 2, this);
        leaderboardPager.setAdapter(mViewPagerAdapterLeaderboard);
        leaderboardPager.setCurrentItem(0);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

        }
    }
}
