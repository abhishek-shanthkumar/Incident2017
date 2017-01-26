package in.co.inci17.activities;

import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;

import in.co.inci17.R;

public class FAQActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        super.onCreateDrawer();
        navigationView.getMenu().getItem(6).setChecked(true);
        ((NavigationMenuView)navigationView.getChildAt(0)).scrollToPosition(6);
    }
}
