package com.samjang.myclinica;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.samjang.myclinica.db.MySingleton;
import com.samjang.myclinica.session.SessionPersistence;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private TextView tvRegister;
    private EditText logEmail, logPassword;
    private Button loginBtn;

    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";

    private String email, password;
    private ProgressDialog pDialog;
    private String login_url = "http://192.168.100.13/myClinica/login.php";
    private SessionPersistence session;


    //LOGIN SCREEN
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //LOGIN SESSION
        session = new SessionPersistence(getApplicationContext());
        if(session.isLoggedIn()){
            loadHome();
        }

        setContentView(R.layout.activity_main);

        tvRegister = findViewById(R.id.tvRegister);

        logEmail = findViewById(R.id.etLogEmail);
        logPassword = findViewById(R.id.etLogPassword);

        loginBtn = findViewById(R.id.btnLogin);

        //TextView clickable redirect to Signup
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                finish();
            }
        });

        //LOGIN
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                email = logEmail.getText().toString().trim();
                password = logPassword.getText().toString().trim();
                if (validateInputs()) {
                    login();
                }
            }
        });

    }

    //Redirect to Home Activity(MAIN PAGE) after succesful login
    private void loadHome() {
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(i);
        finish();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Logging in...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
    private void login() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_EMAIL, email);
            request.put(KEY_PASSWORD, password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, login_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.d("response", response.toString());
                        try {
                            //Check if user got logged in successfully

                            if (response.getInt(KEY_STATUS) == 0) {
                                session.loginUser(email,response.getString(KEY_FIRST_NAME));
                                loadHome();

                            }else{
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private boolean validateInputs() {
        if(KEY_EMPTY.equals(email)){
            logEmail.setError("Email field must have a value");
            logEmail.requestFocus();
            return false;
        }
        if(KEY_EMPTY.equals(password)){
            logPassword.setError("Password field must have a value");
            logPassword.requestFocus();
            return false;
        }
        return true;
    }
}