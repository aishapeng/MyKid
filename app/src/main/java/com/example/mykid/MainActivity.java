package com.example.mykid;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity  {
   // public static final String EXTRA_MESSAGE="com.example.mykid.extra.MESSAGE";
    RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.fragment_main,recyclerViewFragment).commit();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),SecondActivity.class);
                intent.putExtra("EXTRA_MESSAGE","add");
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
        if (id == R.id.action_darkMode) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}