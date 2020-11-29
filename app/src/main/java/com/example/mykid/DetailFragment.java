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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailFragment extends Fragment implements View.OnClickListener {

    private TextView activityNameTextView,locationInputTxtView,dateInputTxtView,timeInputTxtView,reporterNameTextView,imageTxtView;
    private Button locationBtn,editBtn,deleteBtn;
    private ReportViewModel reportViewModel;
    private String activityName,location,date,time,reporter,reportImage;
    private ImageView imageView;
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
        Log.d("details image: ", "i"+ reportImage);

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
                FragmentManager manager = getParentFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
//                ((MainActivity)getActivity()).openMap(location, null, "false");
//                public void openMap (String selectedLocation, String currentLocation, String editable) {
                Fragment frag = new GoogleMapFragment();
                Bundle bundle = new Bundle();
                bundle.putString("currentLocation", location);
                bundle.putString("selectedLocation", null);
                bundle.putString("editable", "false");
                frag.setArguments(bundle);
                transaction.add(R.id.fragment_sec, frag).commit();
                transaction.addToBackStack(null);
            //}
                break;
            case R.id.deleteBtn:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogCustom);
                builder.setTitle("Delete?")
                        .setMessage("Are you sure you want to delete this report?")
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
//                FragmentManager fragmentManager=getParentFragmentManager();
//                FragmentTransaction transaction=manager.beginTransaction();
                Bundle arguments = new Bundle();
                arguments.putInt("ReportID", reportID);
                arguments.putString("activityName", activityName);
                arguments.putString("location",location);
                arguments.putString("date",date);
                arguments.putString("time",time);
                arguments.putString("reporter",reporter);
                arguments.putString("image",reportImage);
                editFragment.setArguments(arguments);
//                fragmentManager.popBackStack();
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_sec,editFragment).commit();
                //transaction.addToBackStack(null);
//                ((MainActivity)getActivity()).openEditFragment(reportID, activityName, location, date, time, reporter);

                break;
            default:
                break;
        }
    }

}