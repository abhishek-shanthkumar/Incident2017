package in.co.inci17.auxiliary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;


import java.util.Random;

public class BottomFadeEdgeRV extends RecyclerView {

    public BottomFadeEdgeRV(Context context) {
        super(context);
    }

    public BottomFadeEdgeRV(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomFadeEdgeRV(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected float getTopFadingEdgeStrength() {
        return 0;
    }

}