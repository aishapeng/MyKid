package com.example.mykid;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ReportDao {
    @Insert
    void insert(Report report);

    @Delete
    void delete(Report report);

    @Query("UPDATE Report SET reportName = :reportName, reportLocation = :reportLocation,reportDate = :reportDate, reportTime = :reportTime, reporterName = :reporterName " +
            "WHERE reportId = :reportId")
    void update(String reportName, String reportLocation, String reportDate, String reportTime, String reporterName,int reportId);

    @Query("DELETE FROM Report")
    void deleteAll();

    @Query("SELECT * from Report ORDER BY reportDate DESC")
    LiveData<List<Report>> getAllReport();

//    @Query("SELECT * FROM Report WHERE reportId = :id")
//    Report loadReport(int id);

}
