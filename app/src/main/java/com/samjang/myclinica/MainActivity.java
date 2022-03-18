package com.samjang.myclinica;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.samjang.myclinica.db.DatabaseHelper;



public class MainActivity extends AppCompatActivity {
    private TextView tvRegister;
    private EditText logEmail, logPassword;
    private Button loginBtn;

    private SQLiteDatabase db;
    private SQLiteOpenHelper openHelper;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openHelper = new DatabaseHelper(this);
        db = openHelper.getReadableDatabase();

        tvRegister = findViewById(R.id.tvRegister);
        logEmail = findViewById(R.id.etLogGmail);
        logPassword = findViewById(R.id.etLoginPassword);
        loginBtn = findViewById(R.id.btnLogin);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = logEmail.getText().toString().trim();
                String password = logPassword.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter your Email and Password correctly to login", Toast.LENGTH_SHORT).show();
                } else {
                    cursor = db.rawQuery("SELECT *FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.COL_4 + "=? AND " + DatabaseHelper.COL_5 + "=?", new String[]{email, password});
                    if (cursor != null) {
                        if (cursor.getCount() > 0) {
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });



        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                finish();
            }
        });

    }
}