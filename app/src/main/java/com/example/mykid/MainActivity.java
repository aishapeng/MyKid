package com.example.mykid;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;


public class MainActivity extends AppCompatActivity  {
    RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
    public static boolean DUAL_FRAME=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final AddReportFrag addReportFrag= new AddReportFrag();
        DetailFragment detailFragment = new DetailFragment();
        FrameLayout frameSec= findViewById(R.id.fragment_mainSec);
        DUAL_FRAME= frameSec!=null&&frameSec.getVisibility()==View.VISIBLE;
        final String frag=getIntent().getStringExtra("fragment");

        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.fragment_main,recyclerViewFragment).commit();

        if(frag!=null&&frag=="detail"){
            transaction.replace(R.id.fragment_mainSec,detailFragment).commit();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (DUAL_FRAME){
                    FragmentManager manager=getSupportFragmentManager();
                    FragmentTransaction transaction=manager.beginTransaction();
                    transaction.replace(R.id.fragment_mainSec,addReportFrag).commit();
                    //transaction.addToBackStack(null);

                }
                else{
                    Intent intent=new Intent(getApplicationContext(),SecondActivity.class);
                    intent.putExtra("EXTRA_MESSAGE","add");
                    startActivity(intent);
               }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_DeleteAll) {
            ReportViewModel reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
            reportViewModel.deleteAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        transaction.add(R.id.fragment_mainSec, frag).commit();
        transaction.addToBackStack(null);
    }
}