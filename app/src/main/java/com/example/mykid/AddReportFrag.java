package com.example.mykid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddReportFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddReportFrag extends Fragment implements View.OnClickListener {

    private TextView dateInputTxtView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddReportFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment add_acttivity_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static AddReportFrag newInstance(String param1, String param2) {
        AddReportFrag fragment = new AddReportFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_add_report, container, false);
        dateInputTxtView = view.findViewById(R.id.dateInputTxtView);
        Button addDateBtn = view.findViewById(R.id.dateBtn);
        addDateBtn.setOnClickListener(this);
        return view;
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dateBtn:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getChildFragmentManager(), "datePicker"); //getsupportmanager to show, tag is identifier
        }
    }

    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getChildFragmentManager(), "datePicker"); //getsupportmanager to show, tag is identifier

    }

    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1); // bc start from 0
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String date = ( day_string+"/"  +month_string+ "/" + year_string);

        dateInputTxtView.setText(date);
    }

}