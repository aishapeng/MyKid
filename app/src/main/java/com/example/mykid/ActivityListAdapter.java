package com.example.mykid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ActivityViewHolder> {

    private final LayoutInflater layoutInflater;
    private List<Activity> activityList;

    ActivityListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ActivityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityListAdapter.ActivityViewHolder holder, int position) {
        if (activityList != null) {
            Activity current = activityList.get(position);
            holder.activityName.setText(current.getActivityName());
        }
    }

    @Override
    public int getItemCount() {
        if (activityList != null) {
            return activityList.size();
        } else {
            return 0;
        }
    }

    void setActivityList(List<Activity> activities) {
        activityList = activities;
        notifyDataSetChanged();
    }

    class ActivityViewHolder extends RecyclerView.ViewHolder {
        private final TextView activityName;

        private ActivityViewHolder(View view) {
            super(view);
            activityName = view.findViewById(R.id.activityNameTextView);
        }
    }
}