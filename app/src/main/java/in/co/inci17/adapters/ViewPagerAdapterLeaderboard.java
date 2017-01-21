package in.co.inci17.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import in.co.inci17.Fragments.FragmentSlamDunk;
import in.co.inci17.Fragments.FragmentVolleyBall;


/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerAdapterLeaderboard extends FragmentPagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapterLeaderboard is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapterLeaderboard is created

    private Context mContext;
    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapterLeaderboard(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, Context context) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.mContext = context;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0)
        {
           FragmentSlamDunk frag_slamdunk = new FragmentSlamDunk();
           return frag_slamdunk;
        }
        else
        {
            FragmentVolleyBall frag_volleyball = new FragmentVolleyBall();
            return frag_volleyball;
        }

    }



    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }


    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
