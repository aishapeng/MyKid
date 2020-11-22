package com.example.mykid;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "activity_table")
public class Activity {
    @PrimaryKey (autoGenerate = true)
    @NonNull
    private int activityId;
    private String activityName;
    private Location activityLocation;
    private Date activityDate;
    private Date activityTime;
    private String reporterName;

    public Activity(@NonNull String activityName, Location activityLocation, @NonNull Date activityDate, @NonNull Date activityTime, @NonNull String reporterName) {
        this.activityName = activityName;
        this.activityLocation = activityLocation;
        this.activityDate = activityDate;
        this.activityTime = activityTime;
        this.reporterName = reporterName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Location getActivityLocation() {
        return activityLocation;
    }

    public void setActivityLocation(Location activityLocation) {
        this.activityLocation = activityLocation;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public Date getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(Date activityTime) {
        this.activityTime = activityTime;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }
}
