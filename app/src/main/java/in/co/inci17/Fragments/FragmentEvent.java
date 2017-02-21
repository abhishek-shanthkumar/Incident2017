package in.co.inci17.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import in.co.inci17.R;
import in.co.inci17.auxiliary.Event;

/**
 * Created by RK on 21/02/2017.
 */
public class FragmentEvent extends Fragment {

    View v;
    ImageView eventImage;
    TextView eventName;
    TextView eventDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_event,container,false);
        eventImage=(ImageView)v.findViewById(R.id.iv_event_image);
        eventName = (TextView) v.findViewById(R.id.tv_event_name);
        eventDescription = (TextView) v.findViewById(R.id.tv_desc);

        Gson gson;
        gson = new Gson();

        Event event = gson.fromJson(getArguments().getString("event"), Event.class);

        int b=getArguments().getInt("pos");
        if((b%2)==0)
            eventImage.setImageResource(R.drawable.raghu);
        else
            eventImage.setImageResource(R.drawable.dj);

        eventName.setText(event.getTitle());
        eventDescription.setText(event.getDescription());

        return v;
    }

    public void scaleImage(float scaleX)
    {
        v.setScaleY(scaleX);
        v.invalidate();
    }

}


