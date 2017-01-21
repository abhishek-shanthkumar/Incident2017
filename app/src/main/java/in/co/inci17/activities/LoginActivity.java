package in.co.inci17.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.SignInButton;

import butterknife.BindView;
import in.co.inci17.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    SignInButton signIn_google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signIn_google = (SignInButton)findViewById(R.id.google_signin_button);

        //OnClickListeners
        signIn_google.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.google_signin_button:
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;
        }
    }
}
