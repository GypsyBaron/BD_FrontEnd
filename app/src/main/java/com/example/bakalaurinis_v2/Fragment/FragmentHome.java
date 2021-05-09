package com.example.bakalaurinis_v2.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bakalaurinis_v2.Authentification.LoginActivity;
import com.example.bakalaurinis_v2.MainWindowActivity;
import com.example.bakalaurinis_v2.R;
import com.example.bakalaurinis_v2.ResponseForms.LoginResponseForm;
import com.example.bakalaurinis_v2.ResponseForms.TodayFoodStatistic;
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

public class FragmentHome extends Fragment {

    private TextView calories;
    private TextView carbs;
    private TextView proteins;
    private TextView fats;
    private TextView steps;
    private String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        id = this.getArguments().getString("id");
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        calories = rootView.findViewById(R.id.home_calories);
        carbs = rootView.findViewById(R.id.home_carbs);
        proteins = rootView.findViewById(R.id.home_proteins);
        fats = rootView.findViewById(R.id.home_fats);
        steps = rootView.findViewById(R.id.home_steps);

        getTodayStatistic();

        return rootView;
    }

    private void getTodayStatistic() {
        OkHttpClient client = new OkHttpClient();

        String url = getContext().getString(R.string.url) + "records/today";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
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
                            TodayFoodStatistic response = gson.fromJson(myResponse, TodayFoodStatistic.class);
                            calories.setText(response.getCal().toString()+ "/2390");
                            carbs.setText(Integer.valueOf(response.getCarbs().intValue()).toString() + "/328");
                            proteins.setText(Integer.valueOf(response.getProteins().intValue()).toString() + "/120");
                            fats.setText(Integer.valueOf(response.getFats().intValue()).toString() + "/67");
                        }
                    }
                });
            }
        });
    }
}
