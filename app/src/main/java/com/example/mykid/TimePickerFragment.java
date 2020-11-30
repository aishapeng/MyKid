package com.example.mykid;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),R.style.DialogTheme,this,hour,minute,false);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {


        if (getTag().equals("timePickerAddFrag")) {
            AddReportFrag addReportFrag = (AddReportFrag)getParentFragment();
            addReportFrag.processTimePickerResult(hourOfDay,minute );
        }
        else if(getTag().equals("timePickerEditFrag")){
            EditFragment editFragment = (EditFragment)getParentFragment();
            editFragment.processTimePickerResult(hourOfDay,minute );
        }
    }
}