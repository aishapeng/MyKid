package com.example.mykid;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddReportFrag extends Fragment implements View.OnClickListener {

    private TextView dateInputTxtView,timeInputTxtView,locationInputTxtView,actErrorMsg,dateErrorMsg,timeErrorMsg,reporterErrorMsg;
    private EditText actNameEditTxt,reporterNameEditTxt;
    ReportViewModel reportViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_add_report, container, false);
        dateInputTxtView = view.findViewById(R.id.dateInputTxtView);
        timeInputTxtView = view.findViewById(R.id.timeInputTxtView);
        actNameEditTxt= view.findViewById(R.id.actNameEditTxt);
        reporterNameEditTxt=view.findViewById(R.id.reporterNameEditTxt);
        locationInputTxtView=view.findViewById(R.id.locationInputTxtView);
        actErrorMsg=view.findViewById(R.id.actErrorMsg);
        dateErrorMsg=view.findViewById(R.id.dateErrorMsg);
        timeErrorMsg=view.findViewById(R.id.timeErrorMsg);
        reporterErrorMsg=view.findViewById(R.id.reporterErrorMsg);
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

    public void onClick(View view) {
        DialogFragment newFragment;
        Fragment frag;
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
                ((SecondActivity)getActivity()).openMap();
                break;
            case R.id.completeBtn:
                String activityName,location,date,time,reporter;
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
//                Report report= new Report(activityName,location,date,time,reporter);
//                if(report!=null){
//                    reportViewModel.insert(report);
//                }

            default:
                break;
        }
    }

    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1); // bc start from 0
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String date = ( day_string+"/"  +month_string+ "/" + year_string);

        dateInputTxtView.setText(date);
    }

    public void processTimePickerResult(int hourOfDay, int minute) {
        String hour_string = Integer.toString(hourOfDay);
        String minute_string = Integer.toString(minute);
        String timeMessage = (hour_string + ":" + minute_string);

        timeInputTxtView.setText(timeMessage);
    }

}