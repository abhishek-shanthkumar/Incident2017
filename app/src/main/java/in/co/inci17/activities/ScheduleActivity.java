package in.co.inci17.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import in.co.inci17.R;
import in.co.inci17.slidingtab.SlidingTabLayout;
import in.co.inci17.viewpageradapters.ViewPagerAdapter_Schedule;

public class ScheduleActivity extends BaseActivity {

    ViewPager vpSchedule;
    SlidingTabLayout schedule_tabs;
    ViewPagerAdapter_Schedule schedule_adapter;
    CharSequence Titles[] = {"Dance", "Music", "Prom Night", "Dance", "Dance"};
    int Numboftabs = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        super.onCreateDrawer();
        navigationView.getMenu().getItem(1).setChecked(true);

        schedule_tabs = (SlidingTabLayout) findViewById(R.id.tabs_schedule);
        vpSchedule = (ViewPager) findViewById(R.id.pager_schedule);

        schedule_adapter = new ViewPagerAdapter_Schedule(getSupportFragmentManager(), Titles, Numboftabs, this);
        vpSchedule.setAdapter(schedule_adapter);
        vpSchedule.setCurrentItem(0);
        schedule_tabs.setDistributeEvenly(false);
        schedule_tabs.setViewPager(vpSchedule);

    }
}
;