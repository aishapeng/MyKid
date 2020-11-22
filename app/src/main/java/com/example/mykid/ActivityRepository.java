package com.example.mykid;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ActivityRepository {
    private ActivityDao activityDao;
    private LiveData<List<Activity>> getAllActivity;

    ActivityRepository(Application application){
        ActivityRoomDatabase db= ActivityRoomDatabase.getInstance(application);
        activityDao= db.activityDao();
        getAllActivity=activityDao.getAllActivity();
    }

    public LiveData<List<Activity>> getAllActivity(){
        return getAllActivity;
    }

    public void insert(Activity activity){
        new insertAsyncTask(activityDao).execute(activity);
    }

    public void delete(Activity activity){
        new deleteAsyncTask(activityDao).execute(activity);
    }

    public void update(Activity activity){
        new updateAsyncTask(activityDao).execute(activity);
    }

    private static class insertAsyncTask extends android.os.AsyncTask<Activity, Void, Void> {
        private ActivityDao asyncTaskDao;
        public insertAsyncTask(ActivityDao activityDao) {
            asyncTaskDao = activityDao;
        }

        @Override
        protected Void doInBackground(Activity... activities) {
            asyncTaskDao.insert(activities[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends android.os.AsyncTask<Activity, Void, Void> {
        private ActivityDao asyncTaskDao;
        public deleteAsyncTask(ActivityDao activityDao) {
            asyncTaskDao = activityDao;
        }

        @Override
        protected Void doInBackground(Activity... activities) {
            asyncTaskDao.delete(activities[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends android.os.AsyncTask<Activity, Void, Void> {
        private ActivityDao asyncTaskDao;
        public updateAsyncTask(ActivityDao activityDao) {
            asyncTaskDao = activityDao;
        }

        @Override
        protected Void doInBackground(Activity... activities) {
            asyncTaskDao.update(activities[0]);
            return null;
        }
    }
}
