package com.example.mykid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import static com.example.mykid.R.color.colorPrimary;


public class SecondActivity extends AppCompatActivity {

    AddReportFrag addReportFrag = new AddReportFrag();
    DetailFragment detailFragment= new DetailFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), colorPrimary)));

        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        Intent intent=getIntent();
        String message=intent.getStringExtra("EXTRA_MESSAGE");


        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            //onBackPressed();
//            Log.d("check",message);
//            if(message.equals("add")){
//                transaction.add(R.id.fragment_mainSec,addReportFrag).commit();
//            }
//            else if(message.equals("detail")){
//                transaction.add(R.id.fragment_mainSec,detailFragment).commit();
//            }
        } else {
            // In portrait
            if(message.equals("add")){
                transaction.replace(R.id.fragment_sec,addReportFrag,"AddFrag").commit();
            }
            else if(message.equals("detail")){
                Report current ;
                current= (Report) intent.getSerializableExtra("Report");
                Bundle arguments = new Bundle();
                arguments.putInt("ReportID", current.getReportId());
                arguments.putString("activityName",current.getReportName());
                arguments.putString("location",current.getReportLocation());
                arguments.putString("date",current.getReportDate());
                arguments.putString("time",current.getReportTime());
                arguments.putString("reporter",current.getReporterName());
                arguments.putString("image", current.getReportImage());
                detailFragment.setArguments(arguments);

                transaction.replace(R.id.fragment_sec,detailFragment).commit();
            }
        }


    }

    public void openMap (String currentLocation, String selectedLocation, String editable) {
        Fragment frag = new GoogleMapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("currentLocation", currentLocation);
        bundle.putString("selectedLocation", selectedLocation);
        bundle.putString("editable", editable);
        frag.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_sec, frag).commit();
        transaction.addToBackStack(null);
    }

}