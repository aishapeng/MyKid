package com.example.mykid;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ReportRepository {
    private ReportDao reportDao;
    private LiveData<List<Report>> getAllReport;

    ReportRepository(Application application) {
        ReportRoomDatabase db = ReportRoomDatabase.getInstance(application);
        reportDao = db.activityDao();
        getAllReport = reportDao.getAllReport();
    }

    public LiveData<List<Report>> getAllReport() {
        return getAllReport;
    }


    public void insert(Report report) {
        new insertAsyncTask(reportDao).execute(report);
    }

    public void delete(Report report) {
        new deleteAsyncTask(reportDao).execute(report);
    }

    public void update(Report report) {
        new updateAsyncTask(reportDao).execute(report);
    }

    public void deleteAll() {
        new deleteAllAsyncTask(reportDao).execute();
    }

    private static class insertAsyncTask extends android.os.AsyncTask<Report, Void, Void> {
        private ReportDao asyncTaskDao;

        public insertAsyncTask(ReportDao reportDao) {
            asyncTaskDao = reportDao;
        }

        @Override
        protected Void doInBackground(Report... reports) {
            asyncTaskDao.insert(reports[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends android.os.AsyncTask<Report, Void, Void> {
        private ReportDao asyncTaskDao;

        public deleteAsyncTask(ReportDao reportDao) {
            asyncTaskDao = reportDao;
        }

        @Override
        protected Void doInBackground(Report... reports) {
            asyncTaskDao.delete(reports[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends android.os.AsyncTask<Report, Void, Void> {
        private ReportDao asyncTaskDao;

        public updateAsyncTask(ReportDao reportDao) {
            asyncTaskDao = reportDao;
        }

        @Override
        protected Void doInBackground(Report... reports) {
            asyncTaskDao.update(reports[0].getReportName(),reports[0].getReportLocation(),reports[0].getReportDate(),reports[0].getReportTime()
                    ,reports[0].getReporterName(),reports[0].getReportImage(),reports[0].getReportId());
            return null;
        }
    }


    private static class deleteAllAsyncTask extends android.os.AsyncTask<Report, Void, Void> {
        private ReportDao asyncTaskDao;

        public deleteAllAsyncTask(ReportDao reportDao) {
            asyncTaskDao = reportDao;
        }

        @Override
        protected Void doInBackground(Report... reports) {
            asyncTaskDao.deleteAll();
            return null;
        }
    }
}
