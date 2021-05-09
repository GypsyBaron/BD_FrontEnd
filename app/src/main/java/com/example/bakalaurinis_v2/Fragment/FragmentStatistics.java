package com.example.bakalaurinis_v2.Fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.bakalaurinis_v2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FragmentStatistics extends Fragment {

    private String id;
    private EditText start;
    private EditText end;
    private Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
        start = rootView.findViewById(R.id.statistics_start);
        end =  rootView.findViewById(R.id.statistics_end);
        spinner = rootView.findViewById(R.id.statistics_spinner);
        setSpinnerValues();
        return rootView;
     }

    private void setSpinnerValues() {
        List<String> criterias = new ArrayList<String>();
        criterias.add("Kalorijos");
        criterias.add("Angliavandeniai");
        criterias.add("Baltymai");
        criterias.add("Riebalai");
        criterias.add("Žingsniai");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, criterias);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
}
