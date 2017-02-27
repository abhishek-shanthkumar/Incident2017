package in.co.inci17.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import in.co.inci17.R;
import in.co.inci17.auxiliary.User;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME = 2000;
    private Intent intent;
    private CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        timer = new CountDownTimer(SPLASH_TIME,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(intent);
                finish();
            }
        };

        //timer.start();

        UpdateRequestDialog updateRequestDialog = new UpdateRequestDialog(this);
        Window window = updateRequestDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

//        if(date_from_server==28_02)
//            updateRequestDialog.show();

        if(User.getCurrentUser(this)==null)
            intent = new Intent(this, LoginActivity.class);
        else
            intent = new Intent(this, HomeActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    public class UpdateRequestDialog extends Dialog {

        public Activity c;
        public Dialog d;
        public String id;
        Context context;

        Button cancel, update;

        public UpdateRequestDialog(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.context = context;
            this.id = id;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_box_update);

            cancel = (Button) findViewById(R.id.b_cancel);
            update = (Button) findViewById(R.id.b_update);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Take the user to PlayStore
                }
            });
        }
    }
}
