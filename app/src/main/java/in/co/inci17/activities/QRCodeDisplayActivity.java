package in.co.inci17.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import in.co.inci17.R;
import in.co.inci17.auxiliary.Constants;
import in.co.inci17.auxiliary.Helper;

public class QRCodeDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_display);

        String displayString = getIntent().getStringExtra(Constants.QR_CODE_CONTENT);
        Bitmap bitmap = Helper.getQRCode(displayString);

        ImageView qrCodeDisplay = (ImageView) findViewById(R.id.iv_qr_code);
        qrCodeDisplay.setImageBitmap(bitmap);
    }
}
