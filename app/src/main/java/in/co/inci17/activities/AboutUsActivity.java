package in.co.inci17.activities;

import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;

import in.co.inci17.R;

public class AboutUsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        super.onCreateDrawer();
        navigationView.getMenu().getItem(10).setChecked(true);
        ((NavigationMenuView)navigationView.getChildAt(0)).scrollToPosition(10);
    }
}