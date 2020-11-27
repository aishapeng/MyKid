package com.example.mykid;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ReportViewModel extends AndroidViewModel {
    private ReportRepository reportRepository;
    private LiveData<List<Report>> allReport;
    //private Application app;

    public ReportViewModel(@NonNull Application application) {
        super(application);
        //app=application;
        reportRepository = new ReportRepository(application);
        allReport = reportRepository.getAllReport();

    }

    public LiveData<List<Report>> getAllReport() {
        return allReport;
    }

//    public Report getReport(int id){ return new ReportRepository(app).getReport(id);}

    public void insert(Report report) {
        reportRepository.insert(report);
    }

    public void delete(Report report) {
        reportRepository.delete(report);
    }

    public void update(Report report) {
        reportRepository.update(report);
    }

    public void deleteAll() {
        reportRepository.deleteAll();
    }
}
