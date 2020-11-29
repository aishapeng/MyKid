package com.example.mykid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ReportViewHolder> implements Filterable {

    private  LayoutInflater layoutInflater;
    private List<Report> reportList, reportListAll;
    private static final String TAG = "ReportListAdapter - ";

    public ReportListAdapter(Context context,List<Report>reportList) {
        layoutInflater = LayoutInflater.from(context);
        this.reportList=reportList;
        this.reportListAll=new ArrayList<>(reportList);
    }


    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportListAdapter.ReportViewHolder holder, int position) {
        if (reportList != null) {
            final Report current = reportList.get(position);
            holder.reportName.setText(current.getReportName());
            holder.reportDate.setText(current.getReportDate());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(MainActivity.DUAL_FRAME){
                        AppCompatActivity activity= (AppCompatActivity)view.getContext();
                    DetailFragment detailFragment= new DetailFragment();
                    Bundle arguments = new Bundle();
                    arguments.putInt("ReportID", current.getReportId());
                    arguments.putString("activityName",current.getReportName());
                    arguments.putString("location",current.getReportLocation());
                    arguments.putString("date",current.getReportDate());
                    arguments.putString("time",current.getReportTime());
                    arguments.putString("reporter",current.getReporterName());
                    arguments.putString("image", current.getReportImage());
                    detailFragment.setArguments(arguments);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_mainSec,detailFragment).commit();
                    }
                    else{
                        Intent intent = new Intent(view.getContext(),SecondActivity.class);
                        intent.putExtra("Report",  current);
                        intent.putExtra("EXTRA_MESSAGE","detail");
                        view.getContext().startActivity(intent);

                    }


                }
            });

        }
    }

    @Override
    public int getItemCount() {
        if (reportList != null) {
            return reportList.size();
        } else {
            return 0;
        }
    }

    void setReportList(List<Report> activities) {
        reportList = activities;
        notifyDataSetChanged();
    }

    public Report getReportAt(int position){
        return reportList.get(position);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter =  new Filter() {
        //run on bkground thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Report> filteredList = new ArrayList<>();

            if(reportListAll != null) {
                if (charSequence.toString().isEmpty()) {
                    filteredList.addAll(reportListAll);
                } else {
                    for (Report report : reportListAll) {
                        if (report.getReportName().toLowerCase().contains(charSequence.toString().toLowerCase()) || report.getReportDate().contains(charSequence.toString())) {
                            filteredList.add(report);
                        }
                    }
                }
            }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
        }
        //runs on a ui thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            if(filterResults.values!= null) {
                reportList.clear();
                reportList.addAll((Collection<? extends Report>) filterResults.values);
            }
            notifyDataSetChanged();
        }
    };

    class ReportViewHolder extends RecyclerView.ViewHolder  {
        private final TextView reportName;
        private final TextView reportDate;

        private ReportViewHolder(View view) {
            super(view);
            reportName = view.findViewById(R.id.activityNameTextView);
            reportDate = view.findViewById(R.id.dateTextView);
        }

    }

}