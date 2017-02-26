package in.co.inci17.viewpageradapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import in.co.inci17.Fragments.schedule.FragmentFive;
import in.co.inci17.Fragments.schedule.FragmentFour;
import in.co.inci17.Fragments.schedule.FragmentOne;
import in.co.inci17.Fragments.schedule.FragmentThree;
import in.co.inci17.Fragments.schedule.FragmentTwo;

public class ViewPagerAdapter_Schedule extends FragmentPagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter_home is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter_home is created

    private Context mContext;
    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter_Schedule(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, Context context) {
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
            FragmentOne frag_one = new FragmentOne();
            return frag_one;
        }
        else if(position == 1)
        {
            FragmentTwo frag_two = new FragmentTwo();
            return frag_two;
        }
        else if(position == 2){
            FragmentThree frag_three = new FragmentThree();
            return frag_three;
        }
        else if(position == 3){
            FragmentFour frag_four = new FragmentFour();
            return frag_four;
        }
        else{
            FragmentFive frag_five = new FragmentFive();
            return frag_five;
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

