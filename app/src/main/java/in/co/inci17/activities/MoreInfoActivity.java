package in.co.inci17.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.co.inci17.R;
import in.co.inci17.auxiliary.Constants;
import in.co.inci17.auxiliary.CustomRequest;
import in.co.inci17.auxiliary.User;

public class MoreInfoActivity extends Activity implements View.OnClickListener{

    User user;
    EditText name, college, course, year, mobile;
    List<EditText> editTexts;
    Button proceed;
    RequestQueue mRequestQueue;
    String defaultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        Gson gson = new Gson();
        user = gson.fromJson(getIntent().getStringExtra("user"), User.class);
        editTexts = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this.getApplicationContext());

        name = (EditText) findViewById(R.id.et_name);
        college = (EditText) findViewById(R.id.et_college_name);
        course = (EditText) findViewById(R.id.et_batch);
        year = (EditText) findViewById(R.id.et_specialisation);
        mobile = (EditText) findViewById(R.id.et_mobile);
        proceed = (Button) findViewById(R.id.b_continue_to_course_selection);

        editTexts.add(name);
        editTexts.add(college);
        editTexts.add(course);
        editTexts.add(year);
        editTexts.add(mobile);

        name.setText(user.getDisplayName());
        defaultText = proceed.getText().toString();
        proceed.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.b_continue_to_course_selection:
                registerUser();
        }
    }

    private void registerUser() {
        for(EditText editText : editTexts)
            if(TextUtils.isEmpty(editText.getText().toString())) {
                editText.setError("Field cannot be empty");
                return;
            }

        user.setDisplayName(name.getText().toString());

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.Keys.ACCOUNT_SIGNI_IN, "1'");
        //Log.d("Unique id", user.getUniqueId());
        params.put(Constants.Keys.ACCOUNT_REG_ID, user.getUniqueId());
        params.put(Constants.Keys.ACCOUNT_NAME, user.getDisplayName());
        params.put(Constants.Keys.ACCOUNT_EMAIL, user.getEmail());
        params.put(Constants.Keys.ACCOUNT_COLLEGE, college.getText().toString());
        params.put(Constants.Keys.ACCOUNT_MOBILE, mobile.getText().toString());
        params.put(Constants.Keys.ACCOUNT_DEGREE, course.getText().toString());
        params.put(Constants.Keys.ACCOUNT_YEAR, year.getText().toString());
        params.put(Constants.Keys.ACCOUNT_IMAGE, user.getImageUrl());

        CustomRequest request = new CustomRequest(Constants.URLs.CREATE_USER_ACCOUNT, params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject object = response.getJSONObject(0);
                            String output = object.getString(Constants.Keys.OUTPUT);
                            if(!output.equals("1"))
                                Toast.makeText(MoreInfoActivity.this, "Error: "+object.getString(Constants.Keys.ERROR), Toast.LENGTH_SHORT).show();
                            else {
                                userRegistered(user, object.getString(Constants.Keys.ACCOUNT_ID));
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MoreInfoActivity.this, Constants.Messages.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                            Log.e("JSON", e.getLocalizedMessage());
                        }
                        if(proceed != null) {
                            proceed.setEnabled(true);
                            proceed.setText(defaultText);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MoreInfoActivity.this, "An error occurred. Please try later", Toast.LENGTH_SHORT).show();
                        proceed.setText(defaultText);
                        proceed.setEnabled(true);
                    }
                });

        mRequestQueue.add(request);
        proceed.setText("Please wait...");
        proceed.setEnabled(false);
    }

    private void userRegistered(User user, String accountID) {
        user.setId(accountID);
        User.updateUser(user, this);

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        this.finish();
    }
}
