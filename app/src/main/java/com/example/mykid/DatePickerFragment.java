package com.example.mykid;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment/* implements DatePickerDialog.OnDateSetListener*/{

    final int ADD_FRAG = 0, EDIT_FRAG = 1;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        Log.d("id",Integer.toString(getId()));
        switch (getId()) {
            case ADD_FRAG:
                return new DatePickerDialog(getActivity(), addFrag, year, month, day);
            case EDIT_FRAG:
                return new DatePickerDialog(getActivity(),editFrag,year,month,day);
            default:
                break;
        }
        return null;
    }

    DatePickerDialog.OnDateSetListener addFrag= new DatePickerDialog.OnDateSetListener() {
       @Override
       public void onDateSet(DatePicker datePicker, int year, int month, int day) {
           AddReportFrag addReportFrag=(AddReportFrag)getParentFragment();
           addReportFrag.processDatePickerResult(year, month, day);
       }
   };

    DatePickerDialog.OnDateSetListener editFrag = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            EditFragment editFragment = (EditFragment)getParentFragment();
            editFragment.processDatePickerResult(year, month, day);
        }
    };


//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        final Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//
//        return new DatePickerDialog(getActivity(), this, year, month, day);
//
//    }

//    @Override
//    public void onDateSet(DatePicker datePicker,
//                          int year, int month, int day) {
//        AddReportFrag addFrag=(AddReportFrag)getParentFragment();
//        addFrag.processDatePickerResult(year, month, day);
//    }

//    public void processDatePickerResult(int year, int month, int day){
//        String month_string = Integer.toString(month+1); // bc start from 0
//        String day_string = Integer.toString(day);
//        String year_string = Integer.toString(year);
//        String date = ( day_string+"/"  +month_string+ "/" + year_string);
//
//        AddReportFrag addFrag=(AddReportFrag)getParentFragment();
//        EditFragment editFrag=(EditFragment)getParentFragment();
//        if(addFrag.isAdded()) {
//            //addFrag.setDate(date);
//            addFrag.processDatePickerResult(year,month,day);
//        }
//        if(editFrag.isAdded()){
//            //editFrag.setDate(date)
//            editFrag.processDatePickerResult(year,month,day);
//        }
//    }
}

