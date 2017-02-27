package in.co.inci17.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.co.inci17.R;
import in.co.inci17.auxiliary.Constants;
import in.co.inci17.auxiliary.CustomRequest;
import in.co.inci17.auxiliary.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 812;
    private final String TAG = "LoginActivity";
    private RequestQueue mRequestQueue;
    private GoogleApiClient mGoogleApiClient;
    private LoginButton facebookSigninButton;
    private CallbackManager callbackManager;
    //SignInButton signIn_google;

    LinearLayout b_google, b_facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        b_google = (LinearLayout) findViewById (R.id.container_google_signin);
        b_facebook = (LinearLayout) findViewById (R.id.container_fb_signin);

        mRequestQueue = Volley.newRequestQueue(this);
        prepareGoogleSignin();
        prepareFacebookSignin();
        findViewById(R.id.google_signin_button).setOnClickListener(this);

        b_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signinWithGoogle();
            }
        });
        b_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookSigninButton.performClick();
            }
        });
    }

    private void prepareFacebookSignin() {
        LoginManager.getInstance().logOut();

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
                        if(object.has("email"))
                            createUser(object);
                        else {
                            Toast.makeText(LoginActivity.this, Constants.Messages.EMAIL_NEEDED, Toast.LENGTH_SHORT).show();
                            LoginManager.getInstance().logOut();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(500).height(500)");
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
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient);
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        });
        mGoogleApiClient.connect();
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
            if(acct != null) {
                String email = acct.getEmail();
                if (email == null || email.isEmpty()) {
                    Toast.makeText(this, Constants.Messages.EMAIL_NEEDED, Toast.LENGTH_SHORT).show();
                    Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient);
                    return;
                }
                createUser(acct);
            }
            else
                handleSigninError();
        } else {
            // Signed out, show unauthenticated UI.
            handleSigninError();
        }
    }

    private void handleSigninError() {
        Toast.makeText(this, Constants.Messages.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
    }

    private void createUser(JSONObject object) {
        //Profile profile = Profile.getCurrentProfile();
        User user = new User();
        //user.setDisplayName(profile.getName());
        try {
            user.setDisplayName(object.getString("name"));
            user.setEmail(object.getString("email"));
            //Log.d("Facebook", object.toString());
            user.setImageUrl(object.getJSONObject("picture").getJSONObject("data").getString("url"));
            //user.setImageUrl(profile.getProfilePictureUri(500, 500).toString());
            Toast.makeText(this, Constants.Messages.FACEBOOK_SIGNIN_SUCCESS +"\nWelcome, "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
            authenticateUserWithServer(user);
        } catch (JSONException e) {
            handleSigninError();
        }
    }

    private void createUser(GoogleSignInAccount account) {
        Toast.makeText(this, Constants.Messages.GOOGLE_SIGNIN_SUCCESS +"\nWelcome, "+account.getDisplayName(), Toast.LENGTH_SHORT).show();
        User user = new User();
        user.setDisplayName(account.getDisplayName());
        user.setEmail(account.getEmail());
        //noinspection ConstantConditions
        if(account.getPhotoUrl()!=null)
            user.setImageUrl(account.getPhotoUrl().toString());
        authenticateUserWithServer(user);
    }

    private void authenticateUserWithServer(final User user) {
        String url = Constants.URLs.USER_PRESENT;
        final HashMap<String, String> params = new HashMap<>();
        params.put(Constants.Keys.EMAIL, user.getEmail());

        CustomRequest mRequest = new CustomRequest(url, params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        String present, accountID, error;
                        try {
                            JSONObject response = responseArray.getJSONObject(0);
                            present = response.getString(Constants.Keys.PRESENT);
                            accountID = response.getString(Constants.Keys.ACCOUNT_ID);
                            error = response.getString(Constants.Keys.ERROR);
                            if(!error.isEmpty())
                                handleSigninError(error);
                            else {
                                if (present.equals("1"))
                                    userAlreadyRegistered(user, accountID);
                                else
                                    registerUser(user);
                            }
                        } catch (JSONException e) {
                            Log.e("JSONError", e.getLocalizedMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.getLocalizedMessage());
                        handleSigninError();
                    }
                });
        mRequest.setTag(TAG);
        mRequestQueue.add(mRequest);
    }

    private void handleSigninError(String error) {
        Toast.makeText(this, "Server error: "+error, Toast.LENGTH_SHORT).show();
    }

    private void userAlreadyRegistered(User user, String accountID) {
        user.setId(accountID);
        if(user.getImageUrl() == null)
            user.setImageUrl(Constants.URLs.PLACEHOLDER_IMAGE);
        User.updateUser(user, this);

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void registerUser(User user) {
        //Toast.makeText(this, "Sorry, you need to register on the website.", Toast.LENGTH_SHORT).show();
        if(user.getImageUrl() == null)
            user.setImageUrl(Constants.URLs.PLACEHOLDER_IMAGE);

        Gson gson = new Gson();
        Intent intent = new Intent(this, MoreInfoActivity.class);
        intent.putExtra("user", gson.toJson(user));
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        handleSigninError();
    }

    @Override
    protected void onStop () {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }
}
