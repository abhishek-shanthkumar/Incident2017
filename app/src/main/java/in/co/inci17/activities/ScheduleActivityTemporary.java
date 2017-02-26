package in.co.inci17.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import in.co.inci17.R;
import in.co.inci17.slidingtab.SlidingTabLayout;
import in.co.inci17.viewpageradapters.ViewPagerAdapter_Schedule;

public class ScheduleActivityTemporary extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_temporary);
        super.onCreateDrawer();
        navigationView.getMenu().getItem(1).setChecked(true);

    }
}
;