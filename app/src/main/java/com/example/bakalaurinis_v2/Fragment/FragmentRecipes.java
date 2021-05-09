package com.example.bakalaurinis_v2.Fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bakalaurinis_v2.Food.FoodCreateActivity;
import com.example.bakalaurinis_v2.MainWindowActivity;
import com.example.bakalaurinis_v2.R;
import com.example.bakalaurinis_v2.Recipe.RecipeCreateActivity;
import com.example.bakalaurinis_v2.Recipe.RecipeWebActivity;
import com.example.bakalaurinis_v2.ResponseForms.RecipesForm;
import com.google.gson.Gson;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FragmentRecipes extends Fragment {
    private String id;
    private EditText recipeSearch;
    private Button findRecipe;
    private ListView recipeListView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> arrayList;
    private RecipesForm[] recipes;
    private Button openWebRecipe;
    private Integer selectedRecipeId;
    private Button createNewRecipeButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipes, container, false);
        id = this.getArguments().getString("id");

        recipeSearch = rootView.findViewById(R.id.recipe_search);
        findRecipe = rootView.findViewById(R.id.find_recipe);
        recipeListView = rootView.findViewById(R.id.recipe_list_view);
        openWebRecipe = rootView.findViewById(R.id.openWebRecipe);

        openWebRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebRecipe();
            }
        });

        openWebRecipe.setVisibility(View.GONE);
        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int j = 0; j < recipeListView.getCount(); j++) {
                    recipeListView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                }
                selectedRecipeId = i;
                openWebRecipe.setVisibility(View.VISIBLE);
                recipeListView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.ButtonColor));
            }
        });


        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayList);
        recipeListView.setAdapter(arrayAdapter);

        findRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebRecipe.setVisibility(View.GONE);
                findRecipe();
            }
        });

        createNewRecipeButton = rootView.findViewById(R.id.create_new_recipe_button);
        createNewRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RecipeCreateActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void openWebRecipe() {
        Intent intent = new Intent(getContext(), RecipeWebActivity.class);
        intent.putExtra("url", recipes[selectedRecipeId].getUrl());
        startActivity(intent);
    }

    private void findRecipe() {
        OkHttpClient client = new OkHttpClient();

        String url = getView().getContext().getString(R.string.url) + "recipes/title";

        System.out.println(url + recipeSearch.getText().toString());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("recipetitle", recipeSearch.getText().toString());
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            Gson gson = new Gson();
                            recipes = gson.fromJson(myResponse, RecipesForm[].class);

                            if (recipes.length == 0) {
                                Toast.makeText(getContext(), "Receptas nerastas", Toast.LENGTH_LONG).show();
                                arrayList.clear();
                                recipeListView.setAdapter(arrayAdapter);
                            } else {
                                addRecipesToAdapter();
                            }
                        }
                    }
                });
            }
        });
    }

    private void addRecipesToAdapter() {
        if (!arrayList.isEmpty()) {
            arrayList.clear();
        }

        if (recipes.length > 5) {
            Toast.makeText(getContext(), "Paieška grąžino pirmus 5 rezultatus. Patikslinkite " +
                            "paiešką norint gauti tikslesnius rezultatus",
                    Toast.LENGTH_LONG).show();

            for (int i = 0; i < 5; i++) {
                arrayList.add(recipes[i].getName());
            }
        } else {
            for (RecipesForm recipe : recipes) {
                arrayList.add(recipe.getName());
            }
        }

        recipeListView.setAdapter(arrayAdapter);
    }

}
