package com.example.mykid;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ActivityViewModel extends AndroidViewModel {
    private ActivityRepository activityRepository;
    private LiveData<List<Activity>> allActivity;

    public ActivityViewModel(@NonNull Application application) {
        super(application);
        activityRepository = new ActivityRepository(application);
        allActivity = activityRepository.getAllActivity();
    }

    public LiveData<List<Activity>> getAllActivity(){
        return allActivity;
    }

    public void insert(Activity activity){
        activityRepository.insert(activity);
    }

    public void delete(Activity activity){
        activityRepository.delete(activity);
    }

    public void update(Activity activity){
        activityRepository.update(activity);
    }
}
