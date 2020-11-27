package com.example.mykid;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;

public class EditFragment extends Fragment implements View.OnClickListener{

    private TextView dateInputTxtView,timeInputTxtView,locationInputTxtView,actErrorMsg,dateErrorMsg,timeErrorMsg,reporterErrorMsg;
    private EditText actNameEditTxt,reporterNameEditTxt;
    ReportViewModel reportViewModel;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient mFusedLocationClient;

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_edit, container, false);
        dateInputTxtView = view.findViewById(R.id.dateInputTxtView);
        timeInputTxtView = view.findViewById(R.id.timeInputTxtView);
        actNameEditTxt= view.findViewById(R.id.actNameEditTxt);
        reporterNameEditTxt=view.findViewById(R.id.reporterNameEditTxt);
        locationInputTxtView=view.findViewById(R.id.locationInputTxtView);
        actErrorMsg=view.findViewById(R.id.actErrorMsg);
        dateErrorMsg=view.findViewById(R.id.dateErrorMsg);
        timeErrorMsg=view.findViewById(R.id.timeErrorMsg);
        reporterErrorMsg=view.findViewById(R.id.reporterErrorMsg);
        reportViewModel= new ViewModelProvider(this).get(ReportViewModel.class);
        Button addDateBtn = view.findViewById(R.id.dateBtn);
        Button addTimeBtn = view.findViewById(R.id.timeBtn);
        Button locationBtn = view.findViewById(R.id.locationBtn);
        Button completeBtn=view.findViewById(R.id.completeBtn);
        addDateBtn.setOnClickListener(this);
        addTimeBtn.setOnClickListener(this);
        locationBtn.setOnClickListener(this);
        completeBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        DialogFragment newFragment;
        switch (view.getId()){
            case R.id.dateBtn:
                newFragment = new DatePickerFragment();
                newFragment.show(getChildFragmentManager(), "datePicker"); //getsupportmanager to show, tag is identifier
                break;
            case R.id.timeBtn:
                newFragment = new TimePickerFragment();
                newFragment.show(getChildFragmentManager(),"timePicker");
                break;
            case R.id.locationBtn:
                //getLocation(); //get user current location
                break;
            case R.id.completeBtn:
                String activityName,location,date,time,reporter,id=null;
                activityName=actNameEditTxt.getText().toString();
                location=locationInputTxtView.getText().toString();
                date=dateInputTxtView.getText().toString();
                time=timeInputTxtView.getText().toString();
                reporter=reporterNameEditTxt.getText().toString();
                if(activityName.isEmpty()){
                    actErrorMsg.setVisibility(View.VISIBLE);
                }
                else {
                    actErrorMsg.setVisibility(View.INVISIBLE);
                }
                if(date.isEmpty()){
                    dateErrorMsg.setVisibility(View.VISIBLE);
                }
                else {
                    dateErrorMsg.setVisibility(View.INVISIBLE);
                }
                if(time.isEmpty()){
                    timeErrorMsg.setVisibility(View.VISIBLE);
                }
                else {
                    timeErrorMsg.setVisibility(View.INVISIBLE);
                }
                if(reporter.isEmpty()){
                    reporterErrorMsg.setVisibility(View.VISIBLE);
                }
                else {
                    reporterErrorMsg.setVisibility(View.INVISIBLE);
                }
                if(location.isEmpty()){
                    location=null;
                }
                if(!activityName.isEmpty() && !date.isEmpty() &&!time.isEmpty() && !reporter.isEmpty()){
                    Report report= new Report(activityName,location,date,time,reporter);
                    reportViewModel.insert(report);
                    Intent intent = new Intent (getActivity(), MainActivity.class);
                    startActivity (intent);
                }
            default:
                break;
        }
    }

    public void setDate(String s){
        dateInputTxtView.setText(s);
    }

//    public void processDatePickerResult(int year, int month, int day){
//        String month_string = Integer.toString(month+1); // bc start from 0
//        String day_string = Integer.toString(day);
//        String year_string = Integer.toString(year);
//        String date = ( day_string+"/"  +month_string+ "/" + year_string);
//
//        dateInputTxtView.setText(date);
//    }
//
//    public void processTimePickerResult(int hourOfDay, int minute) {
//        String hour_string = Integer.toString(hourOfDay);
//        String minute_string = Integer.toString(minute);
//        String timeMessage = (hour_string + ":" + minute_string);
//
//        timeInputTxtView.setText(timeMessage);
//    }
}