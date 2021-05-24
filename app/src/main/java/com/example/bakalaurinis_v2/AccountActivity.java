package com.example.bakalaurinis_v2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends AppCompatActivity {

    private String id;
    private Spinner genderSpinner;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
        }

        genderSpinner = findViewById(R.id.account_gender);
        setSpinnerValues();

        backButton = findViewById(R.id.account_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainWindowActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("fragment", "home");
                startActivity(intent);
            }
        });
    }

    private void setSpinnerValues() {
        List<String> genderList = new ArrayList<String>();
        genderList.add("Vyras");
        genderList.add("Moteris");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_spinner_item, genderList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(dataAdapter);
    }
}
