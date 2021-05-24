package com.example.bakalaurinis_v2.Fragment;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.bakalaurinis_v2.Food.FoodCreateActivity;
import com.example.bakalaurinis_v2.R;
import com.example.bakalaurinis_v2.ResponseForms.ResponseString;
import com.example.bakalaurinis_v2.Sleep.SleepBroadcastReceiver;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.ALARM_SERVICE;

public class FragmentSleep extends Fragment {

    private String id;
    private String clockAlarm;
    private Spinner melodiesSpinner;
    private Button setAlarmButton;
    private TextView title;
    private TimePicker alarmClock;
    private TimePicker recordClock;
    private RatingBar ratingBar;
    private Button saveRecord;
    private long wakeUpTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sleep, container, false);

        id = this.getArguments().getString("id");
        clockAlarm = this.getArguments().getString("clockAlarm");

        melodiesSpinner = rootView.findViewById(R.id.sleep_melodies);
        setAlarmButton = rootView.findViewById(R.id.sleep_setAlarm);

        alarmClock = rootView.findViewById(R.id.sleep_timePicker);
        alarmClock.setIs24HourView(true);
        alarmClock.setHour(8);
        alarmClock.setMinute(0);
        recordClock = rootView.findViewById(R.id.sleep_quality_timePicker);
        recordClock.setIs24HourView(true);
        recordClock.setHour(8);
        recordClock.setMinute(0);
        ratingBar = rootView.findViewById(R.id.sleep_rating_bar);
        saveRecord = rootView.findViewById(R.id.save_sleep_record);

        title = rootView.findViewById(R.id.textView22);

        setHiddenElements();
        if (clockAlarm.equals("true")) {
            showHiddenElements();
        }
        createNotificationChannel();

        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SleepBroadcastReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

                AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);

                long timeAtButtonClick = System.currentTimeMillis();
                wakeUpTime = 0;

                Calendar datetime = Calendar.getInstance();
                Calendar c = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, alarmClock.getHour());
                datetime.set(Calendar.MINUTE, alarmClock.getMinute());

                if(datetime.getTimeInMillis() > c.getTimeInMillis()){
                    wakeUpTime = datetime.getTimeInMillis() - c.getTimeInMillis();
                    Long hours = wakeUpTime / (60000 * 60);
                    Long minutes = (wakeUpTime - (hours * (60000 * 60))) / 60000;
                    Toast.makeText(getContext(), "Žadintuvas skambės po " + hours + " valandų ir " + minutes + " minučių", Toast.LENGTH_LONG).show();
                    System.out.println("po to - " + Long.toString((datetime.getTimeInMillis() - c.getTimeInMillis())/60000));
                }else{
                    wakeUpTime = datetime.getTimeInMillis() + 86400000 - c.getTimeInMillis();
                    Long hours = wakeUpTime / (60000 * 60);
                    Long minutes = (wakeUpTime - (hours * (60000 * 60))) / 60000;
                    Toast.makeText(getContext(), "Žadintuvas skambės po " + hours + " valandų ir " + minutes + " minučių", Toast.LENGTH_LONG).show();
                    System.out.println("po to - " + Long.toString((datetime.getTimeInMillis() + 86400000 - c.getTimeInMillis() )/60000));
                }
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        timeAtButtonClick + wakeUpTime,
                        pendingIntent);

            }
        });

        saveRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient client = new OkHttpClient();

                String url = getContext().getString(R.string.url) + "sleep/create";

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("userid", id);
                    jsonObject.put("duration", recordClock.getHour() * 60 + recordClock.getMinute());
                    jsonObject.put("quality", ratingBar.getRating());
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
                                    ResponseString response = gson.fromJson(myResponse, ResponseString.class);
                                    if (response.getResponse().equals("Miego įrašas pridėtas")) {
                                        setHiddenElements();
                                        Toast.makeText(getContext(), response.getResponse(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });

        setSpinnerValues();
        return rootView;
    }

    private void showHiddenElements() {
        title.setVisibility(View.VISIBLE);
        recordClock.setVisibility(View.VISIBLE);
        ratingBar.setVisibility(View.VISIBLE);
        saveRecord.setVisibility(View.VISIBLE);
    }

    private void setHiddenElements() {
        title.setVisibility(View.GONE);
        recordClock.setVisibility(View.GONE);
        ratingBar.setVisibility(View.GONE);
        saveRecord.setVisibility(View.GONE);
    }

    private void setSpinnerValues() {
        List<String> melodiesList = new ArrayList<String>();
        melodiesList.add("Paukščiai");
        melodiesList.add("Vėjas");
        melodiesList.add("Krioklys");
        melodiesList.add("Bitės");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, melodiesList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        melodiesSpinner.setAdapter(dataAdapter);
    }

    private void createNotificationChannel() {
        CharSequence name = "LemubitReminderChannel";
        String description = "Channel for";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("notifyClock", name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
