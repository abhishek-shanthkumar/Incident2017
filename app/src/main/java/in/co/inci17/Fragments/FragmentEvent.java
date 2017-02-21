package in.co.inci17.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import in.co.inci17.R;

/**
 * Created by RK on 21/02/2017.
 */
public class FragmentEvent extends Fragment {

    View v;
    ImageView eventImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_event,container,false);
        eventImage=(ImageView)v.findViewById(R.id.iv_event_image);

        int b=getArguments().getInt("pos");
        if((b%2)==0)
            eventImage.setImageResource(R.drawable.raghu);
        else
            eventImage.setImageResource(R.drawable.dj);

        return v;
    }

    public void scaleImage(float scaleX)
    {
        v.setScaleY(scaleX);
        v.invalidate();
    }

}


