package com.example.apptrace;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptrace.session.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SessionManager sessionManager = SessionManager.getInstance(this);

        Intent intent;
        if (sessionManager.isLoggedIn()) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}