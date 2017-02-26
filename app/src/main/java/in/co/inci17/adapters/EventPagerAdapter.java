package in.co.inci17.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.List;

import in.co.inci17.Fragments.FragmentEvent;
import in.co.inci17.auxiliary.Event;

public class EventPagerAdapter extends FragmentStatePagerAdapter {
    SparseArray<Fragment> registeredFragments = new SparseArray<>();
    List<Event> events;
    Gson gson;

    public EventPagerAdapter(FragmentManager fm, List<Event> events) {
        super(fm);
        this.events = events;
        gson = new Gson();
    }

    @Override
    public int getCount() {
        return events == null ? 0 : events.size();
    }

    @Override
    public Fragment getItem(int position) {
        return new FragmentEvent();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        Bundle b=new Bundle();
        b.putInt("pos",position);
        b.putString("eventId", events.get(position).getId());
        b.putString("event", gson.toJson(events.get(position)));
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        try{
            super.finishUpdate(container);
            //events = null;
        } catch (Exception nullPointerException){
            System.out.println("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
        }
    }
}
