package in.co.inci17.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import in.co.inci17.R;
import in.co.inci17.auxiliary.User;

public class MainActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if ((user = User.getCurrentUser(this)) == null)
            sendUserToLoginScreen();

        updateLayout();
    }

    private void updateLayout() {
        TextView tvName, tvEmail;
        ImageView ivProfilePic;

        tvName = (TextView) findViewById(R.id.tv_name);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        ivProfilePic = (ImageView) findViewById(R.id.iv_profile_pic);

        tvName.setText(user.getDisplayName());
        tvEmail.setText(user.getEmail());
        Picasso.with(this).load(user.getImageUrl()).into(ivProfilePic);
    }

    private void sendUserToLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }
}
