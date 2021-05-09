package com.example.bakalaurinis_v2.Food;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bakalaurinis_v2.Authentification.RegistrationActivity;
import com.example.bakalaurinis_v2.MainActivity;
import com.example.bakalaurinis_v2.MainWindowActivity;
import com.example.bakalaurinis_v2.R;
import com.example.bakalaurinis_v2.ResponseForms.RegistrationResponseForm;
import com.example.bakalaurinis_v2.ResponseForms.ResponseString;
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

public class FoodCreateActivity extends AppCompatActivity {

    private String id;
    private EditText foodName;
    private EditText calories;
    private EditText carbs;
    private EditText proteins;
    private EditText fats;
    private Button backButton;
    private Button saveNewFoodButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_food);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
        }

        foodName = findViewById(R.id.create_food_name);
        calories = findViewById(R.id.create_food_cal);
        carbs = findViewById(R.id.create_food_carbs);
        proteins = findViewById(R.id.create_food_proteins);
        fats = findViewById(R.id.create_food_fats);

        backButton = findViewById(R.id.back_to_main_activity_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainWindowActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("fragment", "food");
                startActivity(intent);
            }
        });

        saveNewFoodButton = findViewById(R.id.create_new_food_button);
        saveNewFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient client = new OkHttpClient();

                String url = getApplication().getBaseContext().getString(R.string.url) + "foods/create";

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("food", foodName.getText().toString());
                    jsonObject.put("cal", calories.getText().toString());
                    jsonObject.put("carbs", carbs.getText().toString());
                    jsonObject.put("proteins", proteins.getText().toString());
                    jsonObject.put("fats", fats.getText().toString());
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
                        FoodCreateActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response.isSuccessful()) {
                                    Gson gson = new Gson();
                                    ResponseString response = gson.fromJson(myResponse, ResponseString.class);
                                    if (response.getResponse().equals("Maistas pridėtas")) {
                                        Toast.makeText(getApplicationContext(), response.getResponse(), Toast.LENGTH_LONG).show();
                                        foodName.getText().clear();
                                        calories.getText().clear();
                                        carbs.getText().clear();
                                        proteins.getText().clear();
                                        fats.getText().clear();
                                    } else {
                                        Toast.makeText(getApplicationContext(), response.getResponse(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
    }


}
