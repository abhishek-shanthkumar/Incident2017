package in.co.inci17.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import in.co.inci17.R;
import in.co.inci17.auxiliary.User;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME = 2000;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        CountDownTimer timer = new CountDownTimer(SPLASH_TIME,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(intent);
            }
        };

        timer.start();

        if(User.getCurrentUser(this)==null)
            intent = new Intent(this, LoginActivity.class);
        else
            intent = new Intent(this, HomeActivity.class);

    }
}
