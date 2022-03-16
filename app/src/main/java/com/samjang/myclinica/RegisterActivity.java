package com.samjang.myclinica;

//import static androidx.core.content.ContextCompat.startActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.samjang.myclinica.db.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private EditText regFirstName, regMiddleName, regLastName, regBirthday, regPhone, regEmail, regPassword, regConfirmPassword;
    private Button registerBtn,loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        openHelper = new DatabaseHelper(this);

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

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = openHelper.getWritableDatabase();
                String firstName = regFirstName.getText().toString().trim();
                String middleName = regMiddleName.getText().toString().trim();
                String lastName = regLastName.getText().toString().trim();
                String birthDay = regBirthday.getText().toString().trim();
                String phoneNum = regPhone.getText().toString().trim();
                String email = regEmail.getText().toString().trim();
                String password = regPassword.getText().toString().trim();
                String confirmPassword = regConfirmPassword.getText().toString().trim();

                if (firstName.isEmpty() || middleName.isEmpty() || lastName.isEmpty() || birthDay.isEmpty() || phoneNum.isEmpty()
                        || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                } else {
                    insertData(firstName, middleName, lastName, birthDay, phoneNum, email, password);
                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
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
    public void insertData(String firstName,String middleName,String lastName,String birthDay, String phoneNum,
                           String email, String password){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_2,email);
        contentValues.put(DatabaseHelper.COL_3,password);
        contentValues.put(DatabaseHelper.COL_4,firstName);
        contentValues.put(DatabaseHelper.COL_5,middleName);
        contentValues.put(DatabaseHelper.COL_6,lastName);
        contentValues.put(DatabaseHelper.COL_7,phoneNum);
        contentValues.put(DatabaseHelper.COL_8,birthDay);

        long id = db.insert(DatabaseHelper.TABLE_NAME,null,contentValues);
    }
}