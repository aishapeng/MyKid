package com.example.mykid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailFragment extends Fragment implements View.OnClickListener {

    private TextView activityNameTextView,locationInputTxtView,dateInputTxtView,timeInputTxtView,reporterNameTextView,imageTxtView;
    private Button locationBtn,editBtn,deleteBtn;
    private ImageView imageView;

    private String activityName,location,date,time,reporter,reportImage;
    private int reportID;
    private ReportViewModel reportViewModel;

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
        imageView = view.findViewById(R.id.imageView);
        imageTxtView = view.findViewById(R.id.imageTxtView);

        Bundle bundle = this.getArguments();
        reportID = bundle.getInt("ReportID");
        activityName= bundle.getString("activityName");
        location=bundle.getString("location");
        date=bundle.getString("date");
        time=bundle.getString("time");
        reporter=bundle.getString("reporter");
        reportImage = bundle.getString("image");

        activityNameTextView.setText(activityName);
        locationInputTxtView.setText(location);
        dateInputTxtView.setText(date);
        timeInputTxtView.setText(time);
        reporterNameTextView.setText(reporter);

        if(location==null){
            locationBtn.setEnabled(false);
        } else{
            locationBtn.setOnClickListener(this);
        }

        if(reportImage==null){
            imageTxtView.setVisibility(view.GONE);
        }else{
            Picasso.get().load(reportImage).into(imageView);
        }

        deleteBtn.setOnClickListener(this);
        editBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.locationBtn:
                    ((MainActivity)getActivity()).openMap(null, location, "false");
                break;

            case R.id.deleteBtn:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogCustom);
                builder.setTitle("Delete?")
                        .setMessage("Are you sure you want to delete this activity?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                reportViewModel= new ViewModelProvider(getActivity()).get(ReportViewModel.class);
                                Report report= new Report();
                                report.setReportId(reportID);
                                report.setReportName(activityName);
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
                EditFragment editFragment = new EditFragment();
                Bundle arguments = new Bundle();
                arguments.putInt("ReportID", reportID);
                arguments.putString("activityName", activityName);
                arguments.putString("location",location);
                arguments.putString("date",date);
                arguments.putString("time",time);
                arguments.putString("reporter",reporter);
                arguments.putString("image",reportImage);
                editFragment.setArguments(arguments);

                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_mainSec,editFragment).addToBackStack(null).commit();
                break;

            default:
                break;
        }
    }
}