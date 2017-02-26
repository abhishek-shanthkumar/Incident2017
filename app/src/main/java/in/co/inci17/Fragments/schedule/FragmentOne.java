package in.co.inci17.Fragments.schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.co.inci17.R;
import in.co.inci17.adapters.FragmentOneEventListAdapter;
import in.co.inci17.adapters.SlamDunkMatchesAdapter;

public class FragmentOne extends Fragment {

    RecyclerView rvFragmentOneEvents;
    LinearLayoutManager mLayoutManager;
    FragmentOneEventListAdapter mFragmentOneEventListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        rvFragmentOneEvents = (RecyclerView) view.findViewById(R.id.rv_fragment_one_events);

        mLayoutManager = new LinearLayoutManager(view.getContext());
        mFragmentOneEventListAdapter = new FragmentOneEventListAdapter(view.getContext().getApplicationContext());
        rvFragmentOneEvents.setLayoutManager(mLayoutManager);
        rvFragmentOneEvents.setItemAnimator(new DefaultItemAnimator());
        rvFragmentOneEvents.setAdapter(mFragmentOneEventListAdapter);

        return view;
    }

}
