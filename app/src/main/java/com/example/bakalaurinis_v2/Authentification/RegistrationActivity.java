package com.example.bakalaurinis_v2.Authentification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bakalaurinis_v2.MainActivity;
import com.example.bakalaurinis_v2.R;
import com.example.bakalaurinis_v2.ResponseForms.RegistrationResponseForm;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistrationActivity extends AppCompatActivity {

    private EditText surname;
    private EditText password;
    private EditText email;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        email = findViewById(R.id.registration_activity_email);
        surname = findViewById(R.id.registration_activity_surname);
        password = findViewById(R.id.registration_activity_password);
        register = findViewById(R.id.registerButton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAccount();
            }
        });
    }

    private void registerAccount() {
        OkHttpClient client = new OkHttpClient();

        String url = getApplication().getBaseContext().getString(R.string.url) + "users/register";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", surname.getText().toString());
            jsonObject.put("email", email.getText().toString());
            jsonObject.put("password", password.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json;"), jsonObject.toString()))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                System.out.println(response);
                final String myResponse = response.body().string();
                RegistrationActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            Gson gson = new Gson();
                            RegistrationResponseForm response = gson.fromJson(myResponse, RegistrationResponseForm.class);
                            if (!response.getResponse().equals("Paskyra sukurta")) {
                                Toast.makeText(getApplicationContext(), response.getResponse(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), response.getResponse(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });
    }
}
