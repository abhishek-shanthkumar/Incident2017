package in.co.inci17.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import in.co.inci17.R;
import in.co.inci17.auxiliary.Constants;
import in.co.inci17.auxiliary.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 812;
    private GoogleApiClient mGoogleApiClient;
    private LoginButton facebookSigninButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        prepareGoogleSignin();
        prepareFacebookSignin();
        findViewById(R.id.google_signin_button).setOnClickListener(this);
    }

    private void prepareFacebookSignin() {
        callbackManager = CallbackManager.Factory.create();
        facebookSigninButton = (LoginButton) findViewById(R.id.facebook_signin_button);
        facebookSigninButton.setReadPermissions("email");
        facebookSigninButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                continueFacebookSignin(loginResult);
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Facebook Signin cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                handleSigninError();
            }
        });
    }

    private void continueFacebookSignin(LoginResult loginResult) {
        Log.d("Facebook Signin", "Sending graph request");
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        authenticateUserWithServer(object);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void prepareGoogleSignin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_signin_button:
                signinWithGoogle();
                break;
        }
    }

    private void signinWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            continueGoogleSignin(result);
        }
    }

    private void continueGoogleSignin(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            authenticateUserWIthServer(acct);
        } else {
            // Signed out, show unauthenticated UI.
            handleSigninError();
        }
    }

    private void handleSigninError() {
        Toast.makeText(this, Constants.MESSAGE_NETWORK_ERROR, Toast.LENGTH_SHORT).show();
    }

    private void authenticateUserWithServer(JSONObject object) {
        //TODO Send details to server
        Log.d("Facebook Signin", "Authenticating with server");
        Profile profile = Profile.getCurrentProfile();

        User user = new User();
        user.setDisplayName(profile.getName());
        try {
            user.setEmail(object.getString("email"));
            user.setImageUrl(profile.getProfilePictureUri(500, 500).toString());
            User.updateUser(user, this);

            Toast.makeText(this, Constants.MESSAGE_FACEBOOK_SIGNIN_SUCCESS+"\nWelcome, "+user.getDisplayName(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        } catch (JSONException e) {
            handleSigninError();
        }
    }

    private void authenticateUserWIthServer(GoogleSignInAccount account) {
        Toast.makeText(this, Constants.MESSAGE_GOOGLE_SIGNIN_SUCCESS+"\nWelcome, "+account.getDisplayName(), Toast.LENGTH_SHORT).show();

        //TODO Send details to server

        User user = new User();
        user.setDisplayName(account.getDisplayName());
        user.setEmail(account.getEmail());
        //noinspection ConstantConditions
        user.setImageUrl(account.getPhotoUrl().toString());
        User.updateUser(user, this);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        handleSigninError();
    }
}
