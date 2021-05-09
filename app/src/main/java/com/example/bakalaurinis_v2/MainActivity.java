package com.example.bakalaurinis_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bakalaurinis_v2.Authentification.LoginActivity;
import com.example.bakalaurinis_v2.Authentification.RegistrationActivity;

public class MainActivity extends AppCompatActivity {

    private Button navigate_to_login;
    private Button navigate_to_registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigate_to_login = findViewById(R.id.navigate_to_login);
        navigate_to_registration = findViewById(R.id.navigate_to_registration);

        navigate_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginWindow();
            }
        });

        navigate_to_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegistrationWindow();
            }
        });
    }

    private void openLoginWindow() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

    private void openRegistrationWindow() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}
