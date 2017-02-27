package in.co.inci17.auxiliary;
/*
 * Created by Abhishek on 19-01-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.text.SimpleDateFormat;
import java.util.Locale;

import in.co.inci17.R;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class Helper {

    public final static SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.UK);

    public static Bitmap getQRCode(String content) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 500, 500);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? BLACK : WHITE;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            Log.e("QR Generator", e.getLocalizedMessage());
            return null;
        }
    }

    public static void shareEvent(Context context, Event event) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String time = Helper.timeFormat.format(event.getStartDateTime()).replaceAll("\\.","");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, check out "+event.getTitle()+" starting at "+time+ " in "+event.getVenue()+"!");
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, context.getResources().getText(R.string.send_to)));
    }
}
