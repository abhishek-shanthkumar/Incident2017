package in.co.inci17.activities;

import android.os.Bundle;
import android.webkit.WebView;

import in.co.inci17.R;
import in.co.inci17.auxiliary.Constants;

public class ScheduleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        super.onCreateDrawer();
        navigationView.getMenu().getItem(1).setChecked(true);

        WebView webView = (WebView) findViewById(R.id.schedule_wv);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(Constants.URLs.DRIVE_SCHEDULE_URL);

        /*DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        manager.enqueue(new DownloadManager.Request(Uri.parse(Constants.URLs.SCHEDULE_URL)));

        BroadcastReceiver*/
    }
}
