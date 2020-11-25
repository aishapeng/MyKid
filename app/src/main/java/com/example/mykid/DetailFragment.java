package com.example.mykid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DetailFragment extends Fragment {

    private TextView activityNameTextView,locationInputTxtView,dateInputTxtView,timeInputTxtView,reporterNameTextView;
    private Button locationBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_detail, container, false);
        activityNameTextView= view.findViewById(R.id.activityNameTextView);
        locationInputTxtView= view.findViewById(R.id.locationInputTxtView);
        dateInputTxtView = view.findViewById(R.id.dateInputTxtView);
        timeInputTxtView = view.findViewById(R.id.timeInputTxtView);
        reporterNameTextView = view.findViewById(R.id.reporterNameTextView);
        locationBtn = view.findViewById(R.id.locationBtn);



        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }
}