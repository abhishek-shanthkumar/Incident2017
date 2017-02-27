package in.co.inci17.activities;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import in.co.inci17.R;
import in.co.inci17.auxiliary.Constants;

public class ScheduleActivityTemporary extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_temporary);
        super.onCreateDrawer();
        navigationView.getMenu().getItem(1).setChecked(true);

        ((TextView) findViewById(R.id.tv_title)).setText("Schedule");

        WebView webView = (WebView) findViewById(R.id.schedule_wv);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(Constants.URLs.DRIVE_SCHEDULE_URL);

        /*DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        manager.enqueue(new DownloadManager.Request(Uri.parse(Constants.URLs.SCHEDULE_URL)));

        BroadcastReceiver*/

    }
}
