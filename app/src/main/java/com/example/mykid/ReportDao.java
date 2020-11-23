package com.example.mykid;

import android.app.Activity;

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

    @Update
    void update(Report report);

    @Query("DELETE FROM Report")
    void deleteAll();

    @Query("SELECT * from Report ORDER BY reportDate DESC")
    LiveData<List<Report>> getAllReport();
}
