package com.demo.eduscope;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.VolleyError;
import com.demo.eduscope.VolleyService.IResult;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private SharedPrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressBar = findViewById(R.id.progressBar);
        prefManager = new SharedPrefManager(this);
        Button login = findViewById(R.id.buttonLogin);

        //if the user is already logged in we will directly start the profile activity
        if (prefManager.isLoggedIn()) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }

        //if user presses on login
        //calling the method login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        //if user presses on not registered
        findViewById(R.id.textViewRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                finish();
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void userLogin() {
        //first getting the values
        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter your email");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return;
        }

        //if everything is fine
        IResult mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,JSONObject response) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);

                progressBar.setVisibility(View.GONE);

                try {
                    //if no error in response
                    if (!response.getBoolean("error")) {
                        //Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = response.getJSONObject("user");

                        //creating a new user object
                        User user = new User(userJson.getString("id"), userJson.getString("username"),
                                userJson.getString("email"));

                        //storing the user in shared preferences
                        prefManager.userLogin(user);

                        //starting the profile activity
                        finish();
                        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //e.printStackTrace();
                    Log.e(TAG,"Exception caught: "+e.getMessage());
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + "That didn't work!");
                Toast.makeText(getApplicationContext(),"Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        };
        progressBar.setVisibility(View.VISIBLE);
        VolleyService mVolleyService = new VolleyService(mResultCallback,this);
        JSONObject sendObj=null;
        try {
            sendObj = new JSONObject("{\"email\": \""+email+"\", \"password\": \""+password+"\"}");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //sending the request
        mVolleyService.postDataVolley(URLs.URL_LOGIN,sendObj);
    }
}