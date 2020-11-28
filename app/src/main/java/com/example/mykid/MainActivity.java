package com.example.mykid;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity  {
    public static final String EXTRA_MESSAGE="com.example.mykid.extra.MESSAGE";
    RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        //transaction.replace(R.id.fragment_main,recyclerViewFragment).commit();
        transaction.replace(R.id.fragment_main,recyclerViewFragment).commit();
        //transaction.addToBackStack(null);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),SecondActivity.class);
                intent.putExtra(EXTRA_MESSAGE,"add");
                startActivity(intent);
//                FragmentManager manager=getSupportFragmentManager();
//                FragmentTransaction transaction=manager.beginTransaction();
//                transaction.replace(R.id.fragment_main,addReportFrag).commit();
//                transaction.addToBackStack(null);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openEditFragment (int reportID, String activityName, String location, String date, String time, String reporter){
        Fragment frag = new EditFragment();
        Bundle arguments = new Bundle();
        arguments.putInt("ReportID", reportID);
        arguments.putString("activityName", activityName);
        arguments.putString("location",location);
        arguments.putString("date",date);
        arguments.putString("time",time);
        arguments.putString("reporter",reporter);
        frag.setArguments(arguments);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_main, frag,"EditFrag").commit();
        transaction.addToBackStack(null);
    }

    public void openMap (String selectedLocation, String currentLocation, String editable) {
        Fragment frag = new GoogleMapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("currentLocation", currentLocation);
        bundle.putString("selectedLocation", selectedLocation);
        bundle.putString("editable", editable);
        frag.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_main, frag).commit();
        transaction.addToBackStack(null);
    }


}