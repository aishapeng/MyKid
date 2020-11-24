package com.example.mykid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ReportViewHolder> {

    private final LayoutInflater layoutInflater;
    private List<Report> reportList;

    ReportListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
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
            Report current = reportList.get(position);
            holder.reportName.setText(current.getReportName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatActivity activity= (AppCompatActivity)view.getContext();
                    DetailFragment detailFragment= new DetailFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_sec,detailFragment).commit();
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

    class ReportViewHolder extends RecyclerView.ViewHolder  {
        private final TextView reportName;

        private ReportViewHolder(View view) {
            super(view);
            reportName = view.findViewById(R.id.activityNameTextView);
        }

    }
}