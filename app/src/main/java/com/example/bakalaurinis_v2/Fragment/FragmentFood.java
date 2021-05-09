package com.example.bakalaurinis_v2.Fragment;


import android.app.Activity;
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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bakalaurinis_v2.Authentification.LoginActivity;
import com.example.bakalaurinis_v2.Food.FoodCreateActivity;
import com.example.bakalaurinis_v2.ResponseForms.FoodForm;
import com.example.bakalaurinis_v2.R;
import com.example.bakalaurinis_v2.ResponseForms.ResponseString;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FragmentFood extends Fragment {

    private EditText foodSearch;
    private Button findFood;
    private ListView foodListView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> arrayList;
    private FoodForm[] foods;
    private EditText foodWeight;
    private Spinner foodSpinner;
    private String id;
    private Button saveFood;
    private Integer listViewId;
    private Button createFood;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        id = this.getArguments().getString("id");
        View rootView = inflater.inflate(R.layout.fragment_food, container, false);

        foodSearch = rootView.findViewById(R.id.food_search);
        findFood = rootView.findViewById(R.id.find_food);
        foodListView = rootView.findViewById(R.id.food_list_view);
        foodWeight = rootView.findViewById(R.id.food_weight);
        foodSpinner = rootView.findViewById(R.id.food_spinner);
        saveFood = rootView.findViewById(R.id.saveFood);
        createFood = rootView.findViewById(R.id.create_new_food);

        saveFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSelectedFood();
            }
        });
        setSpinnerValues();

        createFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewFood();
            }
        });

        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listViewId = i;
                for (int j = 0; j < foodListView.getCount(); j++) {
                    foodListView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                }
                Toast.makeText(getContext(), "ID - "  + i, Toast.LENGTH_LONG).show();
                foodListView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.ButtonColor));
            }
        });

        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayList);
        foodListView.setAdapter(arrayAdapter);

        findFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findFoodInDataBase();
            }
        });
        return rootView;
    }

    private void createNewFood() {
        Intent intent = new Intent(getContext(), FoodCreateActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void saveSelectedFood() {
        OkHttpClient client = new OkHttpClient();

        String url = getView().getContext().getString(R.string.url) + "records/create";

        LocalDate today = LocalDate.now();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", id);
            jsonObject.put("foodid", foods[listViewId].getId());
            jsonObject.put("type", foodSpinner.getSelectedItem().toString());
            jsonObject.put("date", today.toString());
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
                            ResponseString responseString  = gson.fromJson(myResponse, ResponseString.class);
                            Toast.makeText(getContext(), responseString.getResponse(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void setSpinnerValues() {
        List<String> foodTypeList = new ArrayList<String>();
        foodTypeList.add("Pusryčiai");
        foodTypeList.add("Pietūs");
        foodTypeList.add("Vakarienė");
        foodTypeList.add("Užkandžiai");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, foodTypeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodSpinner.setAdapter(dataAdapter);
    }

    private void findFoodInDataBase() {
        OkHttpClient client = new OkHttpClient();

        String url = getView().getContext().getString(R.string.url) + "foods/title";

        System.out.println(url + foodSearch.getText().toString());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("foodtitle", foodSearch.getText().toString());
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
                            foods = gson.fromJson(myResponse, FoodForm[].class);

                            if (foods.length == 0) {
                                Toast.makeText(getContext(), "Maistas nerastas", Toast.LENGTH_LONG).show();
                                arrayList.clear();
                                foodListView.setAdapter(arrayAdapter);
                            } else {
                                addFoodToAdapter();
                            }
                        }
                    }
                });
            }
        });
    }

    private void addFoodToAdapter() {

        if (!arrayList.isEmpty()) {
            arrayList.clear();
        }

        if (foods.length > 5) {
            Toast.makeText(getContext(), "Paieška grąžino pirmus 5 rezultatus. Patikslinkite " +
                    "paiešką norint gauti tikslesnius rezultatus",
                    Toast.LENGTH_LONG).show();

            for (int i = 0; i < 5; i++) {
                arrayList.add(foods[i].getFood());
            }
        } else {
            for (FoodForm food : foods) {
                arrayList.add(food.getFood());
            }
        }

        foodListView.setAdapter(arrayAdapter);
    }
}
