package com.example.bakalaurinis_v2.Recipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bakalaurinis_v2.Food.FoodCreateActivity;
import com.example.bakalaurinis_v2.MainWindowActivity;
import com.example.bakalaurinis_v2.R;
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


public class RecipeCreateActivity extends AppCompatActivity {

    private String id;
    private Button backButton;
    private Button createRecipe;
    private EditText name;
    private EditText recipeUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_recipe);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
        }

        name = findViewById(R.id.create_recipe_name);
        recipeUrl = findViewById(R.id.create_recipe_url);

        backButton = findViewById(R.id.back_to_main_activity_button_in_recipes);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainWindowActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("fragment", "recipes");
                startActivity(intent);
            }
        });


        createRecipe = findViewById(R.id.create_new_recipe_button_in_activity);
        createRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient client = new OkHttpClient();

                String url = getApplication().getBaseContext().getString(R.string.url) + "recipes/create";

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name", name.getText().toString());
                    jsonObject.put("url", recipeUrl.getText().toString());
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
                        RecipeCreateActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response.isSuccessful()) {
                                    Gson gson = new Gson();
                                    ResponseString response = gson.fromJson(myResponse, ResponseString.class);
                                    if (response.getResponse().equals("Receptas pridėtas")) {
                                        Toast.makeText(getApplicationContext(), response.getResponse(), Toast.LENGTH_LONG).show();
                                        name.getText().clear();
                                        recipeUrl.getText().clear();
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
