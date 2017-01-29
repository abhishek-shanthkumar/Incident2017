package in.co.inci17.activities;

import android.os.Bundle;

import in.co.inci17.R;

public class MapActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        super.onCreateDrawer();
        navigationView.getMenu().getItem(3).setChecked(true);
    }
}
