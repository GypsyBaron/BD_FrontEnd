package com.example.bakalaurinis_v2.Authentification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bakalaurinis_v2.MainWindowActivity;
import com.example.bakalaurinis_v2.R;
import com.example.bakalaurinis_v2.ResponseForms.LoginResponseForm;
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

public class LoginActivity extends AppCompatActivity {

    private EditText surname;
    private EditText password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        surname = findViewById(R.id.login_activity_surname);
        password = findViewById(R.id.login_activity_password);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
    }

    private void checkLogin() {

        OkHttpClient client = new OkHttpClient();

        String url = getApplication().getBaseContext().getString(R.string.url) + "users/login";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", surname.getText().toString());
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
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            Gson gson = new Gson();
                            LoginResponseForm response = gson.fromJson(myResponse, LoginResponseForm.class);
                            if (response.getId().equals("null")) {
                                Toast.makeText(getApplicationContext(), response.getResponse(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), response.getResponse(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainWindowActivity.class);
                                intent.putExtra("id", response.getId());
                                intent.putExtra("fragment", "home");
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });
    }
}
