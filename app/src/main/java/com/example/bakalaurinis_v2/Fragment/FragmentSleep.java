package com.example.bakalaurinis_v2.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bakalaurinis_v2.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentSleep extends Fragment {

    private String id;
    private Spinner melodiesSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sleep, container, false);
        id = this.getArguments().getString("id");
        melodiesSpinner = rootView.findViewById(R.id.sleep_melodies);

        setSpinnerValues();
        return rootView;
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
}
