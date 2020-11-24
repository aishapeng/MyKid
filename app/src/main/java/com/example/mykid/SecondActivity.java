package com.example.mykid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import static com.example.mykid.R.color.darkYellow;


public class SecondActivity extends AppCompatActivity {

    AddReportFrag addReportFrag = new AddReportFrag();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), darkYellow)));

        Intent intent=getIntent();
        String message=intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        if(message.equals("add")){
            FragmentManager manager=getSupportFragmentManager();
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.replace(R.id.fragment_sec,addReportFrag).commit();
            //transaction.addToBackStack(null);
        }
    }

    public void openMap (String currentLocation){
        Fragment frag = new GoogleMapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("currentLocation", currentLocation);
        frag.setArguments(bundle);
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.add(R.id.fragment_sec,frag).commit();
        transaction.addToBackStack(null);
    }
}