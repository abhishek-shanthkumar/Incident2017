package in.co.inci17.activities;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import in.co.inci17.R;
import in.co.inci17.auxiliary.Constants;

public class SponsorsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_temporary);
        super.onCreateDrawer();
        navigationView.getMenu().getItem(5).setChecked(true);
        ((TextView) findViewById(R.id.tv_title)).setText("FAQs");

        String url;
        url = getIntent().getStringExtra("url");
        if(url == null) {
            url = Constants.URLs.SPONSORS;
            navigationView.getMenu().getItem(8).setChecked(true);
            ((TextView) findViewById(R.id.tv_title)).setText("Sponsors");
        }

        WebView webView = (WebView) findViewById(R.id.schedule_wv);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}
