package com.example.mykid;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class RecyclerViewFragment extends Fragment {

    private ActivityViewModel activityViewModel;
    private RecyclerView recyclerView;

    public RecyclerViewFragment() {
        // Required empty public constructor
    }

    public static RecyclerViewFragment newInstance(String param1, String param2) {
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerView = container.findViewById(R.id.recyclerview);
        final ActivityListAdapter adapter = new ActivityListAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        activityViewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
        activityViewModel.getAllActivity().observe(getViewLifecycleOwner(), new Observer<List<Activity>>() {
            @Override
            public void onChanged(List<Activity> activities) {

            }
        });
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }
}