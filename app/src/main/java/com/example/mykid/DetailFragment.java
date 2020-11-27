package com.example.mykid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class DetailFragment extends Fragment implements View.OnClickListener {

    private TextView activityNameTextView,locationInputTxtView,dateInputTxtView,timeInputTxtView,reporterNameTextView;
    private Button locationBtn,editBtn,deleteBtn;
    private ReportViewModel reportViewModel;
    private String activityName,location,date,time,reporter;
    private int reportID;

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
        deleteBtn=view.findViewById(R.id.deleteBtn);
        editBtn=view.findViewById(R.id.editBtn);

        Bundle bundle = this.getArguments();
        reportID = bundle.getInt("ReportID");
        activityName= bundle.getString("activityName");
        location=bundle.getString("location");
        date=bundle.getString("date");
        time=bundle.getString("time");
        reporter=bundle.getString("reporter");

        activityNameTextView.setText(activityName);
        locationInputTxtView.setText(location);
        dateInputTxtView.setText(date);
        timeInputTxtView.setText(time);
        reporterNameTextView.setText(reporter);

        if(location==null){
            locationBtn.setEnabled(false);
        }
        else{
            locationBtn.setOnClickListener(this);
        }
        deleteBtn.setOnClickListener(this);
        editBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.locationBtn:
//                break;
            case R.id.deleteBtn:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete?")
                        .setMessage("Are you sure you want to delete this report?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                reportViewModel= new ViewModelProvider(getActivity()).get(ReportViewModel.class);
                                Report report= new Report();
                                report.setReportId(reportID);
                                report.setReportLocation(location);
                                report.setReportDate(date);
                                report.setReportTime(time);
                                report.setReporterName(reporter);
                                reportViewModel.delete(report);
                                getParentFragmentManager().popBackStack();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .show();
                break;
            case R.id.editBtn:
//                FragmentManager manager=getParentFragmentManager();
//                FragmentTransaction transaction=manager.beginTransaction();
//                transaction.replace(R.id.fragment_main,addReportFrag).commit();
//                transaction.addToBackStack(null);
                break;
            default:
                break;
        }
    }

}