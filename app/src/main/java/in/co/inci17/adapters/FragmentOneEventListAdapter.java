package in.co.inci17.adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import in.co.inci17.R;
import in.co.inci17.activities.InEventActivity;
import in.co.inci17.auxiliary.Constants;
import in.co.inci17.auxiliary.CustomRequest;
import in.co.inci17.auxiliary.Event;
import in.co.inci17.auxiliary.User;
import in.co.inci17.services.EventReminder;

public class FragmentOneEventListAdapter extends RecyclerView.Adapter<FragmentOneEventListAdapter.FragmentOneEventsViewHolder> {
    
    Context context;

    public FragmentOneEventListAdapter(Context mContext) {
        this.context = mContext;

    }

    @Override
    public int getItemCount() {
        return 5;
    }


    @Override
    public void onBindViewHolder(FragmentOneEventsViewHolder mFragmentOneEventsViewHolder, int i) {

    }

    @Override
    public FragmentOneEventsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_layout_event_upcoming, viewGroup, false);
            return new FragmentOneEventsViewHolder(itemView);
    }


    public class FragmentOneEventsViewHolder extends RecyclerView.ViewHolder{

        CardView cardUpcomingEvent;

        public FragmentOneEventsViewHolder(View v) {
            super(v);
            cardUpcomingEvent = (CardView) v.findViewById(R.id.cv_upcoming_event);

            /*//For loading big images (temporary)
            BitmapFactory.Options bm_opts = new BitmapFactory.Options();
            bm_opts.inScaled = false;
            Bitmap imageBM = BitmapFactory.decodeResource(v.getResources(), R.drawable.raghu, bm_opts);
            eventPicture.setImageBitmap(imageBM);

            //To be used for extracting the dominant color
            Palette.from(imageBM).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch vibrantSwatch = palette.getDominantSwatch();

                    if(vibrantSwatch!=null){
                        container_top.setBackgroundColor((vibrantSwatch.getRgb() & 0x00FFFFFF)| 0x99000000);
                        root_layout.setBackgroundColor(vibrantSwatch.getRgb());
                    }

                }
            });*/

        }

    }

}
