package com.samjang.myclinica;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.samjang.myclinica.model.User;
import com.samjang.myclinica.session.SessionPersistence;

public class HomeActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button logoutBtn;
    private SessionPersistence session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        session = new SessionPersistence(getApplicationContext());
        User user = session.getUserDetails();
        welcomeText = findViewById(R.id.welcomeText);
        logoutBtn = findViewById(R.id.btnLogout);

        welcomeText.setText("Welcome "+user.getFirstName());

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                Intent i = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });


    }


}
