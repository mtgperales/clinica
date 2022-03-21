package com.samjang.myclinica;

//import static androidx.core.content.ContextCompat.startActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

import com.samjang.myclinica.db.MySingleton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    private EditText regFirstName, regMiddleName, regLastName, regBirthday, regPhone, regEmail, regPassword, regConfirmPassword;
    private Button registerBtn,loginBtn;

    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_MIDDLE_NAME = "middleName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_BIRTH_DAY = "birthDay";
    private static final String KEY_PHONE_NUM = "phoneNum";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";
    private String register_url = "http://192.168.100.13/myClinica/register.php";
 //   private SessionHandler session;
    private ProgressDialog pDialog;

    private String firstName;
    private String middleName;
    private String lastName;
    private String birthDay;
    private String phoneNum;
    private String email;
    private String password;
    private String confirmPassword;

    final Calendar myCalendar= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    session = new SessionHandler(getApplicationContext());
        setContentView(R.layout.activity_register);

        loginBtn = findViewById(R.id.btnLogin);
        registerBtn = findViewById(R.id.btnRegister);

        regFirstName = findViewById(R.id.etRegFirstName);
        regMiddleName = findViewById(R.id.etRegMiddleName);
        regLastName = findViewById(R.id.etRegLastName);
        regBirthday = findViewById(R.id.etBirthday);
        regPhone = findViewById(R.id.etPhone);
        regEmail = findViewById(R.id.etRegEmail);
        regPassword = findViewById(R.id.etRegPassword);
        regConfirmPassword = findViewById(R.id.etRegConfirmPassword);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        regBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(RegisterActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName = regFirstName.getText().toString().trim();
                middleName = regMiddleName.getText().toString().trim();
                lastName = regLastName.getText().toString().trim();
                birthDay = regBirthday.getText().toString().trim();
                phoneNum = regPhone.getText().toString().trim();
                email = regEmail.getText().toString().trim();
                password = regPassword.getText().toString().trim();
                confirmPassword = regConfirmPassword.getText().toString().trim();
                if (validateInputs()) {
                    registerUser();
                }
            }
        });



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        regBirthday.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void loadHome() {
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(i);
        finish();

    }

    private void displayLoader() {
        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage("Signing Up.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void registerUser() {
        displayLoader();
       JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_EMAIL, email);
            request.put(KEY_PASSWORD, password);
            request.put(KEY_FIRST_NAME, firstName);
            request.put(KEY_MIDDLE_NAME, middleName);
            request.put(KEY_LAST_NAME, lastName);
            request.put(KEY_PHONE_NUM, phoneNum);
            request.put(KEY_BIRTH_DAY, birthDay);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*Map<String, String> request = new HashMap<>();
        request.put(KEY_FIRST_NAME, firstName);
        request.put(KEY_MIDDLE_NAME, middleName);
        request.put(KEY_LAST_NAME, lastName);
        request.put(KEY_BIRTH_DAY, birthDay);
        request.put(KEY_PHONE_NUM, phoneNum);
        request.put(KEY_EMAIL, email);
        request.put(KEY_PASSWORD, password);*/
        Log.d("tag", request.toString());
        Log.d("JSONtest", "Pre-Testing JSONreq");
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, register_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                        pDialog.dismiss();
                        Log.d("JSONtest", "Confirm JSONreq");
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                //Set the user session
                                //session.loginUser(username,fullName);
                                loadHome();

                            }else if(response.getInt(KEY_STATUS) == 1){
                                //Display error message if email is already used
                                regEmail.setError("Use another Email!");
                                regEmail.requestFocus();

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
                        //Log.d("testReq", request.toString());
                        //Display error message whenever an error occurs
                        //Toast.makeText(getApplicationContext(),
                          //      error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    /**
     * Validates inputs and shows error if any
     * @return
     */
    private boolean validateInputs() {
        if (KEY_EMPTY.equals(firstName)) {
            regFirstName.setError("First Name cannot be empty");
            regFirstName.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(middleName)) {
            regMiddleName.setError("Middle Name cannot be empty");
            regMiddleName.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(lastName)) {
            regLastName.setError("Last Name cannot be empty");
            regLastName.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(birthDay)) {
            regBirthday.setError("Birth day cannot be empty");
            regBirthday.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(phoneNum)) {
            regPhone.setError("Phone Number cannot be empty");
            regPhone.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(email)) {
            regEmail.setError("Email cannot be empty");
            regEmail.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(password)) {
            regPassword.setError("Password cannot be empty");
            regPassword.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(confirmPassword)) {
            regConfirmPassword.setError("Confirm Password cannot be empty");
            regConfirmPassword.requestFocus();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            regConfirmPassword.setError("Password and Confirm Password does not match");
            regConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }


}