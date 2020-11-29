package com.example.mykid;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class EditFragment extends Fragment implements FetchAddressTask.OnTaskCompleted, View.OnClickListener{

    private TextView dateInputTxtView,timeInputTxtView,locationInputTxtView,actErrorMsg,dateErrorMsg,timeErrorMsg,reporterErrorMsg;
    private EditText actNameEditTxt,reporterNameEditTxt;
    ReportViewModel reportViewModel;
    private String activityName,location,date,time,reporter, selectedlocation;
    private int reportID;
    private String newactivityName,newlocation,newdate,newtime,newreporter;

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

        Bundle bundle = this.getArguments();
        reportID = bundle.getInt("ReportID");
        activityName= bundle.getString("activityName");
        location=bundle.getString("location");
        date=bundle.getString("date");
        time=bundle.getString("time");
        reporter=bundle.getString("reporter");

        actNameEditTxt.setText(activityName);
        locationInputTxtView.setText(location);
        dateInputTxtView.setText(date);
        timeInputTxtView.setText(time);
        reporterNameEditTxt.setText(reporter);

        addDateBtn.setOnClickListener(this);
        addTimeBtn.setOnClickListener(this);
        locationBtn.setOnClickListener(this);
        completeBtn.setOnClickListener(this);

        // Initialize the FusedLocationClient.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        getParentFragmentManager().setFragmentResultListener("location", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                selectedlocation = bundle.getString("location");
                locationInputTxtView.setText(selectedlocation);
                // Do something with the result
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        DialogFragment newFragment;
        switch (view.getId()){
            case R.id.dateBtn:
                newFragment = new DatePickerFragment();
                newFragment.show(getChildFragmentManager(), "datePickerEditFrag"); //getsupportmanager to show, tag is identifier
                break;
            case R.id.timeBtn:
                newFragment = new TimePickerFragment();
                newFragment.show(getChildFragmentManager(),"timePickerEditFrag");
                break;
            case R.id.locationBtn:
                if (location == null){
                    getLocation(); //get user current location
                }else{
//                    ((MainActivity)getActivity()).openMap(location, null, "true");
                    Fragment frag = new GoogleMapFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("currentLocation", location);
                    bundle.putString("selectedLocation", null);
                    bundle.putString("editable", "false");
                    frag.setArguments(bundle);
                    getParentFragmentManager().beginTransaction().add(R.id.fragment_sec, frag).addToBackStack(null).commit();
                    //transaction.addToBackStack(null);
                }
                break;
            case R.id.completeBtn:
                newactivityName=actNameEditTxt.getText().toString();
                newlocation=locationInputTxtView.getText().toString();
                newdate=dateInputTxtView.getText().toString();
                newtime=timeInputTxtView.getText().toString();
                newreporter=reporterNameEditTxt.getText().toString();
                if(newactivityName.isEmpty()){
                    actErrorMsg.setVisibility(View.VISIBLE);
                }
                else {
                    actErrorMsg.setVisibility(View.INVISIBLE);
                }
                if(newdate.isEmpty()){
                    dateErrorMsg.setVisibility(View.VISIBLE);
                }
                else {
                    dateErrorMsg.setVisibility(View.INVISIBLE);
                }
                if(newtime.isEmpty()){
                    timeErrorMsg.setVisibility(View.VISIBLE);
                }
                else {
                    timeErrorMsg.setVisibility(View.INVISIBLE);
                }
                if(newreporter.isEmpty()){
                    reporterErrorMsg.setVisibility(View.VISIBLE);
                }
                else {
                    reporterErrorMsg.setVisibility(View.INVISIBLE);
                }
                if(newlocation.isEmpty()){
                    newlocation=null;
                }
                if(!newactivityName.isEmpty() && !newdate.isEmpty() &&!newtime.isEmpty() && !newreporter.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogCustom);
                    builder.setTitle("Confirmation")
                            .setMessage("Are you sure you want to add an activity with these details?\n" +
                                    "Activity Name: " + newactivityName + "\n" +
                                    "Location: " + newlocation + "\n" +
                                    "Date: " + newdate + "\n" +
                                    "Time: " + newtime + "\n" +
                                    "Reporter: " + newreporter)
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Report report= new Report();
                                    report.setReportId(reportID);
                                    report.setReportName(newactivityName);
                                    report.setReportLocation(newlocation);
                                    report.setReportDate(newdate);
                                    report.setReportTime(newtime);
                                    report.setReporterName(newreporter);
                                    reportViewModel.update(report);

                                    AppCompatActivity activity= (AppCompatActivity)getContext();
                                    DetailFragment detailFragment= new DetailFragment();
                                    Bundle arguments = new Bundle();
                                    arguments.putInt("ReportID", report.getReportId());
                                    arguments.putString("activityName",report.getReportName());
                                    arguments.putString("location",report.getReportLocation());
                                    arguments.putString("date",report.getReportDate());
                                    arguments.putString("time",report.getReportTime());
                                    arguments.putString("reporter",report.getReporterName());
                                    detailFragment.setArguments(arguments);
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main,detailFragment).addToBackStack(null).commit();

//                                    Intent intent = new Intent (getActivity(), MainActivity.class);
//                                    startActivity (intent);

                                }
                            })
                            .setNegativeButton("Cancel",null)
                            .show();
                }
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
        String timeMessage = (String.format("%02d:%02d",hourOfDay , minute));

        timeInputTxtView.setText(timeMessage);
    }


    //standard code
    public void getLocation() { //check for the ACCESS_FINE_LOCATION permission.
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] // [] can pass more than one permission at the same time//??
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                new FetchAddressTask(getActivity(), EditFragment.this).execute((location));
                            }
                        }
                    });
        }
    }

    //standard code
    @Override
    //request permission, then now chk permission result with this function
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation(); // permit le can get location le
                } else {
                    Toast.makeText(getActivity(),
                            R.string.location_permission_denied,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onTaskCompleted(String result) {
//        ((MainActivity)getActivity()).openMap(null, result,"true"); //open google map
    }


}