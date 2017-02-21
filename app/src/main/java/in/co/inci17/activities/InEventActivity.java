package in.co.inci17.activities;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Response;

import java.util.List;

import in.co.inci17.Fragments.FragmentEvent;
import in.co.inci17.R;
import in.co.inci17.adapters.EventPagerAdapter;
import in.co.inci17.auxiliary.Event;
import in.co.inci17.auxiliary.EventsManager;

public class InEventActivity extends AppCompatActivity {

    ViewPager vpEvent;
    List<Event> events;

    Integer[] colors = {Color.parseColor("#322426"), Color.parseColor("#2A2865"), Color.parseColor("#322426"), Color.parseColor("#2A2865")};
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_event);

        vpEvent = (ViewPager) findViewById(R.id.vp_event);

        vpEvent.setClipToPadding(false);
        vpEvent.setPageMargin(24);
        vpEvent.setPadding(96, 192, 96, 192);

        EventsManager.getAllEvents(this, new Response.Listener<List<Event>>() {
            @Override
            public void onResponse(List<Event> response) {
                events = response;
                //vpEvent.getAdapter().notifyDataSetChanged();
                vpEvent.setAdapter(new EventPagerAdapter(getSupportFragmentManager(), events));
                vpEvent.setCurrentItem(getIntent().getIntExtra("id", 0));
            }
        });

        //vpEvent.setAdapter(new EventPagerAdapter(getSupportFragmentManager(), events));
        vpEvent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if(position < (4 -1) && position < (4 - 1)) {

                    vpEvent.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]));

                } else {

                    vpEvent.setBackgroundColor(colors[colors.length - 1]);

                }


                FragmentEvent sampleFragment = (FragmentEvent) ((EventPagerAdapter) vpEvent.getAdapter()).getRegisteredFragment(position);

                float scale = 1 - (positionOffset * 0.25f);

                // Just a shortcut to findViewById(R.id.image).setScale(scale);
                sampleFragment.scaleImage(scale);


                if (position + 1 < vpEvent.getAdapter().getCount()) {
                    sampleFragment = (FragmentEvent) ((EventPagerAdapter) vpEvent.getAdapter()).getRegisteredFragment(position + 1);
                    scale = positionOffset * 0.25f + (1 - 0.25f);
                    sampleFragment.scaleImage(scale);
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    FragmentEvent fragment = (FragmentEvent) ((EventPagerAdapter) vpEvent.getAdapter()).getRegisteredFragment(vpEvent.getCurrentItem());
                    fragment.scaleImage(1);
                    if (vpEvent.getCurrentItem() > 0) {
                        fragment = (FragmentEvent) ((EventPagerAdapter) vpEvent.getAdapter()).getRegisteredFragment(vpEvent.getCurrentItem() - 1);
                        fragment.scaleImage(1 - 0.25f);
                    }

                    if (vpEvent.getCurrentItem() + 1 < vpEvent.getAdapter().getCount()) {
                        fragment = (FragmentEvent) ((EventPagerAdapter) vpEvent.getAdapter()).getRegisteredFragment(vpEvent.getCurrentItem() + 1);
                        fragment.scaleImage(1 - 0.25f);
                    }
                }

            }
        });
    }
}

